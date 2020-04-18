package me.zhengjie.modules.product.repository;

import me.zhengjie.modules.product.domain.InspectionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionRecordRepository extends JpaRepository<InspectionRecord, Long>, JpaSpecificationExecutor<InspectionRecord> {

    /**
     * 通过code查询
     * @param code
     * @return
     */
    InspectionRecord findByCode(String code);
}
