package me.zhengjie.modules.product.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.product.domain.Product;
import me.zhengjie.modules.product.service.dto.ProductSmallDto;
import me.zhengjie.modules.system.service.mapper.DeptMapper;
import me.zhengjie.modules.system.service.mapper.RegionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductSmallMapper extends BaseMapper<ProductSmallDto, Product> {
}
