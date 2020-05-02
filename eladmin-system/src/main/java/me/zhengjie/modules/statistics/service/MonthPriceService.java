package me.zhengjie.modules.statistics.service;

import me.zhengjie.modules.statistics.domain.MonthPrice;
import me.zhengjie.modules.statistics.domain.Price;
import me.zhengjie.modules.statistics.service.dto.MarketQueryCriteria;
import me.zhengjie.modules.statistics.service.dto.MonthPriceQueryCriteria;

public interface MonthPriceService {

    /**
     * 新增
     * @param resources /
     * @return /
     */
    MonthPrice create(MonthPrice resources);

    /**
     * 查询单一市场、单一产品的某年价格走势
     * @param criteria
     * @return
     */
    Object getMonthPriceOneToOneMarket(MonthPriceQueryCriteria criteria);

    /**
     * 查询多个市场、单一产品的某年价格走势
     * @param criteria
     * @return
     */
    Object getMonthPriceOneToMoreMarket(MonthPriceQueryCriteria criteria);

}
