package me.zhengjie.modules.product.service;

import me.zhengjie.modules.product.domain.InspectionRecord;
import me.zhengjie.modules.product.service.dto.InspectionRecordData;
import me.zhengjie.modules.product.service.dto.InspectionRecordDto;
import me.zhengjie.modules.product.service.dto.InspectionRecordQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface InspectionRecordService {
    /**
     * 根据ID查询
     * @param id ID
     * @return /
     */
    InspectionRecordDto findById(long id);

    /**
     * 新增检测记录
     * @param resources /
     * @return /
     */
    InspectionRecordDto create(InspectionRecordData resources);

    /**
     * 编辑检测记录
     * @param resources /
     */
    void update(InspectionRecord resources);

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
    Object queryAll(InspectionRecordQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<InspectionRecordDto> queryAll(InspectionRecordQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<InspectionRecordDto> queryAll, HttpServletResponse response) throws IOException;
}
