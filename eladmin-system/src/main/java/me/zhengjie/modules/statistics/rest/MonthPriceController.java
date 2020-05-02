package me.zhengjie.modules.statistics.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.statistics.service.MonthPriceService;
import me.zhengjie.modules.statistics.service.dto.MonthPriceQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "价格行情：价格行情分析")
@Slf4j
@RestController
@RequestMapping("/api/month-price")
public class MonthPriceController {

    @Autowired
    private MonthPriceService monthPriceService;

    @GetMapping(value = "/one-market-product")
    @ApiOperation("查询单一市场、单一产品的某年价格走势")
    public ResponseEntity<Object> getMonthPriceOneToOneMarket(MonthPriceQueryCriteria criteria){
        return new ResponseEntity<>(monthPriceService.getMonthPriceOneToOneMarket(criteria), HttpStatus.OK);
    }

    @GetMapping(value = "/more-market-product")
    @ApiOperation("查询单一市场、单一产品的某年价格走势")
    public ResponseEntity<Object> getMonthPriceOneToMoreMarket(MonthPriceQueryCriteria criteria){
        return new ResponseEntity<>(monthPriceService.getMonthPriceOneToMoreMarket(criteria), HttpStatus.OK);
    }
}
