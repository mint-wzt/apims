package me.zhengjie.modules.system.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.statistics.domain.IndustryStatistics;
import me.zhengjie.modules.statistics.service.IndustryStatisticsService;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.Region;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.RegionRepository;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.dto.DeptDto;
import me.zhengjie.modules.system.service.dto.DeptQueryCriteria;
import me.zhengjie.modules.system.service.dto.UserQueryCriteria;
import me.zhengjie.utils.*;
import me.zhengjie.modules.system.repository.DeptRepository;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.modules.system.service.mapper.DeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Zheng Jie
 * @date 2019-03-25
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "dept")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DeptServiceImpl implements DeptService {

    private final DeptRepository deptRepository;

    private final DeptMapper deptMapper;

    private final RegionRepository regionRepository;

    private final UserRepository userRepository;

    private final IndustryStatisticsService statisticsService;

    public DeptServiceImpl(DeptRepository deptRepository, DeptMapper deptMapper, RegionRepository regionRepository, UserRepository userRepository,IndustryStatisticsService statisticsService) {
        this.deptRepository = deptRepository;
        this.deptMapper = deptMapper;
        this.regionRepository = regionRepository;
        this.userRepository = userRepository;
        this.statisticsService = statisticsService;
    }

    @Override
    @Cacheable
    public List<DeptDto> queryAll(DeptQueryCriteria criteria) {
        List<Dept> depts = deptRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        log.info(JSON.toJSONString(depts));
        return deptMapper.toDto(depts);
    }

    @Override
    @Cacheable
    public Object queryAll(DeptQueryCriteria criteria, Pageable pageable) {
        Page<Dept> page = deptRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(deptMapper::toDto));
    }

    @Override
    @Cacheable(key = "#p0")
    public DeptDto findById(Long id) {
        Dept dept = deptRepository.findById(id).orElseGet(Dept::new);
        ValidationUtil.isNull(dept.getId(), "Dept", "id", id);
        return deptMapper.toDto(dept);
    }

    @Override
    @Cacheable
    public List<Dept> findByPid(long pid) {
        return deptRepository.findByPid(pid);
    }

    @Override
    public Set<Dept> findByRoleIds(Long id) {
        return deptRepository.findByRoles_Id(id);
    }

    @Override
    @Cacheable
    public Object buildTree(List<DeptDto> deptDtos) {
        Set<DeptDto> trees = new LinkedHashSet<>();
        Set<DeptDto> depts = new LinkedHashSet<>();
        List<String> deptNames = deptDtos.stream().map(DeptDto::getName).collect(Collectors.toList());
        boolean isChild;
        for (DeptDto deptDTO : deptDtos) {
            isChild = false;
            if ("0".equals(deptDTO.getPid().toString())) {
                trees.add(deptDTO);
            }
            for (DeptDto it : deptDtos) {
                if (it.getPid().equals(deptDTO.getId())) {
                    isChild = true;
                    if (deptDTO.getChildren() == null) {
                        deptDTO.setChildren(new ArrayList<>());
                    }
                    deptDTO.getChildren().add(it);
                }
            }
            if (isChild) {
                depts.add(deptDTO);
            } else if (!deptNames.contains(deptRepository.findNameById(deptDTO.getPid()))) {
                depts.add(deptDTO);
            }
        }

        if (CollectionUtils.isEmpty(trees)) {
            trees = depts;
        }

        Integer totalElements = deptDtos.size();

        Map<String, Object> map = new HashMap<>(2);
        map.put("totalElements", totalElements);
        map.put("content", CollectionUtils.isEmpty(trees) ? deptDtos : trees);
        return map;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public DeptDto create(Dept resources) {
        // 先保存region
        Region r = resources.getRegion();
        // 选择了地区r ！= null，则将address作为该部门的地址，
        // 否则，将上级部门地址作为该部门的地址
        if (r.getId() != null) {
            // 设计基础信息
            Region region = regionRepository.findById(r.getId()).orElseGet(Region::new);
            if (region == null){
                region.setId(r.getId());
            }
            region.setExtName(r.getExtName());
            region.setName(r.getName());
            region.setPid(r.getPid());
            region.setExtId(r.getExtId());

            region.setProvinceId(r.getProvinceId());
            region.setProvinceName(r.getProvinceName());
            region.setCityId(r.getCityId());
            region.setCityName(r.getCityName());
            region.setAreaName(r.getAreaName());
            region.setAreaId(r.getAreaId());
            region.setTownId(r.getTownId());
            region.setTownName(r.getTownName());

            regionRepository.save(r);
        } else {
            // 设置子部门所属区域
            Dept dept = deptRepository.findById(resources.getPid()).get();
            resources.setAddress(dept.getAddress());
            resources.setRegion(dept.getRegion());
        }
        // 设置创建者ID
        resources.setCreateUid(userRepository.findByUsername(SecurityUtils.getUsername()).getId());
        Dept dept = deptRepository.save(resources);

        // 为地区添加组织机构数
        IndustryStatistics industryStatistics = new IndustryStatistics();
        industryStatistics.setRegionId(dept.getRegion().getId());
        industryStatistics.setRegionName(regionRepository.findById(dept.getRegion().getId()).get().getExtName());
        industryStatistics.setStatisticsTime(dept.getCreateTime());
        industryStatistics.setStatisticsTotal(1d);
        industryStatistics.setUnit("个");
        if (dept.getDeptType() == 1){
            industryStatistics.setStatisticsItem("组织机构");
        } else {
            industryStatistics.setStatisticsItem("行政单位");
        }
        // 记录企业数
        statisticsService.create(industryStatistics);
        return deptMapper.toDto(dept);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Dept resources) {
        if (resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        Dept dept = deptRepository.findById(resources.getId()).orElseGet(Dept::new);
        ValidationUtil.isNull(dept.getId(), "Dept", "id", resources.getId());
        resources.setId(dept.getId());
        deptRepository.save(resources);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<DeptDto> deptDtos) {
        for (DeptDto deptDto : deptDtos) {
            deptRepository.deleteById(deptDto.getId());
        }
    }

    @Override
    public void download(List<DeptDto> deptDtos, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DeptDto deptDTO : deptDtos) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("部门名称", deptDTO.getName());
            map.put("部门状态", deptDTO.getEnabled() ? "启用" : "停用");
            map.put("创建日期", deptDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Set<DeptDto> getDeleteDepts(List<Dept> menuList, Set<DeptDto> deptDtos) {
        for (Dept dept : menuList) {
            deptDtos.add(deptMapper.toDto(dept));
            List<Dept> depts = deptRepository.findByPid(dept.getId());
            if (depts != null && depts.size() != 0) {
                getDeleteDepts(depts, deptDtos);
            }
        }
        return deptDtos;
    }
}
