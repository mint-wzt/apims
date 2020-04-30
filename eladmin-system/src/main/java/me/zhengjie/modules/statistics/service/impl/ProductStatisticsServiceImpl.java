package me.zhengjie.modules.statistics.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.statistics.domain.IndustryStatistics;
import me.zhengjie.modules.statistics.domain.ProductStatistics;
import me.zhengjie.modules.statistics.repository.ProductStatisticsRepository;
import me.zhengjie.modules.statistics.service.ProductStatisticsService;
import me.zhengjie.modules.statistics.service.dto.ProductStatisticsDto;
import me.zhengjie.modules.statistics.service.dto.ProductStatisticsQueryCriteria;
import me.zhengjie.modules.system.domain.Region;
import me.zhengjie.modules.system.service.RegionService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@CacheConfig(cacheNames = "product-statistics")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProductStatisticsServiceImpl implements ProductStatisticsService {

    @Autowired
    private ProductStatisticsRepository repository;

    @Autowired
    private RegionService regionService;

    @Autowired
    private UserService userService;

    @Override
    public ProductStatistics findProductStatistics(String regionId, String productName,String statisticsItem) {
        return repository.findByRegionIdAndProductNameAndStatisticsItem(regionId, productName,statisticsItem);
    }

    @Override
    public ProductStatistics create(ProductStatistics resources) {

        addToParent(resources);
        // 上级统计
        return resources;
    }

    public ProductStatistics add(ProductStatistics resources){
        // 添加的是农产品的分类
        ProductStatistics productStatistics;
        if (resources.getProductName() == null){
            productStatistics = repository.findByRegionIdAndStatisticsItem(resources.getRegionId(),resources.getStatisticsItem());
        }else { // 添加的是农产品的产量/销量/具体分类具体产品的种数等信息
            productStatistics = findProductStatistics(resources.getRegionId(), resources.getProductName(),resources.getStatisticsItem());
        }

        ProductStatistics statistics;
        if (productStatistics == null) {
            statistics = repository.save(resources);
        } else {
            productStatistics.setStatisticsTotal(productStatistics.getStatisticsTotal() + resources.getStatisticsTotal());
            statistics = repository.save(productStatistics);
        }
        return statistics;
    }

    // 对行政区域的上级添加项目的数据
    public void addToParent(ProductStatistics industryStatistics) {
        List<Region> regions = regionService.findParents(industryStatistics.getRegionId());
        for (Region region : regions){
            ProductStatistics statistics = new ProductStatistics();
            statistics.setRegionId(region.getId());
            statistics.setRegionName(region.getExtName());
            statistics.setProductName(industryStatistics.getProductName());
            statistics.setProductCode(industryStatistics.getProductCode());
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
    public List<ProductStatistics> queryAll(ProductStatisticsQueryCriteria criteria) {
        List<ProductStatistics> records = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return records;
    }

    @Override
//    @Cacheable
    @CacheEvict
    public Object get(ProductStatisticsQueryCriteria criteria) {
        if (criteria.getRegionName() == null){
            UserDto user = userService.findByName(SecurityUtils.getUsername());
            criteria.setRegionName(user.getRegion().getExtName());
        }
        Map<String,Object> map = new HashMap<>(5);
        List<ProductStatistics> productStatistics = repository.findStatistics(criteria.getRegionName());
        map.put("category",productStatistics.stream().map(ProductStatistics::getStatisticsItem).collect(Collectors.toList()));
        map.put("statisticsData",productStatistics.stream().map(ProductStatistics::getStatisticsTotal).collect(Collectors.toList()));
        log.info(JSON.toJSONString(map));
        return map;
    }

    @Override
//    @Cacheable
    @CacheEvict
    public Object getProductByCategory(ProductStatisticsQueryCriteria criteria) {
        log.info(JSON.toJSONString(criteria));
        Map<String,Object> map = new HashMap<>();
        List<ProductStatistics> statistics = repository.findWithRegionNameAndStatisticItem(criteria.getRegionName(),criteria.getCategory());
        log.info(JSON.toJSONString(statistics));
        map.put("products",statistics.stream().map(ProductStatistics::getProductName).collect(Collectors.toList()));
        map.put("enterprises",statistics.stream().map(ProductStatistics::getStatisticsTotal).collect(Collectors.toList()));
        return map;
    }

}
