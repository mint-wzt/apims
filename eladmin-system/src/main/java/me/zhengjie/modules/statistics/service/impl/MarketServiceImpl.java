package me.zhengjie.modules.statistics.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.statistics.domain.Market;
import me.zhengjie.modules.statistics.repository.MarketRepository;
import me.zhengjie.modules.statistics.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@CacheConfig(cacheNames = "market")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MarketServiceImpl implements MarketService {

    @Autowired
    private MarketRepository marketRepository;

    @Override
    @CacheEvict
    public Market create(Market resources) {
        Market market = marketRepository.findByRegionNameAndMarketName(resources.getRegionName(),resources.getMarketName());
        if (market == null){
            return marketRepository.save(resources);
        }
        return null;
    }

    @Override
    public List<Market> findByRegionName(String regionName) {
        List<Market> markets = marketRepository.findByRegionName(regionName);
        return markets;
    }


}
