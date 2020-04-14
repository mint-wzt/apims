package me.zhengjie.modules.product.repository;

import me.zhengjie.modules.product.domain.InspectionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionItemRepository extends JpaRepository<InspectionItem, Long>, JpaSpecificationExecutor<InspectionItem> {

    InspectionItem findByCode(String code);

}
