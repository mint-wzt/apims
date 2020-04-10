package me.zhengjie.modules.product.repository;

import me.zhengjie.modules.product.domain.Category;
import me.zhengjie.modules.system.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    /**
     * 通过分类名称查询
     * @param name
     * @return
     */
    List<Category> findByNameOrCode(String name,String code);

    /**
     * 根据分类的 PID 查询
     * @param pid /
     * @return /
     */
    List<Category> findByPid(long pid);

    /**
     * 通过分类名称查找
     * @param name
     * @return
     */
    Category findByName(String name);

    /**
     * 通过分类代码查找
     * @param code
     * @return
     */
    Category findByCode(String code);

}
