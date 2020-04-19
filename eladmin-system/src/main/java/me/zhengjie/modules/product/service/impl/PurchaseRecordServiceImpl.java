package me.zhengjie.modules.product.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.product.domain.PurchaseRecord;
import me.zhengjie.modules.product.repository.PurchaseRecordRepository;
import me.zhengjie.modules.product.service.PurchaseRecordService;
import me.zhengjie.modules.product.service.dto.PurchaseRecordDto;
import me.zhengjie.modules.product.service.dto.PurchaseRecordQueryCriteria;
import me.zhengjie.modules.product.service.mapper.PurchaseRecordMapper;
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
@CacheConfig(cacheNames = "purchase-record")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PurchaseRecordServiceImpl implements PurchaseRecordService {

    @Autowired
    private PurchaseRecordRepository purchaseRepository;

    @Autowired
    private PurchaseRecordMapper purchaseMapper;

    @Override
    @Cacheable(key = "#p0")
    public PurchaseRecordDto findById(long id) {
        PurchaseRecord record = purchaseRepository.findById(id).orElseGet(PurchaseRecord::new);
        ValidationUtil.isNull(record.getId(), "purchase-record", "id", id);
        return purchaseMapper.toDto(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public PurchaseRecordDto create(PurchaseRecord resources) {
        return purchaseMapper.toDto(purchaseRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(PurchaseRecord resources) {
        PurchaseRecord record = purchaseRepository.findById(resources.getId()).orElseGet(PurchaseRecord::new);
        ValidationUtil.isNull(record.getId(), "purchase-record", "id", resources.getId());

        record.setProduct(resources.getProduct());
        record.setBatchNumber(resources.getBatchNumber());
        record.setDept(record.getDept());
        record.setGoodsName(record.getGoodsName());
        record.setPurchaseQuantity(resources.getPurchaseQuantity());
        record.setProducerName(resources.getProducerName());
        record.setProducerAddress(resources.getProducerAddress());
        record.setProducerContact(resources.getProducerContact());
        record.setRemark(resources.getRemark());
        record.setUnit(resources.getUnit());
        record.setProductName(resources.getProductName());
        record.setRecordStatus(resources.getRecordStatus());
        record.setPurchaseTime(resources.getPurchaseTime());

        purchaseRepository.save(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            purchaseRepository.deleteById(id);
        }
    }

    @Override
    public Object queryAll(PurchaseRecordQueryCriteria criteria, Pageable pageable) {
        Page<PurchaseRecord> page = purchaseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(purchaseMapper::toDto));
    }

    @Override
    public List<PurchaseRecordDto> queryAll(PurchaseRecordQueryCriteria criteria) {
        List<PurchaseRecord> records = purchaseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return purchaseMapper.toDto(records);
    }

    @Override
    public void download(List<PurchaseRecordDto> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PurchaseRecordDto purchaseDto : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("产品名称", purchaseDto.getProductName());
            map.put("产品代码", purchaseDto.getProduct().getCode());
            map.put("采购批次", purchaseDto.getBatchNumber());
            map.put("采购数量", purchaseDto.getPurchaseQuantity());
            map.put("计数单位", purchaseDto.getUnit());
            map.put("原材料名称", purchaseDto.getGoodsName());
            map.put("生产者名称", purchaseDto.getProducerName());
            map.put("生产者地址", purchaseDto.getProducerAddress());
            map.put("生产者联系方式", purchaseDto.getProducerContact());
            map.put("采购时间", purchaseDto.getPurchaseTime());
            map.put("所属组织机构", purchaseDto.getDept().getName());
            map.put("状态", purchaseDto.getRecordStatus() == 1 ? "已提交" : "草稿");
            map.put("备注", purchaseDto.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
