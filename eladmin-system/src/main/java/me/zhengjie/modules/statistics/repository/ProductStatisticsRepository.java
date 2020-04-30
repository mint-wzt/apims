package me.zhengjie.modules.statistics.repository;

import me.zhengjie.modules.statistics.domain.ProductStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductStatisticsRepository extends JpaRepository<ProductStatistics, Long>, JpaSpecificationExecutor<ProductStatistics> {
    /**
     * 通过地区、产品名称、统计项查询
     * @param regionId
     * @param productName
     * @param statisticsItem
     * @return
     */
    ProductStatistics findByRegionIdAndProductNameAndStatisticsItem(String regionId,String productName,String statisticsItem);

    /**
     * 通过地区和产品名称
     * @param regionId
     * @param statisticsItem
     * @return
     */
    ProductStatistics  findByRegionIdAndStatisticsItem(String regionId,String statisticsItem);

    @Query(value = "SELECT * FROM product_statistics s WHERE s.region_name = ?1 AND (ISNULL(s.product_name) OR LENGTH(TRIM(s.product_name))=0) AND (s.statistics_item = '粮油' OR s.statistics_item = '水产品' OR s.statistics_item = '果品' OR s.statistics_item = '蔬菜' OR s.statistics_item = '畜产品')",nativeQuery = true)
    List<ProductStatistics> findStatistics(String regionName);

    @Query(value = "SELECT * FROM product_statistics s WHERE s.region_id = ?1 AND (s.statistics_item = '粮油' OR s.statistics_item = '水产品' OR s.statistics_item = '果品' OR s.statistics_item = '蔬菜' OR s.statistics_item = '畜产品')",nativeQuery = true)
    List<ProductStatistics> findSaleStatistics(String regionId);

    @Query(value = "SELECT * from product_statistics s WHERE s.region_name =?1 AND s.statistics_item = ?2 AND (ISNULL(s.product_name) = 0 AND LENGTH(TRIM(s.product_name))>0) ORDER BY s.statistics_total DESC LIMIT 10",nativeQuery = true)
    List<ProductStatistics> findWithRegionNameAndStatisticItem(String regionName,String statisticItem);
}
