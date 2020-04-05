package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.Region;
import me.zhengjie.modules.system.repository.RegionRepository;
import me.zhengjie.modules.system.service.RegionService;
import me.zhengjie.modules.system.service.dto.RegionDto;
import me.zhengjie.modules.system.service.mapper.RegionMapper;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@CacheConfig(cacheNames = "region")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    private final RegionMapper regionMapper;

    public RegionServiceImpl(RegionRepository regionRepository, RegionMapper regionMapper) {
        this.regionRepository = regionRepository;
        this.regionMapper = regionMapper;
    }

    @Override
    @Cacheable(key = "#p0")
    public RegionDto findById(String id) {
        Region region = regionRepository.findById(id).orElseGet(Region::new);
        ValidationUtil.isNull(region.getId(),"region","id",id);
        return regionMapper.toDto(region);
    }

    @Override
    public List<Region> findByName(String name) {

        return null;
    }
}
