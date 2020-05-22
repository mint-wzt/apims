package me.zhengjie.modules.statistics.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.product.domain.SalesRecord;
import me.zhengjie.modules.statistics.domain.IndustryStatistics;
import me.zhengjie.modules.statistics.domain.ProductStatistics;
import me.zhengjie.modules.statistics.repository.IndustryStatisticsRepository;
import me.zhengjie.modules.statistics.service.IndustryStatisticsService;
import me.zhengjie.modules.statistics.service.dto.IndustryStatisticsDto;
import me.zhengjie.modules.statistics.service.dto.IndustryStatisticsQueryCriteria;
import me.zhengjie.modules.system.domain.Region;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.RegionRepository;
import me.zhengjie.modules.system.service.RegionService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.SecurityUtils;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@CacheConfig(cacheNames = "industry-statistics")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class IndustryStatisticsServiceImpl implements IndustryStatisticsService {

    @Autowired
    private IndustryStatisticsRepository statisticsRepository;

    @Autowired
    private RegionService regionService;

    @Autowired
    private UserService userService;

    @Override
    public IndustryStatistics findIndustryStatistics(String regionId, String statisticsItem) {
        return statisticsRepository.findByRegionIdAndStatisticsItem(regionId, statisticsItem);
    }

    /**
     * 添加企业分布数据
     *
     * @param resources /
     * @return
     */
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public IndustryStatistics create(IndustryStatistics resources) {
        addToParent(resources);
        return resources;
    }

    @Override
    @Cacheable
    public IndustryStatisticsDto get(IndustryStatisticsQueryCriteria criteria) {
        if (criteria.getRegionName() == null) {
            UserDto user = userService.findByName(SecurityUtils.getUsername());
            criteria.setRegionName(user.getRegion().getExtName());
        }
        IndustryStatisticsDto statisticsDto = new IndustryStatisticsDto();
        List<IndustryStatistics> statistics = statisticsRepository.findByStatistics(criteria.getRegionName());
        log.info(JSON.toJSONString(statistics));
        for (IndustryStatistics s : statistics) {
            switch (s.getStatisticsItem()) {
                case "种植面积":
                    statisticsDto.setPlantingArea(s.getStatisticsTotal());
                    break;
                case "养殖数量":
                    statisticsDto.setBreedingNumber(s.getStatisticsTotal());
                    break;
                case "组织机构":
                    statisticsDto.setEnterpriseNumber(s.getStatisticsTotal());
                    break;
                case "员工":
                    statisticsDto.setEmployeeNumber(s.getStatisticsTotal());
                    break;
                case "种植业":
                    statisticsDto.setPlanting(s.getStatisticsTotal());
                    break;
                case "畜牧业":
                    statisticsDto.setAnimalHusbandry(s.getStatisticsTotal());
                    break;
                case "渔业":
                    statisticsDto.setFishery(s.getStatisticsTotal());
                    break;
                default:
                    break;
            }
        }
        log.info(JSON.toJSONString(statisticsDto));
        return statisticsDto;
    }

    public IndustryStatistics add(IndustryStatistics resources) {
        IndustryStatistics industryStatistics = findIndustryStatistics(resources.getRegionId(), resources.getStatisticsItem());
        IndustryStatistics statistics;
        if (industryStatistics == null) {
            statistics = statisticsRepository.save(resources);
        } else {
            industryStatistics.setStatisticsTotal(industryStatistics.getStatisticsTotal() + resources.getStatisticsTotal());
            statistics = statisticsRepository.save(industryStatistics);
        }
        return statistics;
    }

    // 对行政区域的上级添加项目的数据
    public void addToParent(IndustryStatistics industryStatistics) {
        List<Region> regions = regionService.findParents(industryStatistics.getRegionId());
        log.info(JSON.toJSONString(regions));
        for (Region region : regions) {
            IndustryStatistics statistics = new IndustryStatistics();
            statistics.setRegionId(region.getId());
            statistics.setRegionName(region.getExtName());
            statistics.setStatisticsItem(industryStatistics.getStatisticsItem());
            statistics.setStatisticsTotal(industryStatistics.getStatisticsTotal());
            statistics.setStatisticsTime(industryStatistics.getStatisticsTime());
            statistics.setUnit(industryStatistics.getUnit());
            statistics.setCreateUid(industryStatistics.getCreateUid());
            statistics.setCreateUsername(industryStatistics.getCreateUsername());
            add(statistics);
        }
    }

    @Override
    @Cacheable
    public List<IndustryStatistics> queryAll(IndustryStatisticsQueryCriteria criteria) {
        List<IndustryStatistics> records = statisticsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return records;
    }

    @Override
    public void download(List<IndustryStatistics> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (IndustryStatistics statistics : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("地区", statistics.getRegionName());
            map.put("邮编", statistics.getRegionId());
            map.put("统计项目",statistics.getStatisticsItem());
            map.put("数量", statistics.getStatisticsTotal());
            map.put("单位", statistics.getUnit());
            map.put("统计日期",statistics.getStatisticsTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Cacheable
    public Object get(IndustryStatisticsQueryCriteria criteria, Pageable pageable) {
        Page<IndustryStatistics> statistics = statisticsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder),pageable);
        log.info(JSON.toJSONString(statistics));
        return PageUtil.toPage(statistics);
    }
}
