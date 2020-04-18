package me.zhengjie.modules.product.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.product.domain.InspectionRecordTemplate;
import me.zhengjie.modules.product.repository.InspectionRecordTemplateRepository;
import me.zhengjie.modules.product.service.InspectionRecordTemplateService;
import me.zhengjie.modules.product.service.dto.InspectionRecordTemplateQueryCriteria;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
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
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@CacheConfig(cacheNames = "inspectionRecordTemplate")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class InspectionRecordTemplateServiceImpl implements InspectionRecordTemplateService {

    @Autowired
    private InspectionRecordTemplateRepository repository;


    @Override
    @Cacheable(key = "#p0")
    public InspectionRecordTemplate findById(long id) {
        InspectionRecordTemplate recordTemplate = repository.findById(id).orElseGet(InspectionRecordTemplate::new);
        ValidationUtil.isNull(recordTemplate.getId(), "inspectionRecordTemplate", "id", id);
        return recordTemplate;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public InspectionRecordTemplate create(InspectionRecordTemplate resources) {
        return repository.save(resources);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            repository.deleteById(id);
        }
    }

    @Override
    @Cacheable
    public Object queryAll(InspectionRecordTemplateQueryCriteria criteria, Pageable pageable) {
        Page<InspectionRecordTemplate> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    @Override
    @Cacheable
    public List<InspectionRecordTemplate> queryAll(InspectionRecordTemplateQueryCriteria criteria) {
        return repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
    }

    @Override
    public void download(List<InspectionRecordTemplate> queryAll, HttpServletResponse response) throws IOException {

    }
}
