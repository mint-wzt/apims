package me.zhengjie.modules.statistics.service;

import me.zhengjie.modules.statistics.domain.Price;
import me.zhengjie.modules.statistics.service.dto.PriceQueryCriteria;


public interface PriceService {

    /**
     * 新增
     * @param resources /
     * @return /
     */
    Price create(Price resources);

    /**
     * 获取产品最新价格
     * @param criteria
     * @return
     */
    Object getLatestPrice(PriceQueryCriteria criteria);
}
