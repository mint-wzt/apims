package me.zhengjie.modules.system.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.system.domain.Region;
import me.zhengjie.modules.system.service.dto.RegionDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {DeptMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegionMapper extends BaseMapper<RegionDto, Region> {
}
