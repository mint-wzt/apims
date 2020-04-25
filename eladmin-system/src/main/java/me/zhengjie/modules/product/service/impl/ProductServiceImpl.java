package me.zhengjie.modules.product.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.product.domain.Product;
import me.zhengjie.modules.product.repository.ProductRepository;
import me.zhengjie.modules.product.service.CategoryService;
import me.zhengjie.modules.product.service.ProductService;
import me.zhengjie.modules.product.service.dto.ProductDataDto;
import me.zhengjie.modules.product.service.dto.ProductDto;
import me.zhengjie.modules.product.service.dto.ProductQueryCriteria;
import me.zhengjie.modules.product.service.mapper.ProductMapper;
import me.zhengjie.modules.statistics.domain.ProductStatistics;
import me.zhengjie.modules.statistics.service.ProductStatisticsService;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.Region;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.DeptRepository;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.dto.DeptDto;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
@CacheConfig(cacheNames = "product")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private DeptRepository deptRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductStatisticsService productStatisticsService;

    @Override
    @Cacheable(key = "#p0")
    public ProductDto findById(long id) {
        Product product = productRepository.findById(id).orElseGet(Product::new);
        ValidationUtil.isNull(product.getId(), "product", "id", id);
        return productMapper.toDto(product);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ProductDto create(Product resources) {
        if (productRepository.findByCodeAndDept(resources.getCode(), resources.getDept()) != null) {
            throw new EntityExistException(Product.class, "code:deptId", resources.getCode() + ":" + resources.getDept());
        }
        Dept dept = deptRepository.findById(resources.getDept().getId()).orElseGet(Dept::new);
        ValidationUtil.isNull(dept.getId(),"dept","id",resources.getDept().getId());
        resources.setRegion(dept.getRegion());

        Product product = productRepository.save(resources);

        // 添加产品统计数据
        ProductStatistics statistics = new ProductStatistics();
        statistics.setRegionId(dept.getRegion().getId());
        statistics.setRegionName(dept.getRegion().getExtName());
        statistics.setStatisticsItem(categoryService.findOne(resources.getCategory().getId()).getName());
        statistics.setStatisticsTime(product.getCreateTime());
        statistics.setStatisticsTotal(1d);
        statistics.setUnit("种（个）");
        productStatisticsService.create(statistics);

        return productMapper.toDto(product);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Product resources) {
        Product product = productRepository.findById(resources.getId()).orElseGet(Product::new);
        ValidationUtil.isNull(product.getId(), "product", "id", resources.getId());


        Product product1 = productRepository.findByCodeAndDept(resources.getCode(), resources.getDept());

        if (product1 != null && !product.getId().equals(product1.getId())) {
            throw new EntityExistException(Product.class, "code:deptId", resources.getCode() + ":" + resources.getDept());
        }

        product.setName(resources.getName());
        product.setCode(resources.getCode());
        product.setDept(resources.getDept());
        product.setRegion(resources.getRegion());
        product.setRemark(resources.getRemark());
        product.setEnabled(resources.getEnabled());
        product.setProductType(resources.getProductType());
        product.setScope(resources.getScope());
        productRepository.save(product);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            productRepository.deleteById(id);
        }
    }

    @Override
    public Object queryAll(ProductQueryCriteria criteria, Pageable pageable) {
        Page<Product> page = productRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(productMapper::toDto));
    }

    @Override
    public List<ProductDto> queryAll(ProductQueryCriteria criteria) {
        List<Product> products = productRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return productMapper.toDto(products);
    }

    @Override
    public void download(List<ProductDto> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ProductDto productDto : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("产品名称", productDto.getName());
            map.put("产品代码", productDto.getCode());
            map.put("产品类型", productDto.getProductType());
            map.put("分类代码", productDto.getCategory().getCode());
            map.put("分类名称", productDto.getCategory().getName());
            map.put("使用范围", productDto.getScope());
            map.put("状态", productDto.getEnabled() == 1 ? "启用" : "禁用");
            map.put("区域编码", productDto.getRegion().getId());
            map.put("所在地区", productDto.getRegion().getExtName());
            map.put("所属组织机构", productDto.getDept().getName());
            map.put("备注", productDto.getRemark());
            map.put("创建日期", productDto.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
