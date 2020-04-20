package me.zhengjie.modules.product.repository;

import me.zhengjie.modules.product.domain.Product;
import me.zhengjie.modules.product.domain.ProductData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDataRepository extends JpaRepository<ProductData, Long>, JpaSpecificationExecutor<ProductData> {

    /**
     * 根据产品和状态查询
     * @param product
     * @param pid
     * @return
     */
    List<ProductData> findByProductAndPid(Product product, Long pid);

    List<ProductData> findByPid(Long pid);
}
