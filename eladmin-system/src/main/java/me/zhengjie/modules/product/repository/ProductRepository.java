package me.zhengjie.modules.product.repository;


import me.zhengjie.modules.product.domain.Product;
import me.zhengjie.modules.system.domain.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Product findByCodeAndDept(String code, Dept dept);
}
