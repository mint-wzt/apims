package me.zhengjie.modules.release.service;

import me.zhengjie.modules.release.domain.PurchaseRelease;
import me.zhengjie.modules.release.domain.SalesRelease;
import me.zhengjie.modules.release.service.dto.PurchaseReleaseQueryCriteria;
import me.zhengjie.modules.release.service.dto.SalesReleaseQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface SalesReleaseService {
    /**
     * 新增
     * @param resources /
     * @return /
     */
    SalesRelease create(SalesRelease resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(SalesRelease resources);

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
    Object queryAll(SalesReleaseQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<SalesRelease> queryAll(SalesReleaseQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<SalesRelease> queryAll, HttpServletResponse response) throws IOException;

    void addViews(Long id);

    Object updateProductImage(MultipartFile multipartFile);
}
