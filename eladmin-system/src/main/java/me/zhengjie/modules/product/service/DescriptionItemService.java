package me.zhengjie.modules.product.service;

import me.zhengjie.modules.product.domain.DescriptionItem;
import me.zhengjie.modules.product.service.dto.DescriptionItemDto;
import me.zhengjie.modules.product.service.dto.DescriptionItemQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface DescriptionItemService {
    /**
     * 根据ID查询
     * @param id ID
     * @return /
     */
    DescriptionItemDto findById(long id);

    /**
     * 新增描述项
     * @param resources /
     * @return /
     */
    DescriptionItemDto create(DescriptionItem resources);

    /**
     * 编辑描述项
     * @param resources /
     */
    void update(DescriptionItem resources);

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
    Object queryAll(DescriptionItemQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<DescriptionItemDto> queryAll(DescriptionItemQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<DescriptionItemDto> queryAll, HttpServletResponse response) throws IOException;
}
