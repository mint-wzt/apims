package me.zhengjie.modules.statistics.service;

import me.zhengjie.modules.statistics.domain.ProductStatistics;
import me.zhengjie.modules.statistics.domain.SalesStatistics;
import me.zhengjie.modules.statistics.service.dto.SalesStatisticsQueryCriteria;

import java.util.List;

public interface SalesStatisticsService {

    /**
     * 新增
     * @param resources /
     * @return /
     */
    SalesStatistics create(SalesStatistics resources);

    /**
     * 获取产品的产量、销量、销售额
     * @param criteria
     * @return
     */
    Object get(SalesStatisticsQueryCriteria criteria);

    /**
     * 获取某个地区某月的产品销售额排行
     * @param criteria
     * @return
     */
    Object getProductSalesRank(SalesStatisticsQueryCriteria criteria);

}
