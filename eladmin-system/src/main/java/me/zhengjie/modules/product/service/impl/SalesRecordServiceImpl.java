package me.zhengjie.modules.product.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.product.domain.Product;
import me.zhengjie.modules.product.domain.SalesRecord;
import me.zhengjie.modules.product.repository.SalesRecordRepository;
import me.zhengjie.modules.product.service.ProductService;
import me.zhengjie.modules.product.service.SalesRecordService;
import me.zhengjie.modules.product.service.dto.ProductDto;
import me.zhengjie.modules.product.service.dto.SalesRecordDto;
import me.zhengjie.modules.product.service.dto.SalesRecordQueryCriteria;
import me.zhengjie.modules.product.service.mapper.SalesRecordMapper;
import me.zhengjie.modules.statistics.domain.Market;
import me.zhengjie.modules.statistics.domain.Price;
import me.zhengjie.modules.statistics.domain.ProductStatistics;
import me.zhengjie.modules.statistics.domain.SalesStatistics;
import me.zhengjie.modules.statistics.service.MarketService;
import me.zhengjie.modules.statistics.service.PriceService;
import me.zhengjie.modules.statistics.service.ProductStatisticsService;
import me.zhengjie.modules.statistics.service.SalesStatisticsService;
import me.zhengjie.modules.system.domain.Region;
import me.zhengjie.modules.system.repository.DeptRepository;
import me.zhengjie.modules.system.repository.RegionRepository;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.modules.system.service.dto.DeptDto;
import me.zhengjie.modules.system.service.dto.RegionDto;
import me.zhengjie.utils.*;
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
@CacheConfig(cacheNames = "sales-record")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SalesRecordServiceImpl implements SalesRecordService {

    @Autowired
    private SalesRecordRepository salesRecordRepository;

    @Autowired
    private SalesRecordMapper salesRecordMapper;

    @Autowired
    private DeptService deptService;

    @Autowired
    private SalesStatisticsService statisticsService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PriceService priceService;

    @Autowired
    private MarketService marketService;

    @Override
    public SalesRecordDto findById(long id) {
        SalesRecord salesRecord = salesRecordRepository.findById(id).orElseGet(SalesRecord::new);
        ValidationUtil.isNull(salesRecord.getId(), "SalesRecord", "id", id);
        return salesRecordMapper.toDto(salesRecord);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public SalesRecordDto create(SalesRecord resources) {
        SalesRecord salesRecord = salesRecordRepository.save(resources);
        DeptDto deptDto = deptService.findById(salesRecord.getDept().getId());
        log.info(JSON.toJSONString(salesRecord));

        // 销售数据统计
        SalesStatistics salesStatistics = new SalesStatistics();
        salesStatistics.setProductCode(resources.getProductCode());
        salesStatistics.setProductName(resources.getProductName());
        salesStatistics.setRegionId(deptDto.getRegion().getId());
        salesStatistics.setRegionName(deptDto.getRegion().getExtName());
        salesStatistics.setSaleNumber(resources.getSalesNumber());
        salesStatistics.setSaleUnit(resources.getSalesUnit());
        salesStatistics.setSales(resources.getSales()/10000);
        salesStatistics.setSalesUnit("万元");
        salesStatistics.setStatisticsTime(DateUtils.getYearAndMonthByTimeStamp(resources.getSalesDate()));
        statisticsService.create(salesStatistics);

        // 添加价格统计
        Price price = new Price();
        price.setProductName(resources.getProductName());
        price.setProductCode(resources.getProductCode());
        price.setProductId(resources.getProduct().getId());
        price.setProvinceName(resources.getRegionId());
        price.setMarket(resources.getRegionId() + resources.getSalesArea());
        price.setPrice(resources.getPrice());
        price.setPriceUnit(resources.getPriceUnit());
        price.setStatisticsTime(DateUtils.getYearAndMonthAndDayByTimeStamp(resources.getSalesDate()));
        price.setPublishTime(price.getStatisticsTime());
        ProductDto dto = productService.findById(resources.getProduct().getId());
        price.setCategoryName(dto.getCategory().getName());
        price.setCategoryCode(dto.getCategory().getCode());
        priceService.create(price);

        // 保存市场记录
        Market market = new Market();
        market.setRegionName(resources.getRegionId());
        market.setMarketName(resources.getRegionId() + resources.getSalesArea());
        market.setRegionId(deptDto.getRegion().getId());
        marketService.create(market);

        return salesRecordMapper.toDto(salesRecord);
    }


    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(SalesRecord resources) {
        SalesRecord record = salesRecordRepository.findById(resources.getId()).orElseGet(SalesRecord::new);
        ValidationUtil.isNull(record.getId(),"SalesRecord","id",resources.getId());

        record.setBatchNumber(resources.getBatchNumber());
        record.setDept(resources.getDept());
        record.setOrderNumber(resources.getOrderNumber());
        record.setPrice(resources.getPrice());
        record.setPriceUnit(resources.getPriceUnit());
        record.setSalesDate(resources.getSalesDate());
        record.setSales(resources.getSales());
        record.setSalesNumber(resources.getSalesNumber());
        record.setSalesUnit(resources.getSalesUnit());
        record.setSalesStatus(resources.getSalesStatus());
        record.setDeptName(resources.getDeptName());
        record.setProduct(resources.getProduct());
        record.setProductCode(resources.getProductCode());
        record.setProductName(resources.getProductName());
        record.setRemark(resources.getRemark());
        record.setRegionId(resources.getRegionId());
        record.setSalesArea(resources.getSalesArea());

        salesRecordRepository.save(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            salesRecordRepository.deleteById(id);
        }
    }

    @Override
    @Cacheable
    public Object queryAll(SalesRecordQueryCriteria criteria, Pageable pageable) {
        Page<SalesRecord> page = salesRecordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(salesRecordMapper::toDto));
    }

    @Override
    @Cacheable
    public List<SalesRecordDto> queryAll(SalesRecordQueryCriteria criteria) {
        List<SalesRecord> records = salesRecordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return salesRecordMapper.toDto(records);
    }

    @Override
    public void download(List<SalesRecordDto> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SalesRecordDto recordDto : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("订单号", recordDto.getOrderNumber());
            map.put("产品名称", recordDto.getProduct().getName());
            map.put("产品编码", recordDto.getProduct().getCode());
            map.put("销售区域", recordDto.getSalesArea());
            map.put("生产批次", recordDto.getBatchNumber());
            map.put("销量", recordDto.getSalesNumber() + recordDto.getSalesUnit());
            map.put("单价", recordDto.getPrice() + recordDto.getPriceUnit());
            map.put("销售额", recordDto.getSales());
            map.put("销售日期", recordDto.getSalesDate());
            map.put("组织机构", recordDto.getDept().getName());
            map.put("状态", recordDto.getSalesStatus());
            map.put("备注", recordDto.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
