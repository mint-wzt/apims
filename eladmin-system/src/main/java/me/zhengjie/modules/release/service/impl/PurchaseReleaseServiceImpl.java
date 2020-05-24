package me.zhengjie.modules.release.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.release.domain.PurchaseRelease;
import me.zhengjie.modules.release.repository.PurchaseReleaseRepository;
import me.zhengjie.modules.release.service.PurchaseReleaseService;
import me.zhengjie.modules.release.service.dto.PurchaseReleaseQueryCriteria;
import me.zhengjie.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@CacheConfig(cacheNames = "purchase-release")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PurchaseReleaseServiceImpl implements PurchaseReleaseService {

    private final PurchaseReleaseRepository releaseRepository;

    public PurchaseReleaseServiceImpl(PurchaseReleaseRepository releaseRepository) {
        this.releaseRepository = releaseRepository;
    }

    @Override
    public PurchaseRelease create(PurchaseRelease resources) {
        return releaseRepository.save(resources);
    }

    @Override
    public void update(PurchaseRelease resources) {
        PurchaseRelease release = releaseRepository.findById(resources.getId()).orElseGet(PurchaseRelease::new);
        ValidationUtil.isNull(release.getId(),"PurchaseRelease","id",resources.getId());

        String[] nullPropertyNames = BeanUtil.getNullPropertyNames(resources);
        BeanUtils.copyProperties(resources,release,nullPropertyNames);
        releaseRepository.save(release);
    }

    @Override
    public void delete(Set<Long> ids) {
        for (Long id : ids){
            releaseRepository.deleteById(id);
        }
    }

    @Override
    public Object queryAll(PurchaseReleaseQueryCriteria criteria, Pageable pageable) {
        Page<PurchaseRelease> page = releaseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    @Override
    public List<PurchaseRelease> queryAll(PurchaseReleaseQueryCriteria criteria) {
        List<PurchaseRelease> releases = releaseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return releases;
    }

    @Override
    public void download(List<PurchaseRelease> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PurchaseRelease release : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("采购品种名称", release.getProductName());
            map.put("品种类型ID", release.getCategoryId());
            map.put("品种类型名称", release.getCategoryName());
            map.put("采购数量", release.getPurchaseQuantity());
            map.put("数量单位", release.getQuantityUnit());
            map.put("规格品质", release.getSpecification());
            map.put("发布日期", release.getReleaseDate());
            map.put("浏览次数", release.getViews());
            map.put("收获地址", release.getReceiptAddress());
            map.put("期望货源", release.getSupplyAddress());
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
        PurchaseRelease purchaseRelease = releaseRepository.findById(id).get();
        purchaseRelease.setViews(purchaseRelease.getViews()+1);
        releaseRepository.save(purchaseRelease);
    }
}
