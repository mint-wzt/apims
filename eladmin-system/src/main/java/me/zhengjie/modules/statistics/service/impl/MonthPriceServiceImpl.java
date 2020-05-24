package me.zhengjie.modules.statistics.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.statistics.domain.MonthPrice;
import me.zhengjie.modules.statistics.repository.MonthPriceRepository;
import me.zhengjie.modules.statistics.service.MonthPriceService;
import me.zhengjie.modules.statistics.service.dto.MarketQueryCriteria;
import me.zhengjie.modules.statistics.service.dto.MonthPriceQueryCriteria;
import me.zhengjie.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@CacheConfig(cacheNames = "month-price")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MonthPriceServiceImpl implements MonthPriceService {

    @Autowired
    private MonthPriceRepository monthPriceRepository;

    @Override
    @CacheEvict
    public MonthPrice create(MonthPrice resources) {
        return monthPriceRepository.save(resources);
    }

    @Override
    @Cacheable
    public Object getMonthPriceOneToOneMarket(MonthPriceQueryCriteria criteria) {
        List<MonthPrice> monthPrices1 = new ArrayList<>();
        List<MonthPrice> monthPrices2 = new ArrayList<>();
        if (criteria.getStartTime() != null) {
            criteria.setStatisticsYear(DateUtils.getYearByTimeStamp(criteria.getStartTime()));
            monthPrices1 = monthPriceRepository.findByMarketAndProductNameAndStatisticsYearOrderByStatisticsMonthAsc(criteria.getMarket(),criteria.getProductName(),criteria.getStatisticsYear());
        }
        if (criteria.getEndTime() != null) {
            criteria.setStatisticsYear(DateUtils.getYearByTimeStamp(criteria.getEndTime()));
            monthPrices2 = monthPriceRepository.findByMarketAndProductNameAndStatisticsYearOrderByStatisticsMonthAsc(criteria.getMarket(),criteria.getProductName(),criteria.getStatisticsYear());
        }
        Map<String,Object> map = new HashMap<>();
        map.put("startYearData",monthPrices1.stream().map(MonthPrice::getPrice).collect(Collectors.toList()));
        map.put("endYearData",monthPrices2.stream().map(MonthPrice::getPrice).collect(Collectors.toList()));
        String priceUnit = null;
        if (monthPrices1.size() != 0){
            priceUnit = monthPrices1.get(0).getPriceUnit();
        }else if (monthPrices2.size() != 0){
            priceUnit = monthPrices2.get(0).getPriceUnit();
        }
        map.put("priceUnit",priceUnit);
        return map;
    }

    @Override
    @Cacheable
    public Object getMonthPriceOneToMoreMarket(MonthPriceQueryCriteria criteria){
        List<MonthPrice> startMonthPrices = monthPriceRepository.findByMarketAndProductNameAndStatisticsYearOrderByStatisticsMonthAsc(criteria.getStartMarket(),criteria.getProductName(),criteria.getStatisticsYear());
        List<MonthPrice> endMonthPrices = monthPriceRepository.findByMarketAndProductNameAndStatisticsYearOrderByStatisticsMonthAsc(criteria.getEndMarket(),criteria.getProductName(),criteria.getStatisticsYear());
        Map<String,Object> map = new HashMap<>();
        map.put("startMarketData",startMonthPrices.stream().map(MonthPrice::getPrice).collect(Collectors.toList()));
        map.put("endMarketData",endMonthPrices.stream().map(MonthPrice::getPrice).collect(Collectors.toList()));
        String priceUnit = null;
        if (startMonthPrices.size() != 0){
            priceUnit = startMonthPrices.get(0).getPriceUnit();
        }else if (endMonthPrices.size() != 0){
            priceUnit = endMonthPrices.get(0).getPriceUnit();
        }
        map.put("priceUnit",priceUnit);
        return map;
    }
}
