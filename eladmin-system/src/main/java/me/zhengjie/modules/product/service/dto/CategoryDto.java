package me.zhengjie.modules.product.service.dto;

import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class CategoryDto implements Serializable {

    private Long id;
    /**
     * 分类代码
     */
    private String code;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 父级ID
     */
    private Long pid;

    /**
     * 父类名称
     */
    private String pName;

    /**
     * 父类编码
     */
    private String pCode;
    /**
     * 状态
     */
    private Integer enabled;
    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 子类
     */
    private List<CategoryDto> children;

    /**
     * 父类
     */
    private CategoryDto parent;
}
