package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, String>, JpaSpecificationExecutor<Region> {

    /**
     * 查询父级行政区域
     * @param id
     * @return
     */
    @Query(value = "SELECT * FROM region r WHERE INSTR(?1,r.id) = 1",nativeQuery = true)
    List<Region> findParents(String id);
}
