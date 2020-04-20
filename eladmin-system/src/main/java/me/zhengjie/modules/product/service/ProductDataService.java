package me.zhengjie.modules.product.service;

import me.zhengjie.modules.product.domain.Category;
import me.zhengjie.modules.product.domain.Product;
import me.zhengjie.modules.product.domain.ProductData;
import me.zhengjie.modules.product.service.dto.*;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProductDataService {
    /**
     * 根据ID查询
     * @param id ID
     * @return /
     */
    ProductDataDto findById(long id);

    /**
     * 查询一个
     * @param id
     * @return
     */
    ProductData findOne(Long id);

    /**
     * 查询子类
     * @param pid
     * @return
     */
    List<ProductData> findByPid(Long pid);

    /**
     * 新增生产数据
     * @param resources /
     * @return /
     */
    ProductDataDto create(ProductData resources);

    /**
     * 编辑生产数据
     * @param resources /
     */
    void update(ProductData resources);

    /**
     * 删除生产数据
     * @param ids /
     */
    void delete(Set<ProductData> categoriesSet);

    /**
     * 构建树
     * @param productDataDtos
     * @return
     */
    Map<String, Object> buildTree(List<ProductDataDto> productDataDtos);

    /**
     * 获取待删除列表
     * @param productDataList
     * @param productDataSet
     * @return
     */
    Set<ProductData> getDeleteProductData(List<ProductData> productDataList, Set<ProductData> productDataSet);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<ProductDataDto> queryAll(ProductDataQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<ProductDataDto> queryAll, HttpServletResponse response) throws IOException;
}
