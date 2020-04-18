package me.zhengjie.modules.product.repository;

import me.zhengjie.modules.product.domain.InspectionRecordTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionRecordTemplateRepository extends JpaRepository<InspectionRecordTemplate, Long>, JpaSpecificationExecutor<InspectionRecordTemplate> {

    /**
     * 删除检测记录对应的检测结果
     * @param id
     */
    void deleteByInspectionRecordId(Long id);
}
