package me.zhengjie.modules.statistics.service;

import me.zhengjie.modules.statistics.domain.Market;

import java.util.List;

public interface MarketService {
    /**
     * 新增
     * @param resources /
     * @return /
     */
    Market create(Market resources);

    /**
     * 查询某个地区的市场
     * @param regionName
     * @return
     */
    List<Market> findByRegionName(String regionName);

}
