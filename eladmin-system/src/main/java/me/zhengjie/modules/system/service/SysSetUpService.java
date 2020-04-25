package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.SysSetUp;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface SysSetUpService {

    /**
     * 新增
     * @param resources /
     * @return /
     */
    SysSetUp create(SysSetUp resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(SysSetUp resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 修改logo
     * @param multipartFile 文件
     */
    void updateLogo(MultipartFile multipartFile);

    SysSetUp get();
}
