package me.zhengjie.modules.release.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.release.domain.PurchaseRelease;
import me.zhengjie.modules.release.domain.SalesRelease;
import me.zhengjie.modules.release.repository.PurchaseReleaseRepository;
import me.zhengjie.modules.release.repository.SalesReleaseRepository;
import me.zhengjie.modules.release.service.SalesReleaseService;
import me.zhengjie.modules.release.service.dto.SalesReleaseQueryCriteria;
import me.zhengjie.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@CacheConfig(cacheNames = "sales-release")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SalesReleaseServiceImpl implements SalesReleaseService {

    private final SalesReleaseRepository releaseRepository;

    @Value("${file.product}")
    private String productImageUrl;

    public SalesReleaseServiceImpl(SalesReleaseRepository releaseRepository) {
        this.releaseRepository = releaseRepository;
    }

    @Override
    @CacheEvict
    public SalesRelease create(SalesRelease resources) {
        return releaseRepository.save(resources);
    }

    @Override
    @CacheEvict
    public void update(SalesRelease resources) {
        SalesRelease release = releaseRepository.findById(resources.getId()).orElseGet(SalesRelease::new);
        ValidationUtil.isNull(release.getId(),"SalesRelease","id",resources.getId());

        String[] nullPropertyNames = BeanUtil.getNullPropertyNames(resources);
        BeanUtils.copyProperties(resources,release,nullPropertyNames);
        releaseRepository.save(release);
    }

    @Override
    @CacheEvict
    @Transactional
    public void delete(Set<Long> ids) {
        for (Long id : ids){
            releaseRepository.deleteById(id);
        }
    }

    @Override
    @Cacheable
    public Object queryAll(SalesReleaseQueryCriteria criteria, Pageable pageable) {
        Page<SalesRelease> page = releaseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    @Override
    @Cacheable
    public List<SalesRelease> queryAll(SalesReleaseQueryCriteria criteria) {
        List<SalesRelease> releases = releaseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return releases;
    }

    @Override
    public void download(List<SalesRelease> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SalesRelease release : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("采购品种名称", release.getProductName());
            map.put("品种类型ID", release.getCategoryId());
            map.put("品种类型名称", release.getCategoryName());
            map.put("价格", release.getPrice());
            map.put("价格单位", release.getPriceUnit());
            map.put("起批量", release.getBatchStart());
            map.put("发布日期", release.getReleaseDate());
            map.put("浏览次数", release.getViews());
            map.put("发货地址", release.getDeliveryAddress());
            map.put("发布人", release.getPublisher());
            map.put("联系方式", release.getContact());
            map.put("部门名称", release.getDeptName());
            map.put("备注", release.getRemark());
            map.put("状态",release.getReleaseStatus());
            map.put("更新时间", release.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void addViews(Long id) {
        SalesRelease purchaseRelease = releaseRepository.findById(id).get();
        purchaseRelease.setViews(purchaseRelease.getViews()+1);
        releaseRepository.save(purchaseRelease);
    }

    @Override
    public Object updateProductImage(MultipartFile multipartFile) {
        File file = FileUtil.upload(multipartFile, productImageUrl);
        assert file != null;
        Map<String,String> map = new HashMap<>();
        map.put("imageName",file.getName());
        return map;
    }
}
