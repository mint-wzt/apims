package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.statistics.domain.IndustryStatistics;
import me.zhengjie.modules.statistics.service.IndustryStatisticsService;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.Employee;
import me.zhengjie.modules.system.domain.Region;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.DeptRepository;
import me.zhengjie.modules.system.repository.EmployeeRepository;
import me.zhengjie.modules.system.repository.RegionRepository;
import me.zhengjie.modules.system.service.EmployeeService;
import me.zhengjie.modules.system.service.dto.*;
import me.zhengjie.modules.system.service.mapper.EmployeeMapper;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
@CacheConfig(cacheNames = "employee")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private IndustryStatisticsService statisticsService;

    @Autowired
    private DeptRepository deptRepository;


    /**
     * 按ID查询
     * @param id
     * @return
     */
    @Override
    public EmployeeDto findById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseGet(Employee::new);
        ValidationUtil.isNull(employee.getId(), "User", "id", id);
        return employeeMapper.toDto(employee);
    }

    /**
     * 添加员工
     * @param employee
     * @return
     */
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public EmployeeDto create(Employee employee) {
        Employee e = employeeRepository.save(employee);
        // 为地区添加组织机构数
        IndustryStatistics industryStatistics = new IndustryStatistics();
        Dept dept = deptRepository.findById(e.getDept().getId()).get();
        industryStatistics.setRegionId(dept.getRegion().getId());
        industryStatistics.setRegionName(dept.getRegion().getExtName());
        industryStatistics.setStatisticsTime(e.getCreateTime());
        industryStatistics.setStatisticsTotal(1d);
        industryStatistics.setUnit("人");
        industryStatistics.setStatisticsItem("员工");
        statisticsService.create(industryStatistics);

        return employeeMapper.toDto(e);
    }

    /**
     * 编辑员工
     * @param employee
     */
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Employee employee) {
        employeeRepository.save(employee);
    }

    /**
     * 批量删除员工
     * @param ids
     */
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids){
            employeeRepository.deleteById(id);
        }
    }

    /**
     * 查询员工分页
     * @param criteria 条件
     * @param pageable 分页参数
     * @return
     */
    @Override
    public Object queryAll(EmployeeQueryCriteria criteria, Pageable pageable) {
        Page<Employee> page = employeeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(employeeMapper::toDto));
    }

    /**
     * 查询所有员工不分页
     * @param criteria 条件
     * @return
     */
    @Override
    public List<EmployeeDto> queryAll(EmployeeQueryCriteria criteria) {
        List<Employee> employees = employeeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return employeeMapper.toDto(employees);
    }

    /**
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException
     */
    @Override
    public void download(List<EmployeeDto> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (EmployeeDto employeeDtO : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("员工编号",employeeDtO.getNumber());
            map.put("姓名", employeeDtO.getName());
            map.put("性别",employeeDtO.getSex());
            map.put("部门", employeeDtO.getDept().getName());
            map.put("职位", employeeDtO.getPosition());
            map.put("员工类型", employeeDtO.getEmployeeType());
            map.put("联系方式", employeeDtO.getContact());
            map.put("身份证号码",employeeDtO.getIdNumber());
            map.put("家庭住址", employeeDtO.getAddress());
            map.put("健康情况", employeeDtO.getHealthCondition());
            map.put("致贫原因", employeeDtO.getCausePoverty());
            map.put("帮扶责任人", employeeDtO.getResponsiblePerson());
            map.put("文化程度", employeeDtO.getEducationLevel());
            map.put("家庭情况", employeeDtO.getFamilySituation());
            map.put("耕地面积/亩", employeeDtO.getCultivatedArea());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
