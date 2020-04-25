package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.SysSetUp;
import me.zhengjie.modules.system.service.SysSetUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Api(tags = "系统：系统设置")
@Slf4j
@RestController
@RequestMapping("/api/system")
public class SysSetUpController {

    @Autowired
    private SysSetUpService sysSetUpService;

    @GetMapping
    @ApiOperation("查询")
    @PreAuthorize("@el.check('system:list')")
    public ResponseEntity<Object> get(){
        return new ResponseEntity<>(sysSetUpService.get(), HttpStatus.OK);
    }

    @Log("修改系统信息")
    @ApiOperation("修改系统信息")
    @PutMapping
    @PreAuthorize("@el.check('system:edit')")
    public ResponseEntity<Object> update(@Validated(SysSetUp.Update.class) @RequestBody SysSetUp resources){
        sysSetUpService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除系统信息")
    @ApiOperation("删除系统信息")
    @DeleteMapping
    @PreAuthorize("@el.check('system:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        sysSetUpService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("修改系统头像")
    @ApiOperation("修改系统头像")
    @PostMapping(value = "/logo")
    @PreAuthorize("@el.check('system:edit')")
    public ResponseEntity<Object> updateLogo(@RequestParam MultipartFile file){
        sysSetUpService.updateLogo(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
