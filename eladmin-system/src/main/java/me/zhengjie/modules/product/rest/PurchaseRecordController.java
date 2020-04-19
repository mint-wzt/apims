package me.zhengjie.modules.product.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.aop.log.Log;
import me.zhengjie.config.DataScope;
import me.zhengjie.modules.product.domain.PurchaseRecord;
import me.zhengjie.modules.product.service.PurchaseRecordService;
import me.zhengjie.modules.product.service.dto.PurchaseRecordQueryCriteria;

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
@Api(tags = "农产品：原材料采购信息")
@RestController
@RequestMapping("/api/purchase-record")
public class PurchaseRecordController {

    @Autowired
    private PurchaseRecordService purchaseService;

    @Autowired
    private DataScope dataScope;

    @Autowired
    private DeptService deptService;

    @Log("导出原材料采购数据")
    @ApiOperation("导出原材料采购数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('purchase-record:list')")
    public void download(HttpServletResponse response, PurchaseRecordQueryCriteria criteria) throws IOException {
        purchaseService.download(purchaseService.queryAll(criteria), response);
    }

    @Log("查询原材料采购数据")
    @ApiOperation("查询原材料采购数据")
    @GetMapping
    @PreAuthorize("@el.check('purchase-record:list')")
    public ResponseEntity<Object> getUsers(PurchaseRecordQueryCriteria criteria, Pageable pageable){
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
                return new ResponseEntity<>(purchaseService.queryAll(criteria,pageable),HttpStatus.OK);
            }
            // 否则取并集
        } else {
            result.addAll(deptSet);
            result.addAll(deptIds);
            criteria.setDeptIds(result);
            return new ResponseEntity<>(purchaseService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @Log("新增原材料采购数据")
    @ApiOperation("新增原材料采购数据")
    @PostMapping
    @PreAuthorize("@el.check('purchase-record:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PurchaseRecord resources){
        return new ResponseEntity<>(purchaseService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改原材料采购数据")
    @ApiOperation("修改原材料采购数据")
    @PutMapping
    @PreAuthorize("@el.check('purchase-record:edit')")
    public ResponseEntity<Object> update(@Validated(PurchaseRecord.Update.class) @RequestBody PurchaseRecord resources){
        purchaseService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除原材料采购数据")
    @ApiOperation("删除原材料采购数据")
    @DeleteMapping
    @PreAuthorize("@el.check('purchase-record:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        purchaseService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
