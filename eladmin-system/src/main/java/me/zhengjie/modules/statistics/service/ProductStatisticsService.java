package me.zhengjie.modules.statistics.service;

import me.zhengjie.modules.statistics.domain.IndustryStatistics;
import me.zhengjie.modules.statistics.domain.ProductStatistics;
import me.zhengjie.modules.statistics.service.dto.IndustryStatisticsQueryCriteria;
import me.zhengjie.modules.statistics.service.dto.ProductStatisticsDto;
import me.zhengjie.modules.statistics.service.dto.ProductStatisticsQueryCriteria;

import java.util.List;

public interface ProductStatisticsService {

    /**
     * 查询
     * @param regionId
     * @param statisticsItem
     * @return
     */
    ProductStatistics findProductStatistics(String regionId,String productCode,String statisticsItem);

    /**
     * 新增
     * @param resources /
     * @return /
     */
    ProductStatistics create(ProductStatistics resources);


    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<ProductStatistics> queryAll(ProductStatisticsQueryCriteria criteria);

    /**
     * 查询某个地区的产品数据
     * @param criteria
     * @return
     */
    Object get(ProductStatisticsQueryCriteria criteria);

    Object getProductByCategory(ProductStatisticsQueryCriteria criteria);
}
