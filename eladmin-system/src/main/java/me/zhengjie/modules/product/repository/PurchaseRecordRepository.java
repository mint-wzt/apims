package me.zhengjie.modules.product.repository;

import me.zhengjie.modules.product.domain.PurchaseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRecordRepository extends JpaRepository<PurchaseRecord, Long>, JpaSpecificationExecutor<PurchaseRecord> {
}
