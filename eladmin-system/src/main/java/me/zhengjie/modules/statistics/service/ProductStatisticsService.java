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

//    /**
//     * 编辑
//     * @param resources /
//     */
//    void update(IndustryStatistics resources);
//
//    /**
//     * 删除
//     * @param ids /
//     */
//    void delete(Set<Long> ids);
//
//    /**
//     * 获取所有
//     * @param pageable
//     * @return
//     */
//    Object queryAll(Pageable pageable);

//    /**
//     * 查询全部
//     * @param criteria 条件
//     * @param pageable 分页参数
//     * @return /
//     */
//    Object queryAll(InspectionItemQueryCriteria criteria, Pageable pageable);

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

//    /**
//     * 导出数据
//     * @param queryAll 待导出的数据
//     * @param response /
//     * @throws IOException /
//     */
//    void download(List<IndustryStatistics> queryAll, HttpServletResponse response) throws IOException;
}
