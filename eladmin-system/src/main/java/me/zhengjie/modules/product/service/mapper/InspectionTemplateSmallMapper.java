package me.zhengjie.modules.product.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.product.domain.InspectionTemplate;
import me.zhengjie.modules.product.service.dto.InspectionTemplateSmallDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InspectionTemplateSmallMapper extends BaseMapper<InspectionTemplateSmallDto, InspectionTemplate> {
}
