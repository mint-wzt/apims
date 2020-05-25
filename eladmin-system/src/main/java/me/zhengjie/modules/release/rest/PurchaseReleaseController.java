package me.zhengjie.modules.release.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.aop.log.Log;
import me.zhengjie.config.DataScope;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.release.domain.PurchaseRelease;
import me.zhengjie.modules.release.service.PurchaseReleaseService;
import me.zhengjie.modules.release.service.dto.PurchaseReleaseQueryCriteria;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.utils.PageUtil;
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
@Api(tags = "农产品：采购消息发布")
@RestController
@RequestMapping("/api/purchase-release")
public class PurchaseReleaseController {

    private final PurchaseReleaseService purchaseReleaseService;

    private final DataScope dataScope;

    private final DeptService deptService;

    public PurchaseReleaseController(PurchaseReleaseService purchaseReleaseService, DataScope dataScope, DeptService deptService) {
        this.purchaseReleaseService = purchaseReleaseService;
        this.dataScope = dataScope;
        this.deptService = deptService;
    }

    @Log("导出采购消息发布数据")
    @ApiOperation("导出采购消息发布数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('purchase-release:list')")
    public void download(HttpServletResponse response, PurchaseReleaseQueryCriteria criteria) throws IOException {
        purchaseReleaseService.download(purchaseReleaseService.queryAll(criteria), response);
    }

    @Log("获取采购消息发布数据")
    @ApiOperation("获取采购消息发布数据")
    @GetMapping(value = "/release-new")
    public ResponseEntity<Object> getNewRelease(PurchaseReleaseQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(purchaseReleaseService.queryAll(criteria,pageable), HttpStatus.OK);
    }


    @Log("查询采购消息发布数据")
    @ApiOperation("查询采购消息发布数据")
    @GetMapping
    @PreAuthorize("@el.check('purchase-release:list')")
    public ResponseEntity<Object> getProducts(PurchaseReleaseQueryCriteria criteria, Pageable pageable){
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
                return new ResponseEntity<>(purchaseReleaseService.queryAll(criteria,pageable),HttpStatus.OK);
            }
            // 否则取并集
        } else {
            result.addAll(deptSet);
            result.addAll(deptIds);
            criteria.setDeptIds(result);
            return new ResponseEntity<>(purchaseReleaseService.queryAll(criteria,pageable), HttpStatus.OK);
        }
    }

    @Log("新增采购消息发布数据")
    @ApiOperation("新增采购消息发布数据")
    @PostMapping
    @PreAuthorize("@el.check('purchase-release:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PurchaseRelease resources){
        return new ResponseEntity<>(purchaseReleaseService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改采购消息发布数据")
    @ApiOperation("修改采购消息发布数据")
    @PutMapping
    @PreAuthorize("@el.check('purchase-release:edit')")
    public ResponseEntity<Object> update(@Validated(PurchaseRelease.Update.class) @RequestBody PurchaseRelease resources){
        purchaseReleaseService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除采购消息发布数据")
    @ApiOperation("删除采购消息发布数据")
    @DeleteMapping
    @PreAuthorize("@el.check('purchase-release:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        try {
            purchaseReleaseService.delete(ids);
        }catch (Exception e){
            throw new BadRequestException("删除失败！");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("采购数据访问量+1")
    @ApiOperation("采购数据访问量+1")
    @GetMapping("/views")
    public ResponseEntity<Object> addViews(Long id){
        purchaseReleaseService.addViews(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
