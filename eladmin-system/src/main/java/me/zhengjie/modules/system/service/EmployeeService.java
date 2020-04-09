package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.Employee;
import me.zhengjie.modules.system.service.dto.EmployeeDto;
import me.zhengjie.modules.system.service.dto.EmployeeQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface EmployeeService {
    /**
     * 根据ID查询
     * @param id
     * @return
     */
    EmployeeDto findById(Long id);

    /**
     * 新增员工
     * @param employee
     * @return
     */
    EmployeeDto create(Employee employee);

    /**
     * 编辑员工
     * @param employee
     */
    void update(Employee employee);

    /**
     * 删除员工
     * @param ids
     */
    void delete(Set<Long> ids);

    /**
     * 查询全部
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    Object queryAll(EmployeeQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<EmployeeDto> queryAll(EmployeeQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<EmployeeDto> queryAll, HttpServletResponse response) throws IOException;

}
