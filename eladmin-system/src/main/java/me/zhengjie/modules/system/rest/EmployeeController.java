package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.aop.log.Log;
import me.zhengjie.config.DataScope;
import me.zhengjie.modules.system.domain.Employee;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.modules.system.service.EmployeeService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.EmployeeQueryCriteria;
import me.zhengjie.modules.system.service.dto.UserQueryCriteria;
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

@Api(tags = "系统：员工管理")
@Slf4j
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private DataScope dataScope;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DeptService deptService;

    @Log("导出员工数据")
    @ApiOperation("导出员工数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employee:list')")
//    @AnonymousAccess
    public void download(HttpServletResponse response, EmployeeQueryCriteria criteria) throws IOException {
        employeeService.download(employeeService.queryAll(criteria), response);
    }

    @Log("查询员工")
    @ApiOperation("查询员工")
    @GetMapping
    @PreAuthorize("@el.check('employee:list')")
//    @AnonymousAccess
    public ResponseEntity<Object> getUsers(EmployeeQueryCriteria criteria, Pageable pageable){
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
                return new ResponseEntity<>(employeeService.queryAll(criteria,pageable),HttpStatus.OK);
            }
            // 否则取并集
        } else {
            result.addAll(deptSet);
            result.addAll(deptIds);
            criteria.setDeptIds(result);
            return new ResponseEntity<>(employeeService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @Log("新增员工")
    @ApiOperation("新增员工")
    @PostMapping
    @PreAuthorize("@el.check('employee:add')")
//    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody Employee resources) {
        return new ResponseEntity<>(employeeService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改员工")
    @ApiOperation("修改员工")
    @PutMapping
    @PreAuthorize("@el.check('employee:edit')")
//    @AnonymousAccess
    public ResponseEntity<Object> update(@Validated(Employee.Update.class) @RequestBody Employee resources) {
        employeeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除员工")
    @ApiOperation("删除员工")
    @DeleteMapping
    @PreAuthorize("@el.check('employee:del')")
//    @AnonymousAccess
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids) {
        employeeService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
