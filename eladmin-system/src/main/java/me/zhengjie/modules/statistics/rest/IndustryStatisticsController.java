package me.zhengjie.modules.statistics.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.statistics.service.IndustryStatisticsService;
import me.zhengjie.modules.statistics.service.dto.IndustryStatisticsQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "首页：产业分布")
@Slf4j
@RestController
@RequestMapping("/api/industry-statistics")
public class IndustryStatisticsController {

    @Autowired
    private IndustryStatisticsService industryStatisticsService;

    @GetMapping
    @ApiOperation("查询")
    public ResponseEntity<Object> get(IndustryStatisticsQueryCriteria criteria){
        return new ResponseEntity<>(industryStatisticsService.get(criteria), HttpStatus.OK);
    }
}
