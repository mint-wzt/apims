package me.zhengjie.modules.statistics.repository;

import me.zhengjie.modules.statistics.domain.IndustryStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndustryStatisticsRepository extends JpaRepository<IndustryStatistics, Long>, JpaSpecificationExecutor<IndustryStatistics> {

    /**
     * 通过地区编码和统计项目查询
     * @param regionId
     * @param statisticsItem
     * @return
     */
    IndustryStatistics findByRegionIdAndStatisticsItem(String regionId,String statisticsItem);

//    /**
//     * 查询父级行政区域
//     * @param regionId
//     * @return
//     */
//    @Query("select i from IndustryStatistics i where regionId like i.regionId%")
//    List<IndustryStatistics> findByContainsRegionId(@Param("regionId") String regionId);

    @Query(value = "SELECT * FROM industry_statistics s WHERE s.region_name = ?1 AND (s.statistics_item = '种植面积' OR s.statistics_item = '养殖数量' OR s.statistics_item = '组织机构' OR s.statistics_item = '员工' OR s.statistics_item = '畜牧业' OR s.statistics_item = '种植业' OR s.statistics_item = '渔业')",nativeQuery = true)
    List<IndustryStatistics> findByStatistics(String regionName);
}
