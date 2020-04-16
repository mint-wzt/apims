package me.zhengjie.modules.product.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.product.domain.InspectionTemplate;
import me.zhengjie.modules.product.repository.InspectionTemplateRepository;
import me.zhengjie.modules.product.service.InspectionTemplateService;
import me.zhengjie.modules.product.service.dto.InspectionItemSmallDto;
import me.zhengjie.modules.product.service.dto.InspectionTemplateDto;
import me.zhengjie.modules.product.service.dto.InspectionTemplateQueryCriteria;
import me.zhengjie.modules.product.service.mapper.InspectionTemplateMapper;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.dto.RoleSmallDto;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.modules.system.service.mapper.UserMapper;
import me.zhengjie.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@CacheConfig(cacheNames = "inspectionTemplate")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class InspectionTemplateServiceImpl implements InspectionTemplateService {

    @Autowired
    private InspectionTemplateRepository templateRepository;

    @Autowired
    private InspectionTemplateMapper templateMapper;

    @Autowired
    private UserRepository userRepository;


    /**
     * 根据ID查询模板
     *
     * @param id ID
     * @return
     */
    @Override
    public InspectionTemplateDto findById(long id) {
        InspectionTemplate template = templateRepository.findById(id).orElseGet(InspectionTemplate::new);
        ValidationUtil.isNull(template.getId(), "inspection_template", "id", id);
        return templateMapper.toDto(template);
    }

    @Override
    public InspectionTemplateDto create(InspectionTemplate resources) {
        User user = userRepository.findByUsername(SecurityUtils.getUsername());
        ValidationUtil.isNull(user.getId(),"user","id",user.getId());
        if (templateRepository.findByNameAndUser(resources.getName(),user) != null){
            throw new EntityExistException(InspectionTemplate.class,"InspectionTemplate 记录已存在","id");
        }
        resources.setUser(user);
        return templateMapper.toDto(templateRepository.save(resources));
    }

    @Override
    public void update(InspectionTemplate resources) {
        InspectionTemplate template = templateRepository.findById(resources.getId()).orElseGet(InspectionTemplate::new);
        ValidationUtil.isNull(template.getId(),"inspection_template","id",resources.getId());
        // 查询用户
        User user = userRepository.findByUsername(SecurityUtils.getUsername());
        InspectionTemplate template1 = templateRepository.findByNameAndUser(resources.getName(),user);
        if (template1 != null && !template.getId().equals(template1.getId())){
            throw new EntityExistException(InspectionTemplate.class,"InspectionTemplate 记录已存在","id");
        }
        template.setName(resources.getName());
        template.setEnabled(resources.getEnabled());
        template.setInspectionItems(resources.getInspectionItems());
        template.setItType(resources.getItType());
        template.setProduct(resources.getProduct());
        template.setScope(resources.getScope());
        template.setRemark(resources.getRemark());
        template.setUser(resources.getUser());
        templateRepository.save(template);
    }

    @Override
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            templateRepository.deleteById(id);
        }
    }

    @Override
    public Object queryAll(InspectionTemplateQueryCriteria criteria, Pageable pageable) {
        Page<InspectionTemplate> page = templateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        log.info(JSON.toJSONString(page));
        return PageUtil.toPage(page.map(templateMapper::toDto));
    }

    @Override
    public List<InspectionTemplateDto> queryAll(InspectionTemplateQueryCriteria criteria) {
        List<InspectionTemplate> templates = templateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return templateMapper.toDto(templates);
    }

    @Override
    public void download(List<InspectionTemplateDto> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (InspectionTemplateDto templateDTO : queryAll) {
            List<String> inspectionItems = templateDTO.getInspectionItems().stream().map(InspectionItemSmallDto::getName).collect(Collectors.toList());
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("模板名称", templateDTO.getName());
            map.put("模板类型", templateDTO.getItType());
            map.put("使用范围", templateDTO.getScope());
            map.put("状态", templateDTO.getEnabled() == 1 ? "启用" : "禁用");
            map.put("所属人", templateDTO.getUser().getUsername());
            map.put("检测子项", inspectionItems);
            map.put("检测产品名称", templateDTO.getProduct().getName());
            map.put("检测产品分类", templateDTO.getProduct().getName());
            map.put("所属部门", templateDTO.getUser().getDept().getName());
            map.put("备注", templateDTO.getRemark());
            map.put("创建日期", templateDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
