package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.Region;
import me.zhengjie.modules.system.service.dto.RegionDto;

import java.util.List;

public interface RegionService {

    /**
     * 通过ID查找地区
     * @param id
     * @return
     */
    RegionDto findById(String id);

    /**
     * 通过地区名查找地区
     * @param name
     * @return
     */
    List<Region> findByName(String name);
}
