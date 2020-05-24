package me.zhengjie.modules.release.repository;

import me.zhengjie.modules.release.domain.PurchaseRelease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseReleaseRepository extends JpaRepository<PurchaseRelease, Long>, JpaSpecificationExecutor<PurchaseRelease> {
}
