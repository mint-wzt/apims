package me.zhengjie.modules.product.service.mapper;


import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.product.domain.Product;
import me.zhengjie.modules.product.service.dto.ProductDto;
import me.zhengjie.modules.system.service.mapper.DeptMapper;
import me.zhengjie.modules.system.service.mapper.RegionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, RegionMapper.class, DeptMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper extends BaseMapper<ProductDto, Product> {
}
