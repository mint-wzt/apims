package me.zhengjie.modules.statistics.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.statistics.service.MarketService;
import me.zhengjie.modules.statistics.service.dto.MarketQueryCriteria;
import me.zhengjie.modules.statistics.service.dto.PriceQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "市场：农产品销售市场")
@Slf4j
@RestController
@RequestMapping("/api/market")
public class MarketController {

    @Autowired
    private MarketService marketService;

    @GetMapping
    @ApiOperation("查询产品价格行情")
    public ResponseEntity<Object> getLatestPrice(MarketQueryCriteria criteria){
        return new ResponseEntity<>(marketService.findByRegionName(criteria.getRegionName()), HttpStatus.OK);
    }
}
