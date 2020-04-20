package me.zhengjie.modules.product.service.impl;

import me.zhengjie.exception.BadRequestException;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.product.domain.Category;
import me.zhengjie.modules.product.repository.CategoryRepository;
import me.zhengjie.modules.product.service.CategoryService;
import me.zhengjie.modules.product.service.dto.CategoryDto;
import me.zhengjie.modules.product.service.dto.CategoryQueryCriteria;
import me.zhengjie.modules.product.service.mapper.CategoryMapper;
import me.zhengjie.modules.system.domain.Menu;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.dto.MenuDto;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.ValidationUtil;
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
@CacheConfig(cacheNames = "category")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final UserRepository userRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable
    public List<CategoryDto> queryAll(CategoryQueryCriteria criteria) {
        //        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return categoryMapper.toDto(categoryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public CategoryDto findById(long id) {
        Category category = categoryRepository.findById(id).orElseGet(Category::new);
        ValidationUtil.isNull(category.getId(), "category", "id", id);
        return categoryMapper.toDto(category);
    }

    @Override
    @Cacheable(key = "'tree'")
    public Object getCategoryTree(List<Category> categories) {
        List<Map<String, Object>> list = new LinkedList<>();
        categories.forEach(category -> {
                    if (category != null) {
                        List<Category> categoryList = categoryRepository.findByPid(category.getId());
                        Map<String, Object> map = new HashMap<>(16);
                        map.put("id", category.getId());
                        map.put("label", category.getName());
                        if (categoryList != null && categoryList.size() != 0) {
                            map.put("children", getCategoryTree(categoryList));
                        }
                        list.add(map);
                    }
                }
        );
        return list;
    }

    @Override
    @CacheEvict(allEntries = true)
    public CategoryDto create(Category resources) {
        if (categoryRepository.findByNameOrCode(resources.getName(), resources.getCode()).size() > 0) {
            throw new EntityExistException(Menu.class, "name", resources.getName());
        }
        String username = SecurityUtils.getUsername();
        User user = userRepository.findByUsername(username);
        resources.setCreateUid(user.getId());
        return categoryMapper.toDto(categoryRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    public void update(Category resources) {
        if (resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        Category category = categoryRepository.findById(resources.getId()).orElseGet(Category::new);
        ValidationUtil.isNull(category.getId(), "Permission", "id", resources.getId());

        Category category1 = categoryRepository.findByName(resources.getName());
        Category category2 = categoryRepository.findByCode(resources.getCode());

        if ((category1 != null && !category1.getId().equals(category.getId())) || (category2 != null && !category2.getId().equals(category.getId()))) {
            throw new EntityExistException(Category.class, "name", resources.getName());
        }
        String username = SecurityUtils.getUsername();
        User user = userRepository.findByUsername(username);
        category.setUpdateUid(user.getId());
        category.setName(resources.getName());
        category.setCode(resources.getCode());
        category.setDescription(resources.getDescription());
        category.setPid(resources.getPid());
        category.setEnabled(resources.getEnabled());
        categoryRepository.save(category);
    }

    @Override
    public Set<Category> getDeleteCategories(List<Category> categoryList, Set<Category> categorySet) {
        // 递归找出待删除的分类
        for (Category category : categoryList) {
            categorySet.add(category);
            List<Category> categories = categoryRepository.findByPid(category.getId());
            if (categories != null && categories.size() != 0) {
                getDeleteCategories(categories, categorySet);
            }
        }
        return categorySet;
    }

    @Override
    @Cacheable(key = "'pid:'+#p0")
    public List<Category> findByPid(long pid) {
        return categoryRepository.findByPid(pid);
    }

    @Override
    public Map<String, Object> buildTree(List<CategoryDto> categoryDtos) {
        List<CategoryDto> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (CategoryDto categoryDTO : categoryDtos) {
            if (categoryDTO.getPid() == 0) {
                trees.add(categoryDTO);
            }
            for (CategoryDto it : categoryDtos) {
                if (it.getPid().equals(categoryDTO.getId())) {
                    if (categoryDTO.getChildren() == null) {
                        categoryDTO.setChildren(new ArrayList<>());
                    }
                    categoryDTO.getChildren().add(it);
                    it.setPName(categoryDTO.getName());
                    it.setPCode(categoryDTO.getCode());
                    ids.add(it.getId());
                }
            }
        }
        Map<String, Object> map = new HashMap<>(2);
        if (trees.size() == 0) {
            trees = categoryDtos.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        map.put("content", trees);
        map.put("totalElements", categoryDtos.size());
        return map;
    }

    @Override
    public Category findOne(Long id) {
        Category category = categoryRepository.findById(id).orElseGet(Category::new);
        ValidationUtil.isNull(category.getId(), "category", "id", id);
        return category;
    }

    /**
     * 批量删除分类
     *
     * @param categorySet /
     */
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Category> categorySet) {
        for (Category category : categorySet) {
            categoryRepository.deleteById(category.getId());
        }
    }

    /**
     * 导出数据
     *
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException
     */
    @Override
    public void download(List<CategoryDto> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        setParentCategory(queryAll);
        for (CategoryDto categoryDto : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("分类代码", categoryDto.getCode());
            map.put("分类名称", categoryDto.getName());
            map.put("描述", categoryDto.getDescription());
            map.put("状态", categoryDto.getEnabled() == 1 ? "启用" : "停用");
            map.put("父类代码", categoryDto.getParent() == null ? null : categoryDto.getParent().getCode());
            map.put("父类名称", categoryDto.getParent() == null ? null : categoryDto.getParent().getName());
            map.put("创建日期", categoryDto.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 设置父类
     *
     * @param categoryDtos
     */
    @Override
    public void setParentCategory(List<CategoryDto> categoryDtos) {
        for (CategoryDto categoryDto : categoryDtos) {
            if (categoryDto.getPid() == 0) {
                categoryDto.setParent(null);
            }
            for (CategoryDto categoryDto1 : categoryDtos) {
                if (categoryDto1.getPid() == categoryDto.getId()) {
                    categoryDto1.setParent(categoryDto);
                }
            }
        }
    }
}
