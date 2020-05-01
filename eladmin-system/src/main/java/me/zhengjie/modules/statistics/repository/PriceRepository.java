package me.zhengjie.modules.statistics.repository;

import me.zhengjie.modules.statistics.domain.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long>, JpaSpecificationExecutor<Price> {

    Price findByMarketAndProductNameAndStatisticsTime(String market,String productName,String statisticsTime);
}
