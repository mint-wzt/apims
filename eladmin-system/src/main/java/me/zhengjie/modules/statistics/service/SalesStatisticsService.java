package me.zhengjie.modules.statistics.service;

import me.zhengjie.modules.statistics.domain.ProductStatistics;
import me.zhengjie.modules.statistics.domain.SalesStatistics;
import me.zhengjie.modules.statistics.service.dto.SalesStatisticsQueryCriteria;
import me.zhengjie.modules.system.service.dto.UserDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    /**
     * 获取产品销售详情信息
     * @param criteria
     * @return
     */
    Object getSalesData(SalesStatisticsQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有
     * @param criteria
     * @return
     */
    List<SalesStatistics> queryAll(SalesStatisticsQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll
     * @param response
     * @throws IOException
     */
    void download(List<SalesStatistics> queryAll, HttpServletResponse response) throws IOException;

}
