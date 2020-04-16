package me.zhengjie.modules.product.service;

import me.zhengjie.modules.product.domain.InspectionTemplate;
import me.zhengjie.modules.product.service.dto.InspectionTemplateDto;
import me.zhengjie.modules.product.service.dto.InspectionTemplateQueryCriteria;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.modules.system.service.dto.UserQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface InspectionTemplateService {
    /**
     * 根据ID查询
     * @param id ID
     * @return /
     */
    InspectionTemplateDto findById(long id);

    /**
     * 新增模板
     * @param resources /
     * @return /
     */
    InspectionTemplateDto create(InspectionTemplate resources);

    /**
     * 编辑模板
     * @param resources /
     */
    void update(InspectionTemplate resources);

    /**
     * 删除模板
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 查询全部
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    Object queryAll(InspectionTemplateQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<InspectionTemplateDto> queryAll(InspectionTemplateQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<InspectionTemplateDto> queryAll, HttpServletResponse response) throws IOException;

}
