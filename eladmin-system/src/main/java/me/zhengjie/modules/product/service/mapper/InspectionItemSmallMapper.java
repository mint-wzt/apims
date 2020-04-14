package me.zhengjie.modules.product.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.product.domain.InspectionItem;
import me.zhengjie.modules.product.service.dto.InspectionItemSmallDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InspectionItemSmallMapper extends BaseMapper<InspectionItemSmallDto, InspectionItem> {
}
