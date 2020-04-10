package me.zhengjie.modules.product.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.product.domain.Category;
import me.zhengjie.modules.product.service.CategoryService;
import me.zhengjie.modules.product.service.dto.CategoryDto;
import me.zhengjie.modules.product.service.dto.CategoryQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Api(tags = "农产品：分类管理")
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    private static final String ENTITY_NAME = "category";

    @Log("导出分类数据")
    @ApiOperation("导出分类数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('category:list')")
    public void download(HttpServletResponse response, CategoryQueryCriteria criteria) throws IOException {
        categoryService.download(categoryService.queryAll(criteria), response);
    }

    @ApiOperation("返回全部的分类")
    @GetMapping(value = "/tree")
    @PreAuthorize("@el.check('category:list')")
    public ResponseEntity<Object> getCategoryTree(){
        return new ResponseEntity<>(categoryService.getCategoryTree(categoryService.findByPid(0L)),HttpStatus.OK);
    }

    @Log("查询分类")
    @ApiOperation("查询分类")
    @GetMapping
    @PreAuthorize("@el.check('category:list')")
    public ResponseEntity<Object> getCategories(CategoryQueryCriteria criteria){
        List<CategoryDto> categoryDtoList = categoryService.queryAll(criteria);
//        categoryService.setParentCategory(categoryDtoList);
        return new ResponseEntity<>(categoryService.buildTree(categoryDtoList),HttpStatus.OK);
    }

    @Log("新增分类")
    @ApiOperation("新增分类")
    @PostMapping
    @PreAuthorize("@el.check('category:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Category resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        return new ResponseEntity<>(categoryService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改分类")
    @ApiOperation("修改分类")
    @PutMapping
    @PreAuthorize("@el.check('category:edit')")
    public ResponseEntity<Object> update(@Validated(Category.Update.class) @RequestBody Category resources){
        categoryService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除分类")
    @ApiOperation("删除分类")
    @DeleteMapping
    @PreAuthorize("@el.check('category:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        Set<Category> categoriesSet = new HashSet<>();
        for (Long id : ids) {
            List<Category> categoriesList = categoryService.findByPid(id);
            Category category = categoryService.findOne(id);
            categoriesSet.add(category);
            categoriesSet = categoryService.getDeleteCategories(categoriesList, categoriesSet);
        }
        categoryService.delete(categoriesSet);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
