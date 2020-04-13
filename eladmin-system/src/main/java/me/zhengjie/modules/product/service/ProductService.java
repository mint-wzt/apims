package me.zhengjie.modules.product.service;

import me.zhengjie.modules.product.domain.Product;
import me.zhengjie.modules.product.service.dto.ProductDto;
import me.zhengjie.modules.product.service.dto.ProductQueryCriteria;
import me.zhengjie.modules.system.service.dto.UserDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ProductService {
    /**
     * 根据ID查询
     * @param id ID
     * @return /
     */
    ProductDto findById(long id);

    /**
     * 新增产品
     * @param resources /
     * @return /
     */
    ProductDto create(Product resources);

    /**
     * 编辑产品
     * @param resources /
     */
    void update(Product resources);

    /**
     * 删除产品
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 查询全部
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    Object queryAll(ProductQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<ProductDto> queryAll(ProductQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<ProductDto> queryAll, HttpServletResponse response) throws IOException;

}
