package me.zhengjie.modules.statistics.rest;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.aop.log.Log;
import me.zhengjie.config.DataScope;
import me.zhengjie.modules.statistics.domain.RemunerationPayment;
import me.zhengjie.modules.statistics.service.RemunerationPaymentService;
import me.zhengjie.modules.statistics.service.dto.RemunerationPaymentQueryCriteria;
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

@Api(tags = "补助发放")
@Slf4j
@RestController
@RequestMapping("/api/remuneration-payment")
public class RemunerationPaymentController {

    @Autowired
    private RemunerationPaymentService paymentService;

    @Autowired
    private DataScope dataScope;

    @Autowired
    private DeptService deptService;

    @Log("导出补助发放项目")
    @ApiOperation("导出补助发放项目")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('remuneration-payment:list')")
    public void download(HttpServletResponse response, RemunerationPaymentQueryCriteria criteria) throws IOException {
        paymentService.download(paymentService.queryAll(criteria), response);
    }

    @Log("查询补助发放项目")
    @ApiOperation("查询补助发放项目")
    @GetMapping
    @PreAuthorize("@el.check('remuneration-payment:list')")
    public ResponseEntity<Object> getRemunerationPayments(RemunerationPaymentQueryCriteria criteria, Pageable pageable){
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
                return new ResponseEntity<>(paymentService.queryAll(criteria,pageable),HttpStatus.OK);
            }
            // 否则取并集
        } else {
            result.addAll(deptSet);
            result.addAll(deptIds);
            criteria.setDeptIds(result);
            return new ResponseEntity<>(paymentService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @Log("新增补助发放项目")
    @ApiOperation("新增补助发放项目")
    @PostMapping
    @PreAuthorize("@el.check('remuneration-payment:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RemunerationPayment resources){
        return new ResponseEntity<>(paymentService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改补助发放项目")
    @ApiOperation("修改补助发放项目")
    @PutMapping
    @PreAuthorize("@el.check('remuneration-payment:edit')")
    public ResponseEntity<Object> update(@Validated(RemunerationPayment.Update.class) @RequestBody RemunerationPayment resources){
        paymentService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除补助发放项目")
    @ApiOperation("删除补助发放项目")
    @DeleteMapping
    @PreAuthorize("@el.check('remuneration-payment:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        paymentService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
