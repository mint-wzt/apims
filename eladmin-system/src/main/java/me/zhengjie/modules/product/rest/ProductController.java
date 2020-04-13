package me.zhengjie.modules.product.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.config.DataScope;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.product.domain.Product;
import me.zhengjie.modules.product.service.ProductService;
import me.zhengjie.modules.product.service.dto.ProductDto;
import me.zhengjie.modules.product.service.dto.ProductQueryCriteria;
import me.zhengjie.modules.system.domain.Region;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.modules.system.service.RoleService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.RegionSmallDto;
import me.zhengjie.modules.system.service.dto.RoleSmallDto;
import me.zhengjie.modules.system.service.dto.UserDto;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "农产品：组织机构产品目录")
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private DataScope dataScope;

    @Log("导出产品数据")
    @ApiOperation("导出产品数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('product:list')")
    public void download(HttpServletResponse response, ProductQueryCriteria criteria) throws IOException {
        productService.download(productService.queryAll(criteria), response);
}

    @Log("查询产品")
    @ApiOperation("查询产品")
    @GetMapping
    @PreAuthorize("@el.check('product:list')")
    public ResponseEntity<Object> getProducts(ProductQueryCriteria criteria, Pageable pageable){
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
                return new ResponseEntity<>(productService.queryAll(criteria,pageable),HttpStatus.OK);
            }
            // 否则取并集
        } else {
            result.addAll(deptSet);
            result.addAll(deptIds);
            criteria.setDeptIds(result);
            return new ResponseEntity<>(productService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @Log("新增产品")
    @ApiOperation("新增产品")
    @PostMapping
    @PreAuthorize("@el.check('product:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Product resources){
        return new ResponseEntity<>(productService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改产品")
    @ApiOperation("修改产品")
    @PutMapping
    @PreAuthorize("@el.check('product:edit')")
    public ResponseEntity<Object> update(@Validated(User.Update.class) @RequestBody Product resources){
//        checkLevel(resources);
        productService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除产品")
    @ApiOperation("删除产品")
    @DeleteMapping
    @PreAuthorize("@el.check('product:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        // 查询用户所在的地区
        UserDto user = userService.findByName(SecurityUtils.getUsername());
//        String currentRegion = user.getRegion().getId();
        Long currentDeptId = user.getDept().getId();
        Integer currentLevel =  Collections.max(roleService.findByUsersId(user.getId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
        // 如果当前用户不是系统管理员
        if (currentLevel != 1){
            for (Long id : ids) {
                // 查询产品所在的部门
                ProductDto product = productService.findById(id);
                Long deptId = product.getDept().getId();
//                String optRegion = product.getRegion().getId();

                // 用户等级低于组织机构管理员，或者用户的所属部门与产品所属部门不同
                if (currentLevel < 4 || currentDeptId != deptId){
                    throw new BadRequestException("角色权限不足，不能删除：" + userService.findByName(SecurityUtils.getUsername()).getUsername());
                }
            }
        }
        productService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
//
//    /**
//     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
//     * @param resources /
//     */
//    private void checkLevel(User resources) {
//        UserDto user = userService.findByName(SecurityUtils.getUsername());
//        Integer currentLevel =  Collections.min(roleService.findByUsersId(user.getId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
//        Integer optLevel = roleService.findByRoles(resources.getRoles());
//        if (currentLevel > optLevel) {
//            throw new BadRequestException("角色权限不足");
//        }
//    }
}
