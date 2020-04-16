package me.zhengjie.modules.product.repository;

import me.zhengjie.modules.product.domain.InspectionTemplate;
import me.zhengjie.modules.system.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionTemplateRepository extends JpaRepository<InspectionTemplate, Long>, JpaSpecificationExecutor<InspectionTemplate> {

    InspectionTemplate findByNameAndUser(String name, User user);

}
