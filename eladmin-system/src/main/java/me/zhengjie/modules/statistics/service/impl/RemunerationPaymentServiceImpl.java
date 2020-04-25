package me.zhengjie.modules.statistics.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.statistics.domain.IndustryStatistics;
import me.zhengjie.modules.statistics.domain.RemunerationPayment;
import me.zhengjie.modules.statistics.repository.RemunerationPaymentRepository;
import me.zhengjie.modules.statistics.service.IndustryStatisticsService;
import me.zhengjie.modules.statistics.service.RemunerationPaymentService;
import me.zhengjie.modules.statistics.service.dto.RemunerationPaymentDto;
import me.zhengjie.modules.statistics.service.dto.RemunerationPaymentQueryCriteria;
import me.zhengjie.modules.statistics.service.mapper.RemunerationPaymentMapper;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.Region;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.DeptRepository;
import me.zhengjie.modules.system.service.dto.RoleSmallDto;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@CacheConfig(cacheNames = "remuneration-payment")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RemunerationPaymentServiceImpl implements RemunerationPaymentService {

    @Autowired
    private RemunerationPaymentRepository paymentRepository;

    @Autowired
    private RemunerationPaymentMapper paymentMapper;

    @Autowired
    private IndustryStatisticsService statisticsService;

    @Autowired
    private DeptRepository deptRepository;

    @Override
    @Cacheable
    public RemunerationPaymentDto findById(long id) {
        RemunerationPayment payment = paymentRepository.findById(id).orElseGet(RemunerationPayment::new);
        ValidationUtil.isNull(payment.getId(),"RemunerationPayment","id",id);
        return paymentMapper.toDto(payment);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public RemunerationPaymentDto create(RemunerationPayment resources) {

        // 通过部门查询地区ID
        Dept dept = deptRepository.findById(resources.getDept().getId()).get();
        resources.setRegionId(dept.getRegion().getId());
        resources.setRegionName(dept.getRegion().getExtName());
        RemunerationPayment payment = paymentRepository.save(resources);

        // 数据统计帮扶项目和酬金发放数量
        statisticsService.create(get(resources,dept.getRegion(),"帮扶项目",1d,"个"));
        statisticsService.create(get(resources,dept.getRegion(),"帮扶资金",resources.getPaymentTotal(),"元"));

        return paymentMapper.toDto(payment);
    }

    public IndustryStatistics get(RemunerationPayment resources, Region region,String item,Double total,String unit){
        IndustryStatistics industryStatistics = new IndustryStatistics();
        industryStatistics.setRegionId(region.getId());
        industryStatistics.setRegionName(region.getExtName());
        industryStatistics.setStatisticsItem(item);
        industryStatistics.setStatisticsTotal(total);
        industryStatistics.setUnit(unit);
        industryStatistics.setStatisticsTime(resources.getPaymentTime());
        return industryStatistics;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(RemunerationPayment resources) {
        RemunerationPayment payment = paymentRepository.findById(resources.getId()).orElseGet(RemunerationPayment::new);
        ValidationUtil.isNull(payment.getId(),"RemunerationPayment","id",resources.getId());

        payment.setDept(resources.getDept());
        payment.setDeptName(resources.getDeptName());
        payment.setDistributor(resources.getDistributor());
        payment.setName(resources.getName());
        payment.setPaymentReason(resources.getPaymentReason());
        payment.setPaymentTotal(resources.getPaymentTotal());
        payment.setPaymentTime(resources.getPaymentTime());
        payment.setRemark(resources.getRemark());

        paymentRepository.save(payment);

    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id: ids){
            paymentRepository.deleteById(id);
        }
    }

    @Override
    @Cacheable
    public Object queryAll(RemunerationPaymentQueryCriteria criteria, Pageable pageable) {
        Page<RemunerationPayment> page = paymentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(paymentMapper::toDto));
    }

    @Override
    @Cacheable
    public List<RemunerationPaymentDto> queryAll(RemunerationPaymentQueryCriteria criteria) {
        List<RemunerationPayment> users = paymentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return paymentMapper.toDto(users);
    }

    @Override
    public void download(List<RemunerationPaymentDto> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RemunerationPaymentDto paymentDto : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("名称", paymentDto.getName());
            map.put("发放人", paymentDto.getDistributor());
            map.put("发放事由", paymentDto.getPaymentReason());
            map.put("发放时间", paymentDto.getPaymentTime());
            map.put("发放总额", paymentDto.getPaymentTotal());
            map.put("行政区域ID", paymentDto.getRegionId());
            map.put("行政区域名称", paymentDto.getRegionName());
            map.put("所属机构名称", paymentDto.getRegionName());
            map.put("备注", paymentDto.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
