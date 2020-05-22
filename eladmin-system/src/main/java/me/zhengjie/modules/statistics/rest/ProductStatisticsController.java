package me.zhengjie.modules.statistics.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.statistics.service.IndustryStatisticsService;
import me.zhengjie.modules.statistics.service.ProductStatisticsService;
import me.zhengjie.modules.statistics.service.dto.IndustryStatisticsQueryCriteria;
import me.zhengjie.modules.statistics.service.dto.ProductStatisticsQueryCriteria;
import me.zhengjie.modules.statistics.service.dto.SalesStatisticsQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(tags = "首页：产品分布")
@Slf4j
@RestController
@RequestMapping("/api/product-statistics")
public class ProductStatisticsController {
    @Autowired
    private ProductStatisticsService productStatisticsService;

    @GetMapping("/info")
    @ApiOperation("查询")
    public ResponseEntity<Object> get(ProductStatisticsQueryCriteria criteria) {
        return new ResponseEntity<>(productStatisticsService.get(criteria), HttpStatus.OK);
    }

    @GetMapping(value = "/products")
    @ApiOperation("获取该地区某种种类的产品数据")
    public ResponseEntity<Object> getProductByCategory(ProductStatisticsQueryCriteria criteria) {
        return new ResponseEntity<>(productStatisticsService.getProductByCategory(criteria), HttpStatus.OK);
    }


    @GetMapping
    @Log("查询产品数据")
    @ApiOperation("查询产品数据")
    public ResponseEntity<Object> getProductData(ProductStatisticsQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(productStatisticsService.getProductData(criteria, pageable), HttpStatus.OK);
    }

    @Log("导出产品数据")
    @ApiOperation("导出产品数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('product-statistics:list')")
    public void download(HttpServletResponse response, ProductStatisticsQueryCriteria criteria) throws IOException {
        productStatisticsService.download(productStatisticsService.queryAll(criteria), response);
    }
}
