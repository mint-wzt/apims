package me.zhengjie.modules.product.rest;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.aop.log.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.product.domain.Category;
import me.zhengjie.modules.product.domain.DescriptionItem;
import me.zhengjie.modules.product.service.DescriptionItemService;
import me.zhengjie.modules.product.service.dto.DescriptionItemQueryCriteria;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.dto.RoleSmallDto;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.modules.system.service.dto.UserQueryCriteria;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.SecurityUtils;
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
@Api(tags = "农产品：基本信息描述")
@RestController
@RequestMapping("/api/description")
public class DescriptionItemController {

    @Autowired
    private DescriptionItemService itemService;

    @Log("导出描述项数据")
    @ApiOperation("导出描述项数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('description:list')")
    public void download(HttpServletResponse response, DescriptionItemQueryCriteria criteria) throws IOException {
        itemService.download(itemService.queryAll(criteria), response);
    }

    @Log("查询描述项")
    @ApiOperation("查询描述项")
    @GetMapping
    @PreAuthorize("@el.check('description:list')")
    public ResponseEntity<Object> getDescriptionItems(DescriptionItemQueryCriteria criteria, Pageable pageable) {
        log.info(JSON.toJSONString(criteria));
        return new ResponseEntity<>(itemService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增描述项")
    @ApiOperation("新增描述项")
    @PostMapping
    @PreAuthorize("@el.check('description:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DescriptionItem resources) {
        return new ResponseEntity<>(itemService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改描述项")
    @ApiOperation("修改描述项")
    @PutMapping
    @PreAuthorize("@el.check('description:edit')")
    public ResponseEntity<Object> update(@Validated(DescriptionItem.Update.class) @RequestBody DescriptionItem resources) {
        itemService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除描述项")
    @ApiOperation("删除描述项")
    @DeleteMapping
    @PreAuthorize("@el.check('description:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids) {
        itemService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
