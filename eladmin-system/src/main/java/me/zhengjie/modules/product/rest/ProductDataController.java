package me.zhengjie.modules.product.rest;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.aop.log.Log;
import me.zhengjie.config.DataScope;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.product.domain.Category;
import me.zhengjie.modules.product.domain.ProductData;
import me.zhengjie.modules.product.service.ProductDataService;
import me.zhengjie.modules.product.service.dto.CategoryDto;
import me.zhengjie.modules.product.service.dto.CategoryQueryCriteria;
import me.zhengjie.modules.product.service.dto.ProductDataDto;
import me.zhengjie.modules.product.service.dto.ProductDataQueryCriteria;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Api(tags = "农产品：产品生产数据")
@RestController
@RequestMapping("/api/product-data")
public class ProductDataController {

    @Autowired
    private ProductDataService productDataService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private DataScope dataScope;

    @Log("导出产品生产数据")
    @ApiOperation("导出产品生产数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('product-data:list')")
    public void download(HttpServletResponse response, ProductDataQueryCriteria criteria) throws IOException {
        productDataService.download(productDataService.queryAll(criteria), response);
    }

    @Log("查询产品生产数据")
    @ApiOperation("查询产品生产数据")
    @GetMapping
    @PreAuthorize("@el.check('product-data:list')")
    public ResponseEntity<Object> getProductData(ProductDataQueryCriteria criteria){
        Set<Long> deptSet = new HashSet<>();
        Set<Long> result = new HashSet<>();
        if (!ObjectUtils.isEmpty(criteria.getDeptId())) {
            deptSet.add(criteria.getDeptId());
            deptSet.addAll(dataScope.getDeptChildren(deptService.findByPid(criteria.getDeptId())));
        }
        // 数据权限
        Set<Long> deptIds = dataScope.getDeptIds();
        // 查询条件不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(deptIds) && !CollectionUtils.isEmpty(deptSet)){
            // 取交集
            result.addAll(deptSet);
            result.retainAll(deptIds);
            // 若无交集，则代表无数据权限
            criteria.setDeptIds(result);
            if(result.size() != 0){
                return new ResponseEntity<>(PageUtil.toPage(null,0),HttpStatus.OK);
            } else {
                List<ProductDataDto> productDataDtoList = productDataService.queryAll(criteria);
                log.info(JSON.toJSONString(productDataDtoList));
                return new ResponseEntity<>(productDataService.buildTree(productDataDtoList),HttpStatus.OK);
            }
            // 否则取并集
        } else {
            result.addAll(deptSet);
            result.addAll(deptIds);
            criteria.setDeptIds(result);
            List<ProductDataDto> productDataDtoList = productDataService.queryAll(criteria);
            log.info(JSON.toJSONString(productDataDtoList));
            return new ResponseEntity<>(productDataService.buildTree(productDataDtoList),HttpStatus.OK);
        }
    }

    @Log("新增产品生产数据")
    @ApiOperation("新增产品生产数据")
    @PostMapping
    @PreAuthorize("@el.check('product-data:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ProductData resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ "product-data" +" cannot already have an ID");
        }
        return new ResponseEntity<>(productDataService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改产品生产数据")
    @ApiOperation("修改产品生产数据")
    @PutMapping
    @PreAuthorize("@el.check('product-data:edit')")
    public ResponseEntity<Object> update(@Validated(ProductData.Update.class) @RequestBody ProductData resources){
        productDataService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除产品生产数据")
    @ApiOperation("删除产品生产数据")
    @DeleteMapping
    @PreAuthorize("@el.check('product-data:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        Set<ProductData> categoriesSet = new HashSet<>();
        for (Long id : ids) {
            List<ProductData> productDataList = productDataService.findByPid(id);
            ProductData productData = productDataService.findOne(id);
            categoriesSet.add(productData);
            categoriesSet = productDataService.getDeleteProductData(productDataList, categoriesSet);
        }
        productDataService.delete(categoriesSet);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
