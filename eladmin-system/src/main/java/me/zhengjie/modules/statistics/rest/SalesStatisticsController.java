package me.zhengjie.modules.statistics.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.statistics.service.SalesStatisticsService;
import me.zhengjie.modules.statistics.service.dto.SalesStatisticsQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "首页：产品销量数据")
@Slf4j
@RestController
@RequestMapping("/api/sales-statistics")
public class SalesStatisticsController {

    @Autowired
    private SalesStatisticsService salesStatisticsService;

    @GetMapping
    @ApiOperation("查询产量、销量和销售额")
    public ResponseEntity<Object> get(SalesStatisticsQueryCriteria criteria){
        return new ResponseEntity<>(salesStatisticsService.get(criteria), HttpStatus.OK);
    }

    @ApiOperation("查询销售额排行")
    @GetMapping("/sales-rank")
    public ResponseEntity<Object> getProductSalesRank(SalesStatisticsQueryCriteria criteria){
        return new ResponseEntity<>(salesStatisticsService.getProductSalesRank(criteria), HttpStatus.OK);
    }
}
