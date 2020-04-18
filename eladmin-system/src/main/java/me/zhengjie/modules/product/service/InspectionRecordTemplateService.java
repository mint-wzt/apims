package me.zhengjie.modules.product.service;

import me.zhengjie.modules.product.domain.InspectionRecordTemplate;
import me.zhengjie.modules.product.service.dto.InspectionRecordTemplateQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface InspectionRecordTemplateService {
    /**
     * 根据ID查询
     * @param id ID
     * @return /
     */
    InspectionRecordTemplate findById(long id);

    /**
     * 新增检测记录
     * @param resources /
     * @return /
     */
    InspectionRecordTemplate create(InspectionRecordTemplate resources);

    /**
     * 删除检测记录
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 查询全部
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    Object queryAll(InspectionRecordTemplateQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<InspectionRecordTemplate> queryAll(InspectionRecordTemplateQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<InspectionRecordTemplate> queryAll, HttpServletResponse response) throws IOException;
}
