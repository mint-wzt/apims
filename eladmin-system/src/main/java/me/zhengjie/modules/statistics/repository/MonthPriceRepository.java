package me.zhengjie.modules.statistics.repository;

import me.zhengjie.modules.statistics.domain.MonthPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthPriceRepository extends JpaRepository<MonthPrice, Long>, JpaSpecificationExecutor<MonthPrice> {

    /**
     * 查询单一市场、单一产品的某年价格走势
     * @param market
     * @param productName
     * @param statisticsYear
     * @return
     */
    List<MonthPrice> findByMarketAndProductNameAndStatisticsYearOrderByStatisticsMonthAsc(String market,String productName,String statisticsYear);

}
