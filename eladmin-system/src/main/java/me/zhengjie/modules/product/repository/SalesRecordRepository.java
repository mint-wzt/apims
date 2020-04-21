package me.zhengjie.modules.product.repository;

import me.zhengjie.modules.product.domain.SalesRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRecordRepository extends JpaRepository<SalesRecord, Long>, JpaSpecificationExecutor<SalesRecord> {
}
