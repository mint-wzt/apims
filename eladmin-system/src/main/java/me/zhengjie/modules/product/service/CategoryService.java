package me.zhengjie.modules.product.service;

import me.zhengjie.modules.product.domain.Category;
import me.zhengjie.modules.product.service.dto.CategoryDto;
import me.zhengjie.modules.product.service.dto.CategoryQueryCriteria;
import me.zhengjie.modules.system.domain.Menu;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CategoryService {
    /**
     * 查询全部数据
     * @param criteria 条件
     * @return /
     */
    List<CategoryDto> queryAll(CategoryQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    CategoryDto findById(long id);

    /**
     * 创建
     * @param resources /
     * @return /
     */
    CategoryDto create(Category resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(Category resources);

    /**
     * 获取待删除的分类
     * @param categoryList /
     * @param categorySet /
     * @return /
     */
    Set<Category> getDeleteCategories(List<Category> categoryList, Set<Category> categorySet);

    /**
     * 根据pid查询
     * @param pid /
     * @return /
     */
    List<Category> findByPid(long pid);

    /**
     * 构建分类树
     * @param categoryDtos 原始数据
     * @return /
     */
    Map<String,Object> buildTree(List<CategoryDto> categoryDtos);


    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    Category findOne(Long id);

    /**
     * 批量删除
     * @param categorySet /
     */
    void delete(Set<Category> categorySet);

    /**
     * 导出
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<CategoryDto> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 获取分类树
     * @param categories
     * @return
     */
    public Object getCategoryTree(List<Category> categories);

    /**
     * 设置父类
     * @param categoryDtos
     */
    public void setParentCategory(List<CategoryDto> categoryDtos);
}
