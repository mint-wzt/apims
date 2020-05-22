package me.zhengjie.modules.statistics.service;

import me.zhengjie.modules.statistics.domain.IndustryStatistics;
import me.zhengjie.modules.statistics.domain.ProductStatistics;
import me.zhengjie.modules.statistics.domain.SalesStatistics;
import me.zhengjie.modules.statistics.service.dto.IndustryStatisticsQueryCriteria;
import me.zhengjie.modules.statistics.service.dto.ProductStatisticsDto;
import me.zhengjie.modules.statistics.service.dto.ProductStatisticsQueryCriteria;
import me.zhengjie.modules.statistics.service.dto.SalesStatisticsQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ProductStatisticsService {

    /**
     * 查询
     *
     * @param regionId
     * @param statisticsItem
     * @return
     */
    ProductStatistics findProductStatistics(String regionId, String productCode, String statisticsItem);

    /**
     * 新增
     *
     * @param resources /
     * @return /
     */
    ProductStatistics create(ProductStatistics resources);


    /**
     * 查询全部不分页
     *
     * @param criteria 条件
     * @return /
     */
    List<ProductStatistics> queryAll(ProductStatisticsQueryCriteria criteria);

    /**
     * 查询某个地区的产品数据
     *
     * @param criteria
     * @return
     */
    Object get(ProductStatisticsQueryCriteria criteria);

    Object getProductByCategory(ProductStatisticsQueryCriteria criteria);

    Object getProductData(ProductStatisticsQueryCriteria criteria, Pageable pageable);

    /**
     * 导出数据
     * @param queryAll
     * @param response
     * @throws IOException
     */
    void download(List<ProductStatistics> queryAll, HttpServletResponse response) throws IOException;
}
