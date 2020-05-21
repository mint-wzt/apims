package me.zhengjie.modules.statistics.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.statistics.service.SalesStatisticsService;
import me.zhengjie.modules.statistics.service.dto.SalesStatisticsQueryCriteria;
import me.zhengjie.modules.system.service.dto.UserQueryCriteria;
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

@Api(tags = "首页：产品销量数据")
@Slf4j
@RestController
@RequestMapping("/api/sales-statistics")
public class SalesStatisticsController {

    @Autowired
    private SalesStatisticsService salesStatisticsService;

    @GetMapping("/sales")
    @ApiOperation("查询产量、销量和销售额")
    public ResponseEntity<Object> get(SalesStatisticsQueryCriteria criteria) {
        return new ResponseEntity<>(salesStatisticsService.get(criteria), HttpStatus.OK);
    }

    @ApiOperation("查询销售额排行")
    @GetMapping("/sales-rank")
    public ResponseEntity<Object> getProductSalesRank(SalesStatisticsQueryCriteria criteria) {
        return new ResponseEntity<>(salesStatisticsService.getProductSalesRank(criteria), HttpStatus.OK);
    }

    @Log("查询产品销售数据")
    @GetMapping
    @ApiOperation("查询产量、销量和销售额详细数据")
    public ResponseEntity<Object> getSalesData(SalesStatisticsQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(salesStatisticsService.getSalesData(criteria, pageable), HttpStatus.OK);
    }

    @Log("导出产品销售数据")
    @ApiOperation("导出产品销售数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('product-statistics:list')")
    public void download(HttpServletResponse response, SalesStatisticsQueryCriteria criteria) throws IOException {
        salesStatisticsService.download(salesStatisticsService.queryAll(criteria), response);
    }
}
