package me.zhengjie.modules.product.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.product.domain.PurchaseRecord;
import me.zhengjie.modules.product.service.dto.PurchaseRecordDto;
import me.zhengjie.modules.system.service.mapper.DeptMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, DeptMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PurchaseRecordMapper extends BaseMapper<PurchaseRecordDto, PurchaseRecord> {
}
