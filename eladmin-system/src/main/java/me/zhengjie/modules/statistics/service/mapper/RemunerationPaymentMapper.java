package me.zhengjie.modules.statistics.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.statistics.domain.RemunerationPayment;
import me.zhengjie.modules.statistics.service.dto.RemunerationPaymentDto;
import me.zhengjie.modules.system.service.mapper.DeptMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {DeptMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RemunerationPaymentMapper extends BaseMapper<RemunerationPaymentDto, RemunerationPayment> {
}
