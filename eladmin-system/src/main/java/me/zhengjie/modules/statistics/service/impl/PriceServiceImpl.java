package me.zhengjie.modules.statistics.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.statistics.domain.Price;
import me.zhengjie.modules.statistics.repository.PriceRepository;
import me.zhengjie.modules.statistics.service.PriceService;
import me.zhengjie.modules.statistics.service.dto.PriceQueryCriteria;
import me.zhengjie.utils.DateUtils;
import me.zhengjie.utils.QueryHelp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
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
@CacheConfig(cacheNames = "price")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PriceServiceImpl implements PriceService {

    @Autowired
    private PriceRepository priceRepository;

    @Override
    @CacheEvict(allEntries = true)
    public Price create(Price resources) {
        Price price = priceRepository.findByMarketAndProductNameAndStatisticsTime(resources.getMarket(),resources.getProductName(),resources.getStatisticsTime());
        if (price == null){
            return priceRepository.save(resources);
        }
        return null;
    }

    @Override
    @Cacheable
//    @CacheEvict
    public Object getLatestPrice(PriceQueryCriteria criteria) {
        log.info(JSON.toJSONString(criteria));
        if (criteria.getStartTime() != null) {
            List<String> statisticsTime = new ArrayList<>(2);
            statisticsTime.add(DateUtils.getYearAndMonthByTimeStamp(criteria.getStartTime()));
            statisticsTime.add(DateUtils.getYearAndMonthByTimeStamp(criteria.getEndTime()));
            criteria.setStatisticsTime(statisticsTime);
        }
        log.info(JSON.toJSONString(criteria));
        List<Price> prices = priceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), new Sort(Sort.Direction.ASC, "statisticsTime"));
        Map<String,Object> map = new HashMap<>(3);
        map.put("date",prices.stream().map(Price::getStatisticsTime).collect(Collectors.toList()));
        map.put("price",prices.stream().map(Price::getPrice).collect(Collectors.toList()));
        map.put("priceUnit",prices.size() == 0 ? null : prices.get(0).getPriceUnit());
        return map;
    }
}
