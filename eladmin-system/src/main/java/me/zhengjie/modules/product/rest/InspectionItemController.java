package me.zhengjie.modules.product.rest;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.product.domain.InspectionItem;
import me.zhengjie.modules.product.service.InspectionItemService;
import me.zhengjie.modules.product.service.dto.InspectionItemQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Slf4j
@Api(tags = "农产品：检测项基础属性管理")
@RestController
@RequestMapping("/api/inspection-item")
public class InspectionItemController {

    @Autowired
    private InspectionItemService itemService;

    @Log("导出检测项数据")
    @ApiOperation("导出检测项数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('inspection-item:list')")
    public void download(HttpServletResponse response, InspectionItemQueryCriteria criteria) throws IOException {
        itemService.download(itemService.queryAll(criteria), response);
    }

    @Log("查询检测项")
    @ApiOperation("查询检测项")
    @GetMapping
    @PreAuthorize("@el.check('inspection-item:list')")
    public ResponseEntity<Object> getInspectionItems(InspectionItemQueryCriteria criteria, Pageable pageable) {
        log.info(JSON.toJSONString(criteria));
        return new ResponseEntity<>(itemService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增检测项")
    @ApiOperation("新增检测项")
    @PostMapping
    @PreAuthorize("@el.check('inspection-item:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody InspectionItem resources) {
        return new ResponseEntity<>(itemService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改检测项")
    @ApiOperation("修改检测项")
    @PutMapping
    @PreAuthorize("@el.check('inspection-item:edit')")
    public ResponseEntity<Object> update(@Validated(InspectionItem.Update.class) @RequestBody InspectionItem resources) {
        itemService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除检测项")
    @ApiOperation("删除检测项")
    @DeleteMapping
    @PreAuthorize("@el.check('inspection-item:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids) {
        itemService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
