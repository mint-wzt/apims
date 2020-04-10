package me.zhengjie.modules.product.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.product.domain.Category;
import me.zhengjie.modules.product.service.dto.CategorySmallDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategorySmallMapper extends BaseMapper<CategorySmallDto, Category> {
}
