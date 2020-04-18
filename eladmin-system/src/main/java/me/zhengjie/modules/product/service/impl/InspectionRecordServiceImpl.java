package me.zhengjie.modules.product.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.product.domain.InspectionRecord;
import me.zhengjie.modules.product.domain.InspectionRecordTemplate;
import me.zhengjie.modules.product.domain.InspectionTemplate;
import me.zhengjie.modules.product.repository.InspectionRecordRepository;
import me.zhengjie.modules.product.repository.InspectionRecordTemplateRepository;
import me.zhengjie.modules.product.service.InspectionItemService;
import me.zhengjie.modules.product.service.InspectionRecordService;
import me.zhengjie.modules.product.service.InspectionTemplateService;
import me.zhengjie.modules.product.service.dto.*;
import me.zhengjie.modules.product.service.mapper.InspectionRecordMapper;
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

@Service
@Slf4j
@CacheConfig(cacheNames = "inspectionRecord")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class InspectionRecordServiceImpl implements InspectionRecordService {

    @Autowired
    private InspectionRecordRepository recordRepository;

    @Autowired
    private InspectionRecordMapper recordMapper;

    @Autowired
    private InspectionItemService itemService;

    @Autowired
    private InspectionRecordTemplateRepository templateRepository;

    @Override
    @Cacheable(key = "#p0")
    public InspectionRecordDto findById(long id) {
        InspectionRecord record = recordRepository.findById(id).orElseGet(InspectionRecord::new);
        ValidationUtil.isNull(record.getId(), "inspectionRecord", "id", id);
        return recordMapper.toDto(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public InspectionRecordDto create(InspectionRecordData resources) {

        if (recordRepository.findByCode(resources.getInspectionRecord().getCode()) != null) {
            throw new EntityExistException(InspectionRecord.class, "code", resources.getInspectionRecord().getCode());
        }

        /**
         * 先将记录保存
         */
        InspectionRecordDto inspectionRecordDto = resources.getInspectionRecord();
        InspectionRecord record = recordMapper.toEntity(inspectionRecordDto);
        record = recordRepository.save(record);

        // 处理数据
        if (isPassed(resources, record)) {
            record.setIsPassed(1);
        } else {
            record.setIsPassed(0);
        }
        return recordMapper.toDto(recordRepository.save(record));
    }

    /**
     * 判断是否合格
     * @param resources
     * @param record
     * @return
     */
    public boolean isPassed(InspectionRecordData resources, InspectionRecord record) {
        boolean result = true;

        // 模板
        InspectionTemplateSmallDto templateSmallDto = resources.getInspectionTemplate();

        // 检测项和值
        Set<InspectionItemSmallDto> inspectionItems = resources.getInspectionItems();

        // 与检测项内的上限和下限数值对比
        for (InspectionItemSmallDto itemSmallDto : inspectionItems) {
            // 创建一个包含模板、检测项、检测记录的对象
            InspectionRecordTemplate recordTemplate = new InspectionRecordTemplate();
            // 记录ID
            recordTemplate.setInspectionRecordId(record.getId());
            // 模板
            recordTemplate.setInspectionTemplateId(templateSmallDto.getId());
            recordTemplate.setInspectionTemplateName(templateSmallDto.getName());
            // 检测项目
            recordTemplate.setInspectionItemId(itemSmallDto.getId());
            recordTemplate.setInspectionItemName(itemSmallDto.getName());
            recordTemplate.setInspectMethod(resources.getInspectMethod());

            // 逐个检查
            if (!detect(itemSmallDto, recordTemplate)) {
                result = false;
            }
            templateRepository.save(recordTemplate);
        }
        return result;
    }

    /**
     * 逐个检测每一项
     * @param itemSmallDto
     * @param recordTemplate
     * @return
     */
    public boolean detect(InspectionItemSmallDto itemSmallDto, InspectionRecordTemplate recordTemplate) {
        InspectionItemDto itemDto = itemService.findById(itemSmallDto.getId());
        Double lowerLimit = itemDto.getLowerLimit();
        String lowerLimitComparator = itemDto.getLowerLimitComparator();
        Double upperLimit = itemDto.getUpperLimit();
        String upperLimitComparator = itemDto.getUpperLimitComparator();

        if (lowerLimitComparator.equals(">")){
            if (itemSmallDto.getItemValue() <= lowerLimit){
                recordTemplate.setIsDetected(1);
                recordTemplate.setIsExceeded(0);
                return false;
            }
        }else if (lowerLimitComparator.equals(">=")){
            if (itemSmallDto.getItemValue() < lowerLimit){
                recordTemplate.setIsDetected(1);
                recordTemplate.setIsExceeded(0);
                return false;
            }
        }

        if (upperLimitComparator.equals("<")){
            if (itemSmallDto.getItemValue() >= upperLimit){
                recordTemplate.setIsDetected(1);
                recordTemplate.setIsExceeded(1);
                return false;
            }
        }else if (upperLimitComparator.equals("<=")){
            if (itemSmallDto.getItemValue() > upperLimit){
                recordTemplate.setIsDetected(1);
                recordTemplate.setIsExceeded(1);
                return false;
            }
        }

        recordTemplate.setIsExceeded(0);
        recordTemplate.setIsDetected(0);
        return true;
    }


    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(InspectionRecord resources) {
        InspectionRecord record = recordRepository.findById(resources.getId()).orElseGet(InspectionRecord::new);
        ValidationUtil.isNull(record.getId(), "inspectionTemplate", "id", resources.getId());

        InspectionRecord record1 = recordRepository.findByCode(record.getCode());

        if (record1 != null && !record.getId().equals(record1.getId())) {
            throw new EntityExistException(InspectionRecord.class, "code", resources.getCode());
        }

        record.setCode(record1.getCode());
        record.setBatchNumber(record1.getBatchNumber());
        record.setDept(record1.getDept());
        record.setInspector(record1.getInspector());
        record.setInspectTime(record1.getInspectTime());
        record.setIsPassed(record1.getIsPassed());
        record.setIsSubmit(record1.getIsSubmit());
        record.setProduct(record1.getProduct());
        record.setRemark(record1.getRemark());

        recordRepository.save(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            recordRepository.deleteById(id);
            templateRepository.deleteByInspectionRecordId(id);
        }
    }

    @Override
    @Cacheable
    public Object queryAll(InspectionRecordQueryCriteria criteria, Pageable pageable) {
        Page<InspectionRecord> page = recordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(recordMapper::toDto));
    }

    @Override
    @Cacheable
    public List<InspectionRecordDto> queryAll(InspectionRecordQueryCriteria criteria) {
        List<InspectionRecord> records = recordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return recordMapper.toDto(records);
    }

    @Override
    public void download(List<InspectionRecordDto> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (InspectionRecordDto recordDTO : queryAll) {
            // 检测项
//            List<String> roles = userDTO.getRoles().stream().map(RoleSmallDto::getName).collect(Collectors.toList());
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("编号", recordDTO.getCode());
            map.put("批次", recordDTO.getBatchNumber());
            map.put("检测员", recordDTO.getInspector());
            map.put("检测日期", recordDTO.getInspectTime());
            map.put("是否通过", recordDTO.getIsPassed() == 1 ? "是" : "否");
            map.put("产品名称", recordDTO.getProduct().getName());
            map.put("产品编码", recordDTO.getProduct().getCode());
            map.put("组织机构", recordDTO.getDept().getName());
            map.put("是否提交", recordDTO.getIsPassed());
            map.put("备注", recordDTO.getRemark());
            map.put("创建日期", recordDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
