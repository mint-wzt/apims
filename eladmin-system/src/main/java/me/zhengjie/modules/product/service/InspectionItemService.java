package me.zhengjie.modules.product.service;

import me.zhengjie.modules.product.domain.DescriptionItem;
import me.zhengjie.modules.product.domain.InspectionItem;
import me.zhengjie.modules.product.service.dto.DescriptionItemDto;
import me.zhengjie.modules.product.service.dto.DescriptionItemQueryCriteria;
import me.zhengjie.modules.product.service.dto.InspectionItemDto;
import me.zhengjie.modules.product.service.dto.InspectionItemQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface InspectionItemService {
    /**
     * 根据ID查询
     * @param id ID
     * @return /
     */
    InspectionItemDto findById(long id);

    /**
     * 新增检测项
     * @param resources /
     * @return /
     */
    InspectionItemDto create(InspectionItem resources);

    /**
     * 编辑检测项
     * @param resources /
     */
    void update(InspectionItem resources);

    /**
     * 删除描述项
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 查询全部
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    Object queryAll(InspectionItemQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<InspectionItemDto> queryAll(InspectionItemQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<InspectionItemDto> queryAll, HttpServletResponse response) throws IOException;
}
