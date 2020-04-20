package me.zhengjie.modules.product.rest;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.aop.log.Log;
import me.zhengjie.config.DataScope;
import me.zhengjie.modules.product.domain.InspectionRecord;
import me.zhengjie.modules.product.service.InspectionRecordService;
import me.zhengjie.modules.product.service.dto.InspectionRecordData;
import me.zhengjie.modules.product.service.dto.InspectionRecordQueryCriteria;
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
@Api(tags = "农产品：产品检测数据")
@RestController
@RequestMapping("/api/inspection-record")
public class InspectionRecordController {

    @Autowired
    private InspectionRecordService recordService;

    @Autowired
    private  DataScope dataScope;

    @Autowired
    private  DeptService deptService;


    @Log("导出检测记录数据")
    @ApiOperation("导出检测记录数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('products-detection:list')")
    public void download(HttpServletResponse response, InspectionRecordQueryCriteria criteria) throws IOException {
        recordService.download(recordService.queryAll(criteria), response);
    }

    @Log("查询检测记录")
    @ApiOperation("查询检测记录")
    @GetMapping
    @PreAuthorize("@el.check('products-detection:list')")
    public ResponseEntity<Object> getProductRecord(InspectionRecordQueryCriteria criteria, Pageable pageable){
        log.info(JSON.toJSONString(criteria));
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
                return new ResponseEntity<>(recordService.queryAll(criteria,pageable),HttpStatus.OK);
            }
            // 否则取并集
        } else {
            result.addAll(deptSet);
            result.addAll(deptIds);
            criteria.setDeptIds(result);
            return new ResponseEntity<>(recordService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @Log("新增检测记录")
    @ApiOperation("新增检测记录")
    @PostMapping
    @PreAuthorize("@el.check('products-detection:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody InspectionRecordData resources){
        return new ResponseEntity<>(recordService.create(resources),HttpStatus.CREATED);
    }

//    @Log("修改检测记录")
//    @ApiOperation("修改检测记录")
//    @PutMapping
//    @PreAuthorize("@el.check('products-detection:edit')")
//    public ResponseEntity<Object> update(@Validated(User.Update.class) @RequestBody User resources){
//        checkLevel(resources);
//        userService.update(resources);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
    @Log("删除检测记录")
    @ApiOperation("删除检测记录")
    @DeleteMapping
    @PreAuthorize("@el.check('products-detection:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        recordService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
