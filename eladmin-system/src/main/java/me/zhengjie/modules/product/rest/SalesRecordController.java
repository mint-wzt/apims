package me.zhengjie.modules.product.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.aop.log.Log;
import me.zhengjie.config.DataScope;
import me.zhengjie.modules.product.domain.SalesRecord;
import me.zhengjie.modules.product.service.SalesRecordService;
import me.zhengjie.modules.product.service.dto.SalesRecordQueryCriteria;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
import java.util.Set;

@Slf4j
@Api(tags = "农产品：产品销售数据")
@RestController
@RequestMapping("/api/sales-record")
public class SalesRecordController {

    @Autowired
    private SalesRecordService salesRecordService;

    @Autowired
    private DataScope dataScope;

    @Autowired
    private DeptService deptService;

    @Log("导出销售数据")
    @ApiOperation("导出销售数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('sales-record:list')")
    public void download(HttpServletResponse response, SalesRecordQueryCriteria criteria) throws IOException {
        salesRecordService.download(salesRecordService.queryAll(criteria), response);
    }

    @Log("查询销售数据")
    @ApiOperation("查询销售数据")
    @GetMapping
    @PreAuthorize("@el.check('sales-record:list')")
    public ResponseEntity<Object> getProducts(SalesRecordQueryCriteria criteria, Pageable pageable){
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
            if(result.size() == 0){
                return new ResponseEntity<>(PageUtil.toPage(null,0),HttpStatus.OK);
            } else {
                return new ResponseEntity<>(salesRecordService.queryAll(criteria,pageable),HttpStatus.OK);
            }
            // 否则取并集
        } else {
            result.addAll(deptSet);
            result.addAll(deptIds);
            criteria.setDeptIds(result);
            return new ResponseEntity<>(salesRecordService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @Log("新增销售数据")
    @ApiOperation("新增销售数据")
    @PostMapping
    @PreAuthorize("@el.check('sales-record:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody SalesRecord resources){
        return new ResponseEntity<>(salesRecordService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改销售数据")
    @ApiOperation("修改销售数据")
    @PutMapping
    @PreAuthorize("@el.check('sales-record:edit')")
    public ResponseEntity<Object> update(@Validated(SalesRecord.Update.class) @RequestBody SalesRecord resources){
        salesRecordService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除销售数据")
    @ApiOperation("删除销售数据")
    @DeleteMapping
    @PreAuthorize("@el.check('sales-record:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        salesRecordService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
