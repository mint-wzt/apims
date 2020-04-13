package me.zhengjie.modules.product.service.dto;

import lombok.Data;
import me.zhengjie.modules.system.service.dto.DeptSmallDto;
import me.zhengjie.modules.system.service.dto.RegionSmallDto;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class ProductDto implements Serializable {
    /**
     * 产品ID
     */
    private Long id;
    /**
     * 产品名称
     */
    private String name;
    /**
     * 产品代码
     */
    private String code;
    /**
     * 分类ID
     */
    private CategorySmallDto category;
    /**
     * 类型
     */
    private String productType;
    /**
     * 使用范围
     */
    private String scope;
    /**
     * 启用
     */
    private Integer enabled;
    /**
     * 所属区域ID
     */
    private RegionSmallDto region;
    /**
     * 所属部门ID
     */
    private DeptSmallDto dept;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Timestamp createTime;
}
