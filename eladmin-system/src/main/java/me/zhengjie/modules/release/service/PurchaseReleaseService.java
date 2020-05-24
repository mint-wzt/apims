package me.zhengjie.modules.release.service;

import me.zhengjie.modules.product.domain.SalesRecord;
import me.zhengjie.modules.product.service.dto.SalesRecordDto;
import me.zhengjie.modules.product.service.dto.SalesRecordQueryCriteria;
import me.zhengjie.modules.release.domain.PurchaseRelease;
import me.zhengjie.modules.release.service.dto.PurchaseReleaseQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface PurchaseReleaseService {
    /**
     * 新增
     * @param resources /
     * @return /
     */
    PurchaseRelease create(PurchaseRelease resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(PurchaseRelease resources);

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
    Object queryAll(PurchaseReleaseQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<PurchaseRelease> queryAll(PurchaseReleaseQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<PurchaseRelease> queryAll, HttpServletResponse response) throws IOException;

    void addViews(Long id);
}
