package me.zhengjie.modules.statistics.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.statistics.service.IndustryStatisticsService;
import me.zhengjie.modules.statistics.service.dto.IndustryStatisticsQueryCriteria;
import me.zhengjie.modules.statistics.service.dto.ProductStatisticsQueryCriteria;
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

@Api(tags = "首页：产业分布")
@Slf4j
@RestController
@RequestMapping("/api/industry-statistics")
public class IndustryStatisticsController {

    @Autowired
    private IndustryStatisticsService industryStatisticsService;

    @GetMapping("/info")
    @ApiOperation("查询")
    public ResponseEntity<Object> get(IndustryStatisticsQueryCriteria criteria){
        return new ResponseEntity<>(industryStatisticsService.get(criteria), HttpStatus.OK);
    }

    @Log("查询产业数据")
    @GetMapping
    @ApiOperation("导出产业数据")
    @PreAuthorize("@el.check('product-statistics:list')")
    public ResponseEntity<Object> get(IndustryStatisticsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(industryStatisticsService.get(criteria, pageable), HttpStatus.OK);
    }

    @Log("导出产业数据")
    @ApiOperation("导出产业数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('product-statistics:list')")
    public void download(HttpServletResponse response, IndustryStatisticsQueryCriteria criteria) throws IOException {
        industryStatisticsService.download(industryStatisticsService.queryAll(criteria), response);
    }
}
