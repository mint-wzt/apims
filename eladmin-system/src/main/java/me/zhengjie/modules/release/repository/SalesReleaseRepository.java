package me.zhengjie.modules.release.repository;

import me.zhengjie.modules.release.domain.SalesRelease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesReleaseRepository extends JpaRepository<SalesRelease, Long>, JpaSpecificationExecutor<SalesRelease> {
}
