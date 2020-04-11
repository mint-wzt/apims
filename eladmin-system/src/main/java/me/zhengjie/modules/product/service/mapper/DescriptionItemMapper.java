package me.zhengjie.modules.product.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.product.domain.DescriptionItem;
import me.zhengjie.modules.product.service.dto.DescriptionItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DescriptionItemMapper extends BaseMapper<DescriptionItemDto, DescriptionItem> {
}
