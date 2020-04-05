package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, String>, JpaSpecificationExecutor<Region> {
}
