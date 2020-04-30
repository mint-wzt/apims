package me.zhengjie.modules.statistics.repository;

import me.zhengjie.modules.statistics.domain.SalesStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesStatisticsRepository extends JpaRepository<SalesStatistics, Long>, JpaSpecificationExecutor<SalesStatistics> {

    // 查询该地区该产品该统计时间的记录是否存在
    SalesStatistics findByRegionIdAndProductNameAndStatisticsTime(String regionId,String productName,String statisticsTime);

    @Query(value = "SELECT * from sales_statistics s WHERE s.statistics_time = ?2 AND s.region_name = ?1 ORDER BY sales DESC LIMIT 10",nativeQuery = true)
    List<SalesStatistics> findByRegionNameAndStatisticsTimeOrderBySalesDesc(String regionName,String statisticsTime);

}
