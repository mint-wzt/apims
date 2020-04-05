package me.zhengjie.modules.system.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.system.domain.Region;
import me.zhengjie.modules.system.service.dto.RegionSmallDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegionSmallMapper extends BaseMapper<RegionSmallDto, Region> {
}
