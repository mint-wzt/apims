package me.zhengjie.modules.product.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.product.domain.DescriptionItem;
import me.zhengjie.modules.product.repository.DescriptionItemRepository;
import me.zhengjie.modules.product.service.DescriptionItemService;
import me.zhengjie.modules.product.service.dto.DescriptionItemDto;
import me.zhengjie.modules.product.service.dto.DescriptionItemQueryCriteria;
import me.zhengjie.modules.product.service.mapper.DescriptionItemMapper;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.dto.RoleSmallDto;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.FileUtil;
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
import java.util.*;

@Slf4j
@Service
@CacheConfig(cacheNames = "description")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DescriptionItemServiceImpl implements DescriptionItemService {

    @Autowired
    private DescriptionItemRepository descriptionItemRepository;

    @Autowired
    private DescriptionItemMapper descriptionItemMapper;

    @Override
    @Cacheable
    public DescriptionItemDto findById(long id) {
        DescriptionItem item = descriptionItemRepository.findById(id).orElseGet(DescriptionItem::new);
        ValidationUtil.isNull(item.getId(),"description","id",id);
        return descriptionItemMapper.toDto(item);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public DescriptionItemDto create(DescriptionItem resources) {
        if (descriptionItemRepository.findByCode(resources.getCode()) != null) {
            throw new EntityExistException(DescriptionItem.class, "code", resources.getCode());
        }
        log.info(JSON.toJSONString(resources));
        DescriptionItem descriptionItem = descriptionItemRepository.save(resources);
        return descriptionItemMapper.toDto(descriptionItem);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(DescriptionItem resources) {
        DescriptionItem descriptionItem = descriptionItemRepository.findById(resources.getId()).orElseGet(DescriptionItem::new);
        ValidationUtil.isNull(descriptionItem.getId(), "descriptionItem", "id", descriptionItem.getId());
        DescriptionItem descriptionItem1 = descriptionItemRepository.findByCode(resources.getCode());

        // 编码重复
        if (descriptionItem1 != null && !descriptionItem.getId().equals(descriptionItem1.getId())) {
            throw new EntityExistException(DescriptionItem.class, "code", resources.getCode());
        }

        descriptionItem.setCategory(resources.getCategory());
        descriptionItem.setDataRange(resources.getDataRange());
        descriptionItem.setChineseName(resources.getChineseName());
        descriptionItem.setCode(resources.getCode());
        descriptionItem.setRestrictions(resources.getRestrictions());
        descriptionItem.setMark(resources.getMark());
        descriptionItem.setRemark(resources.getRemark());
        descriptionItem.setEnglishName(resources.getEnglishName());
        descriptionItem.setDatatypeFormat(resources.getDatatypeFormat());
        descriptionItem.setSynonym(resources.getSynonym());
        descriptionItem.setUnit(resources.getUnit());

        descriptionItemRepository.save(descriptionItem);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            descriptionItemRepository.deleteById(id);
        }
    }

    @Override
    @Cacheable
    public Object queryAll(DescriptionItemQueryCriteria criteria, Pageable pageable) {
        Page<DescriptionItem> page = descriptionItemRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(descriptionItemMapper::toDto));
    }

    @Override
    @Cacheable
    public List<DescriptionItemDto> queryAll(DescriptionItemQueryCriteria criteria) {
        List<DescriptionItem> page = descriptionItemRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return descriptionItemMapper.toDto(page);
    }

    @Override
    public void download(List<DescriptionItemDto> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DescriptionItemDto itemDto : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("编码", itemDto.getCode());
            map.put("中文名称", itemDto.getChineseName());
            map.put("英文名称", itemDto.getEnglishName());
            map.put("所属类型编码", itemDto.getCategory().getCode());
            map.put("所属类型名称", itemDto.getCategory().getName());
            map.put("标记", itemDto.getMark());
            map.put("说明", itemDto.getDescription());
            map.put("数据类型及格式", itemDto.getDatatypeFormat());
            map.put("值域", itemDto.getDataRange());
            map.put("同义词", itemDto.getSynonym());
            map.put("约束/条件", itemDto.getRestrictions());
            map.put("计量单位", itemDto.getUnit());
            map.put("备注", itemDto.getRemark());
            map.put("创建时间", itemDto.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
