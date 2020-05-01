package me.zhengjie.modules.statistics.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.statistics.service.PriceService;
import me.zhengjie.modules.statistics.service.dto.PriceQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "价格行情：价格行情数据")
@Slf4j
@RestController
@RequestMapping("/api/price-statistics")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @GetMapping
    @ApiOperation("查询产品价格行情")
    public ResponseEntity<Object> getLatestPrice(PriceQueryCriteria criteria){
        return new ResponseEntity<>(priceService.getLatestPrice(criteria), HttpStatus.OK);
    }
}
