package me.zhengjie.modules.product.rest;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.aop.log.Log;
import me.zhengjie.domain.VerificationCode;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.product.domain.InspectionTemplate;
import me.zhengjie.modules.product.service.InspectionTemplateService;
import me.zhengjie.modules.product.service.dto.InspectionTemplateQueryCriteria;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.vo.UserPassVo;
import me.zhengjie.modules.system.service.RoleService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.RoleSmallDto;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.modules.system.service.dto.UserQueryCriteria;
import me.zhengjie.utils.ElAdminConstant;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.SecurityUtils;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "模板管理：检测项模板管理")
@RestController
@RequestMapping("/api/inspection-template")
public class InspectionTemplateController {

    @Autowired
    private InspectionTemplateService templateService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Log("导出模板数据")
    @ApiOperation("导出模板数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('inspection-template:list')")
    public void download(HttpServletResponse response, InspectionTemplateQueryCriteria criteria) throws IOException {
        templateService.download(templateService.queryAll(criteria), response);
    }

    @Log("查询模板")
    @ApiOperation("查询模板")
    @GetMapping
    @PreAuthorize("@el.check('inspection-template:list')")
    public ResponseEntity<Object> getTemplates(InspectionTemplateQueryCriteria criteria, Pageable pageable){
        UserDto user = userService.findByName(SecurityUtils.getUsername());
        Integer currentLevel =  Collections.min(roleService.findByUsersId(user.getId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
        if (currentLevel != 1) {
           criteria.setUserId(user.getId());
        }
        log.info(JSON.toJSONString(criteria));
        Object o = templateService.queryAll(criteria,pageable);
        return new ResponseEntity<>(o,HttpStatus.OK);
    }

    @Log("新增模板")
    @ApiOperation("新增模板")
    @PostMapping
    @PreAuthorize("@el.check('inspection-template:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody InspectionTemplate resources){
        return new ResponseEntity<>(templateService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改模板")
    @ApiOperation("修改模板")
    @PutMapping
    @PreAuthorize("@el.check('inspection-template:edit')")
    public ResponseEntity<Object> update(@Validated(InspectionTemplate.Update.class) @RequestBody InspectionTemplate resources){
        templateService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @Log("删除模板")
    @ApiOperation("删除模板")
    @DeleteMapping
    @PreAuthorize("@el.check('inspection-template:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        UserDto user = userService.findByName(SecurityUtils.getUsername());
        Long userId = user.getId();
        for (Long id : ids) {
            if (! userId.equals(templateService.findById(id).getUser().getId())) {
                throw new BadRequestException("角色权限不足，不能删除：" + id);
            }
        }
        templateService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
