package me.zhengjie.modules.product.service;

import me.zhengjie.modules.product.domain.SalesRecord;
import me.zhengjie.modules.product.service.dto.SalesRecordDto;
import me.zhengjie.modules.product.service.dto.SalesRecordQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface SalesRecordService {
    /**
     * 根据ID查询
     * @param id ID
     * @return /
     */
    SalesRecordDto findById(long id);

    /**
     * 新增
     * @param resources /
     * @return /
     */
    SalesRecordDto create(SalesRecord resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(SalesRecord resources);

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
    Object queryAll(SalesRecordQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<SalesRecordDto> queryAll(SalesRecordQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<SalesRecordDto> queryAll, HttpServletResponse response) throws IOException;
}
