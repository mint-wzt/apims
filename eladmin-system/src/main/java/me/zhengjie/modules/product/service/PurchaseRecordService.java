package me.zhengjie.modules.product.service;

import me.zhengjie.modules.product.domain.PurchaseRecord;
import me.zhengjie.modules.product.service.dto.PurchaseRecordDto;
import me.zhengjie.modules.product.service.dto.PurchaseRecordQueryCriteria;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.modules.system.service.dto.UserQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface PurchaseRecordService {
    /**
     * 根据ID查询
     * @param id ID
     * @return /
     */
    PurchaseRecordDto findById(long id);

    /**
     * 新增采购记录
     * @param resources /
     * @return /
     */
    PurchaseRecordDto create(PurchaseRecord resources);

    /**
     * 编辑采购记录
     * @param resources /
     */
    void update(PurchaseRecord resources);

    /**
     * 删除采购记录
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 查询全部
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    Object queryAll(PurchaseRecordQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<PurchaseRecordDto> queryAll(PurchaseRecordQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<PurchaseRecordDto> queryAll, HttpServletResponse response) throws IOException;
}
