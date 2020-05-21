package me.zhengjie.modules.statistics.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.statistics.domain.ProductStatistics;
import me.zhengjie.modules.statistics.domain.SalesStatistics;
import me.zhengjie.modules.statistics.repository.SalesStatisticsRepository;
import me.zhengjie.modules.statistics.service.SalesStatisticsService;
import me.zhengjie.modules.statistics.service.dto.SalesStatisticsQueryCriteria;
import me.zhengjie.modules.system.domain.Region;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.RegionService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.RoleSmallDto;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.modules.system.service.dto.UserQueryCriteria;
import me.zhengjie.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@CacheConfig(cacheNames = "sales-statistics")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SalesStatisticsServiceImpl implements SalesStatisticsService {

    @Autowired
    private RegionService regionService;

    @Autowired
    private SalesStatisticsRepository repository;

    @Override
    @CacheEvict(allEntries = true)
    public SalesStatistics create(SalesStatistics resources) {
        addToParent(resources);
        return resources;
    }

    public void add(SalesStatistics resources) {
        SalesStatistics statistics = repository.findByRegionIdAndProductNameAndStatisticsTime(resources.getRegionId(), resources.getProductName(), resources.getStatisticsTime());

        log.info(JSON.toJSONString(resources));
        log.info(JSON.toJSONString(statistics));
        if (statistics != null) { // 添加销量、销售额、产量
            if (resources.getOutput() == null) { // 添加销量、销售额
                statistics.setSaleNumber(DoubleUtils.parseDouble(resources.getSaleNumber()) + DoubleUtils.parseDouble(statistics.getSaleNumber())); // 销量
                statistics.setSales(DoubleUtils.parseDouble(resources.getSales()) + DoubleUtils.parseDouble(statistics.getSales())); // 销售额
                statistics.setSaleUnit(resources.getSaleUnit());
                statistics.setSalesUnit(resources.getSalesUnit());
            } else { // 产量
                statistics.setOutput(DoubleUtils.parseDouble(resources.getOutput()) + DoubleUtils.parseDouble(statistics.getOutput())); // 产量
                statistics.setOutputUnit(resources.getOutputUnit());
            }
            repository.save(statistics);
        } else {
            repository.save(resources);
        }
    }

    // 对行政区域的上级添加项目的数据
    public void addToParent(SalesStatistics resources) {
        List<Region> regions = regionService.findParents(resources.getRegionId());
        for (Region region : regions) {
            SalesStatistics statistics = new SalesStatistics();
            statistics.setRegionId(region.getId());
            statistics.setRegionName(region.getExtName());
            statistics.setProductName(resources.getProductName());
            statistics.setProductCode(resources.getProductCode());
            statistics.setOutput(resources.getOutput());
            statistics.setOutputUnit(resources.getOutputUnit());
            statistics.setSaleNumber(resources.getSaleNumber());
            statistics.setSaleUnit(resources.getSaleUnit());
            statistics.setSales(resources.getSales());
            statistics.setSalesUnit(resources.getSalesUnit());
            statistics.setStatisticsTime(resources.getStatisticsTime());
            add(statistics);
        }
    }

    @Override
    @Cacheable
    public Object get(SalesStatisticsQueryCriteria criteria) {
        log.info(JSON.toJSONString(criteria));
        if (criteria.getTimestamps1() != null) {
            List<String> statisticsTime = new ArrayList<>(2);
            statisticsTime.add(DateUtils.getYearAndMonthByTimeStamp(criteria.getTimestamps1()));
            statisticsTime.add(DateUtils.getYearAndMonthByTimeStamp(criteria.getTimestamps2()));
            criteria.setStatisticsTime(statisticsTime);
        }
        log.info(JSON.toJSONString(criteria));
        List<SalesStatistics> statistics = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), new Sort(Sort.Direction.ASC, "statisticsTime"));
        log.info(JSON.toJSONString(statistics));
        Map<String, Object> map = new HashMap<>();
        map.put("date", statistics.stream().map(SalesStatistics::getStatisticsTime).collect(Collectors.toList()));
        map.put("output", statistics.stream().map(SalesStatistics::getOutput).collect(Collectors.toList()));
        map.put("saleNumber", statistics.stream().map(SalesStatistics::getSaleNumber).collect(Collectors.toList()));
        map.put("sales", statistics.stream().map(SalesStatistics::getSales).collect(Collectors.toList()));
        map.put("outputUnit", statistics.size() == 0 ? null : statistics.get(0).getOutputUnit());
        map.put("salesUnit", statistics.size() == 0 ? null : statistics.get(0).getSalesUnit());
        return map;
    }

    @Override
    @Cacheable
    public Object getProductSalesRank(SalesStatisticsQueryCriteria criteria) {
        log.info(JSON.toJSONString(criteria));
        if (criteria.getTimestamps1() != null) {
            criteria.setBlurry(DateUtils.getYearAndMonthByTimeStamp(criteria.getTimestamps1()));
        }
        log.info(JSON.toJSONString(criteria));
        List<SalesStatistics> statistics = repository.findByRegionNameAndStatisticsTimeOrderBySalesDesc(criteria.getRegionName(),criteria.getBlurry());
        log.info(JSON.toJSONString(statistics));
        Map<String,Object> map = new HashMap<>();
        map.put("products",statistics.stream().map(SalesStatistics::getProductName).collect(Collectors.toList()));
        map.put("sales",statistics.stream().map(SalesStatistics::getSales).collect(Collectors.toList()));
        return map;
    }

    @Override
    @Cacheable
    public Object getSalesData(SalesStatisticsQueryCriteria criteria, Pageable pageable) {
        if (criteria.getStatisticsTimes() != null && criteria.getStatisticsTimes().size() != 0){
            List<String> times = new ArrayList<>(2);
            times.add(DateUtils.getYearAndMonthByTimeStamp(criteria.getStatisticsTimes().get(0)));
            times.add(DateUtils.getYearAndMonthByTimeStamp(criteria.getStatisticsTimes().get(1)));
            criteria.setStatisticsTime(times);
        }
        Page<SalesStatistics> statistics = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder),pageable);
        log.info(JSON.toJSONString(statistics));
        return PageUtil.toPage(statistics);
    }

    @Override
    @Cacheable
    public List<SalesStatistics> queryAll(SalesStatisticsQueryCriteria criteria) {
        List<SalesStatistics> salesStatistics = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return salesStatistics;
    }

    @Override
    public void download(List<SalesStatistics> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SalesStatistics statistics : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("地区", statistics.getRegionName());
            map.put("邮编", statistics.getRegionId());
            map.put("产品名称", statistics.getProductName());
            map.put("产品编码", statistics.getProductCode());
            map.put("产量", statistics.getOutput() + statistics.getOutputUnit());
            map.put("销量", statistics.getSaleNumber() + statistics.getSaleUnit());
            map.put("销售额", statistics.getSales() + statistics.getSalesUnit());
            map.put("统计日期", statistics.getStatisticsTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


}
