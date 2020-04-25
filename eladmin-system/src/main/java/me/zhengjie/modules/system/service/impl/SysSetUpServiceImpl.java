package me.zhengjie.modules.system.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.system.domain.SysSetUp;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.UserAvatar;
import me.zhengjie.modules.system.repository.SysSetUpRepository;
import me.zhengjie.modules.system.service.SysSetUpService;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Set;

@Service
@Slf4j
@CacheConfig(cacheNames = "system")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysSetUpServiceImpl implements SysSetUpService {

    @Autowired
    private SysSetUpRepository setUpRepository;

    @Value("${file.avatar}")
    private String logo;



    @Override
    public SysSetUp create(SysSetUp resources) {
        return setUpRepository.save(resources);
    }

    @Override
    public void update(SysSetUp resources) {
        SysSetUp sysSetUp = setUpRepository.findAll().get(0);
        sysSetUp.setCaseNumber(resources.getCaseNumber());
        sysSetUp.setCopyright(resources.getCopyright());
        sysSetUp.setSystemName(resources.getSystemName());
        sysSetUp.setEnterprise(resources.getEnterprise());
        sysSetUp.setDescription(resources.getDescription());
        sysSetUp.setOwner(resources.getOwner());
        sysSetUp.setRegion(resources.getRegion());
        sysSetUp.setSysStatus(resources.getSysStatus());
        setUpRepository.save(sysSetUp);
    }

    @Override
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            setUpRepository.deleteById(id);
        }
    }

    @Override
    public void updateLogo(MultipartFile multipartFile) {
        SysSetUp sysSetUp = setUpRepository.findAll().get(0);
        File file = FileUtil.upload(multipartFile, logo);
        assert file != null;
        sysSetUp.setSystemLogo(file.getName());
        setUpRepository.save(sysSetUp);
    }

    @Override
    public SysSetUp get() {
        return setUpRepository.findAll().get(0);
    }
}
