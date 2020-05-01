package me.zhengjie.modules.statistics.repository;

import me.zhengjie.modules.statistics.domain.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketRepository extends JpaRepository<Market, Long>, JpaSpecificationExecutor<Market> {

    /**
     * 查询是否已存在市场
     * @param regionName
     * @param marketName
     * @return
     */
    Market findByRegionNameAndMarketName(String regionName,String marketName);

    /**
     * 通过地区名查询
     * @param regionName
     * @return
     */
    List<Market> findByRegionName(String regionName);
}
