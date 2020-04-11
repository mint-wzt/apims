package me.zhengjie.modules.product.repository;

import me.zhengjie.modules.product.domain.DescriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptionItemRepository extends JpaRepository<DescriptionItem, Long>, JpaSpecificationExecutor<DescriptionItem> {

    /**
     * 通过编码查询
     * @param code
     * @return
     */
    DescriptionItem findByCode(String code);

    /**
     * 通过中文名查询
     * @param chineseName
     * @return
     */
    DescriptionItem findByChineseName(String chineseName);
}
