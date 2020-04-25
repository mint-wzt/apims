package me.zhengjie.modules.statistics.service;

import me.zhengjie.modules.statistics.domain.RemunerationPayment;
import me.zhengjie.modules.statistics.service.dto.RemunerationPaymentDto;
import me.zhengjie.modules.statistics.service.dto.RemunerationPaymentQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface RemunerationPaymentService {
    /**
     * 根据ID查询
     * @param id ID
     * @return /
     */
    RemunerationPaymentDto findById(long id);

    /**
     * 新增
     * @param resources /
     * @return /
     */
    RemunerationPaymentDto create(RemunerationPayment resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(RemunerationPayment resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 查询全部
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    Object queryAll(RemunerationPaymentQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<RemunerationPaymentDto> queryAll(RemunerationPaymentQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<RemunerationPaymentDto> queryAll, HttpServletResponse response) throws IOException;
}
