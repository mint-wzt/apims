package me.zhengjie.modules.product.service.mapper;


import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.product.domain.InspectionTemplate;
import me.zhengjie.modules.product.service.dto.InspectionTemplateDto;
import me.zhengjie.modules.system.service.mapper.UserMapper;
import me.zhengjie.modules.system.service.mapper.UserSmallMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, InspectionItemMapper.class, UserMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InspectionTemplateMapper extends BaseMapper<InspectionTemplateDto, InspectionTemplate> {
}
