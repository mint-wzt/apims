package me.zhengjie.modules.product.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.product.domain.Product;
import me.zhengjie.modules.product.domain.ProductData;
import me.zhengjie.modules.product.repository.ProductDataRepository;
import me.zhengjie.modules.product.repository.ProductRepository;
import me.zhengjie.modules.product.service.ProductDataService;
import me.zhengjie.modules.product.service.dto.ProductDataDto;
import me.zhengjie.modules.product.service.dto.ProductDataQueryCriteria;
import me.zhengjie.modules.product.service.mapper.ProductDataMapper;
import me.zhengjie.modules.statistics.domain.IndustryStatistics;
import me.zhengjie.modules.statistics.domain.ProductStatistics;
import me.zhengjie.modules.statistics.domain.SalesStatistics;
import me.zhengjie.modules.statistics.service.IndustryStatisticsService;
import me.zhengjie.modules.statistics.service.ProductStatisticsService;
import me.zhengjie.modules.statistics.service.SalesStatisticsService;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.repository.DeptRepository;
import me.zhengjie.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@CacheConfig(cacheNames = "product-data")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProductDataServiceImpl implements ProductDataService {

    @Autowired
    private ProductDataRepository dataRepository;

    @Autowired
    private ProductDataMapper dataMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DeptRepository deptRepository;

    @Autowired
    private IndustryStatisticsService statisticsService;

    @Autowired
    private SalesStatisticsService salesStatisticsService;

    @Override
    @Cacheable
    public ProductDataDto findById(long id) {
        ProductData productData = dataRepository.findById(id).orElseGet(ProductData::new);
        ValidationUtil.isNull(productData.getId(), "product-data", "id", id);
        return dataMapper.toDto(productData);
    }

    @Override
    public List<ProductData> findByPid(Long pid) {
        return dataRepository.findByPid(pid);
    }

    @Override
    public ProductData findOne(Long id) {
        return dataRepository.findById(id).orElseGet(ProductData::new);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ProductDataDto create(ProductData resources) {
        Product product = productRepository.findById(resources.getProduct().getId()).orElseGet(Product::new);
        ValidationUtil.isNull(product.getId(), "Product", "id", resources.getProduct().getId());

        List<ProductData> productDatas = dataRepository.findByProductAndPid(product, 0L);
        // 如果是0，查询父类是否已存在
        if (resources.getDataStatus() == 0 && productDatas.size() != 0) {
            throw new EntityExistException(ProductData.class, "product", resources.getProductName());
        }
        if (resources.getDataStatus() == 0 && productDatas.size() == 0) {
            resources.setPid(0l);
        }
        // 如果是1，查询父类是否存在
        if (resources.getDataStatus() == 1 && productDatas.size() == 0) {
            throw new BadRequestException("请先添加产品信息");
        }
        if (resources.getDataStatus() == 1 && productDatas.size() != 0) {
            resources.setPid(productDatas.get(0).getId());
        }

        Dept dept = deptRepository.findById(resources.getDept().getId()).get();
        // 产品信息
        if (resources.getDataStatus() == 0){

            // 添加农产品产业数量
            statisticsService.create(getIndustryStatistics(dept,resources,"种",1d,resources.getProductType()));

            // 添加种植面积、
            if (!StringUtils.isBlank(resources.getAreaUnit())){
                // 添加农产品产业数量
                statisticsService.create(getIndustryStatistics(dept,resources,"亩",resources.getArea(),"种植面积"));
            }else {
                // 添加农产品养殖数量
                statisticsService.create(getIndustryStatistics(dept,resources,"条（只）",resources.getCount(),"养殖数量"));
            }
        }

        // 添加产量
        SalesStatistics salesStatistics = new SalesStatistics();
        salesStatistics.setProductCode(resources.getProductType());
        salesStatistics.setProductName(resources.getProductName());
        salesStatistics.setRegionId(dept.getRegion().getId());
        salesStatistics.setRegionName(dept.getRegion().getExtName());
        salesStatistics.setOutput(resources.getOutput());
        salesStatistics.setOutputUnit(resources.getOutputUnit());
        salesStatistics.setStatisticsTime(DateUtils.getYearAndMonthByTimeStamp(resources.getManufactureDate()));
        salesStatisticsService.create(salesStatistics);

        return dataMapper.toDto(dataRepository.save(resources));
    }

    // 创建IndustryStatistics实体类
    public IndustryStatistics getIndustryStatistics(Dept dept, ProductData productData, String unit, Double total, String item){
        IndustryStatistics statistics = new IndustryStatistics();
        statistics.setRegionId(dept.getRegion().getId());
        statistics.setRegionName(dept.getRegion().getExtName());
        statistics.setStatisticsTime(productData.getManufactureDate());
        statistics.setStatisticsItem(item);
        statistics.setStatisticsTotal(total);
        statistics.setUnit(unit);
        return statistics;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ProductData resources) {
        ProductData productData = dataRepository.findById(resources.getId()).orElseGet(ProductData::new);
        ValidationUtil.isNull(productData.getId(), "ProductData", "id", resources.getId());

        if (productData.getDataStatus() != resources.getDataStatus() || productData.getProduct().getId() != resources.getProduct().getId()) {
            throw new BadRequestException("产品信息与生产信息不能修改");
        }

        productData.setBatchNumber(resources.getBatchNumber());
        productData.setDept(resources.getDept());
        productData.setProduct(resources.getProduct());
        productData.setProductType(resources.getProductType());
        productData.setProductName(resources.getProductName());
        productData.setArea(resources.getArea());
        productData.setAreaUnit(resources.getAreaUnit());
        productData.setCount(resources.getCount());
        productData.setCountUnit(resources.getCountUnit());
        productData.setRemark(resources.getRemark());
        productData.setDataStatus(resources.getDataStatus());
        productData.setDeptName(resources.getDeptName());
        productData.setManufactureDate(resources.getManufactureDate());
        productData.setOutput(resources.getOutput());
        productData.setOutputUnit(resources.getOutputUnit());
        productData.setMaterialBatch(resources.getMaterialBatch());

        dataRepository.save(productData);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<ProductData> categoriesSet) {
        for (ProductData data : categoriesSet) {
            dataRepository.deleteById(data.getId());
        }
    }

    @Override
    public Map<String, Object> buildTree(List<ProductDataDto> productDataDtos) {
        List<ProductDataDto> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (ProductDataDto productDataDto : productDataDtos) {
            if (productDataDto.getPid() == 0) {
                trees.add(productDataDto);
            }
            for (ProductDataDto it : productDataDtos) {
                if (it.getPid().equals(productDataDto.getId())) {
                    if (productDataDto.getChildren() == null) {
                        productDataDto.setChildren(new ArrayList<>());
                    }
                    productDataDto.getChildren().add(it);
                    ids.add(it.getId());
                }
            }
        }
        Map<String, Object> map = new HashMap<>(2);
        if (trees.size() == 0) {
            trees = productDataDtos.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        map.put("content", trees);
        map.put("totalElements", productDataDtos.size());
        log.info("" + productDataDtos.size());
        return map;
    }

    @Override
    public Set<ProductData> getDeleteProductData(List<ProductData> productDataList, Set<ProductData> productDataSet) {
        // 递归找出待删除的菜单
        for (ProductData productData : productDataList) {
            productDataSet.add(productData);
            List<ProductData> productDataList1 = dataRepository.findByPid(productData.getId());
            if (productDataList1 != null && productDataList1.size() != 0) {
                getDeleteProductData(productDataList1, productDataSet);
            }
        }
        return productDataSet;
    }


    @Override
    @Cacheable
    public List<ProductDataDto> queryAll(ProductDataQueryCriteria criteria) {
        return dataMapper.toDto(dataRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public void download(List<ProductDataDto> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ProductDataDto productDataDto : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("产品名称", productDataDto.getProduct().getName());
            map.put("产品代码", productDataDto.getProduct().getCode());
            map.put("生产批次", productDataDto.getBatchNumber());
            map.put("原料批次", productDataDto.getMaterialBatch());
            map.put("生产日期", productDataDto.getManufactureDate());
            map.put("种植面积", productDataDto.getArea() + productDataDto.getAreaUnit());
            map.put("养殖数量", productDataDto.getCount() + productDataDto.getCountUnit());
            map.put("产量", productDataDto.getOutput() + productDataDto.getOutputUnit());
            map.put("组织机构名称", productDataDto.getDept().getName());
            map.put("产品类型", productDataDto.getProductType());
            map.put("备注", productDataDto.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
