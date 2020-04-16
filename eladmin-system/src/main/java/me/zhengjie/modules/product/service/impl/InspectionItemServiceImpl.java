package me.zhengjie.modules.product.service.impl;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.aop.log.Log;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.product.domain.DescriptionItem;
import me.zhengjie.modules.product.domain.InspectionItem;
import me.zhengjie.modules.product.repository.InspectionItemRepository;
import me.zhengjie.modules.product.service.InspectionItemService;
import me.zhengjie.modules.product.service.dto.DescriptionItemDto;
import me.zhengjie.modules.product.service.dto.InspectionItemDto;
import me.zhengjie.modules.product.service.dto.InspectionItemQueryCriteria;
import me.zhengjie.modules.product.service.mapper.InspectionItemMapper;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.hibernate.annotations.CreationTimestamp;
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
import java.util.*;

@Service
@Slf4j
@CacheConfig(cacheNames = "inspection-item")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class InspectionItemServiceImpl implements InspectionItemService {

    @Autowired
    private InspectionItemRepository inspectionItemRepository;

    @Autowired
    private InspectionItemMapper inspectionItemMapper;


    @Override
    @Cacheable
    public InspectionItemDto findById(long id) {
        InspectionItem inspectionItem = inspectionItemRepository.findById(id).orElseGet(InspectionItem::new);
        ValidationUtil.isNull(inspectionItem.getId(), "inspection_item", "id", id);
        return inspectionItemMapper.toDto(inspectionItem);
    }


    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public InspectionItemDto create(InspectionItem resources) {
        if (inspectionItemRepository.findByCode(resources.getCode()) != null) {
            throw new EntityExistException(InspectionItem.class, "code", resources.getCode());
        }
        InspectionItem item = inspectionItemRepository.save(resources);
        return inspectionItemMapper.toDto(item);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(InspectionItem resources) {
        // 根据id查询数据库中原有的记录
        InspectionItem inspectionItem = inspectionItemRepository.findById(resources.getId()).orElseGet(InspectionItem::new);
        ValidationUtil.isNull(inspectionItem.getId(), "inspection_item", "id", resources.getId());

        // 根据code查询数据库中原有的记录
        InspectionItem inspectionItem1 = inspectionItemRepository.findByCode(resources.getCode());

        // 如果为true则说明已经存在一个相同code的检测项
        if (inspectionItem1 != null && !inspectionItem.getId().equals(inspectionItem1.getId())) {
            throw new EntityExistException(InspectionItem.class, "code", resources.getCode());
        }

        inspectionItem.setCode(resources.getCode());
        inspectionItem.setName(resources.getName());
        inspectionItem.setUnit(resources.getUnit());
        inspectionItem.setCompareDirection(resources.getCompareDirection());
        inspectionItem.setCheckoutType(resources.getCheckoutType());
        inspectionItem.setLowerLimit(resources.getLowerLimit());
        inspectionItem.setLowerLimitComparator(resources.getLowerLimitComparator());
        inspectionItem.setUpperLimit(resources.getUpperLimit());
        inspectionItem.setUpperLimitComparator(resources.getUpperLimitComparator());
        inspectionItem.setReferenceValue1(resources.getReferenceValue1());
        inspectionItem.setReferenceValue2(resources.getReferenceValue2());
        inspectionItem.setReferenceValue3(resources.getReferenceValue3());
        inspectionItem.setClassificationCode(resources.getClassificationCode());
        inspectionItem.setClassificationName(resources.getClassificationName());
        inspectionItem.setVersion(resources.getVersion());
        inspectionItem.setBackup1(resources.getBackup1());
        inspectionItem.setBackup2(resources.getBackup2());
        inspectionItem.setBackup3(resources.getBackup3());
        inspectionItem.setBackup4(resources.getBackup4());
        inspectionItem.setBackup5(resources.getBackup5());

        inspectionItemRepository.save(inspectionItem);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            inspectionItemRepository.deleteById(id);
        }
    }

    @Override
    @Cacheable
    public Object queryAll(Pageable pageable) {
        return inspectionItemMapper.toDto(inspectionItemRepository.findAll(pageable).getContent());
    }

    @Override
    @Cacheable
    public Object queryAll(InspectionItemQueryCriteria criteria, Pageable pageable) {
        Page<InspectionItem> page = inspectionItemRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(inspectionItemMapper::toDto));
    }

    @Override
    @Cacheable
    public List<InspectionItemDto> queryAll(InspectionItemQueryCriteria criteria) {
        List<InspectionItem> page = inspectionItemRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return inspectionItemMapper.toDto(page);
    }

    @Override
    public void download(List<InspectionItemDto> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (InspectionItemDto itemDto : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("编码", itemDto.getCode());
            map.put("检测项名称", itemDto.getName());
            map.put("单位", itemDto.getUnit());
            map.put("比较方向", itemDto.getCompareDirection());
            map.put("检出类型", itemDto.getCheckoutType());
            map.put("参考下限", itemDto.getLowerLimit());
            map.put("下限比较符", itemDto.getLowerLimitComparator());
            map.put("参考上限", itemDto.getUpperLimit());
            map.put("上限比较符", itemDto.getUpperLimitComparator());
            map.put("参考值1", itemDto.getReferenceValue1());
            map.put("参考值2", itemDto.getReferenceValue2());
            map.put("参考值3", itemDto.getReferenceValue3());
            map.put("分类代码", itemDto.getClassificationCode());
            map.put("分类名称", itemDto.getClassificationName());
            map.put("版本号", itemDto.getVersion());
            map.put("备用1", itemDto.getBackup1());
            map.put("备用2", itemDto.getBackup2());
            map.put("备用3", itemDto.getBackup3());
            map.put("备用4", itemDto.getBackup4());
            map.put("备用5", itemDto.getBackup5());
            map.put("创建时间", itemDto.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
