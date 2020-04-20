package me.zhengjie.modules.product.service.dto;

import lombok.Data;
import me.zhengjie.modules.system.service.dto.DeptSmallDto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class ProductDataDto implements Serializable {
    /**
     * ID
     */
    private Long id;
    /**
     * 产品ID
     */
    private ProductSmallDto product;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 父级ID
     */
    private Long pid;
    /**
     * 生产批次
     */
    private String batchNumber;
    /**
     * 原料批次
     */
    private String materialBatch;
    /**
     * 生产日期
     */
    private Timestamp manufactureDate;
    /**
     * 种植面积
     */
    private Double area;
    /**
     * 面积单位
     */
    private String areaUnit;
    /**
     * 养殖数量
     */
    private Double count;
    /**
     * 数量单位
     */
    private String countUnit;
    /**
     * 产量
     */
    private Double output;
    /**
     * 产量单位
     */
    private String outputUnit;
    /**
     * 状态
     */
    private Integer dataStatus;
    /**
     * 所属组织机构
     */
    private DeptSmallDto dept;
    /**
     * 组织机构名称
     */
    private String deptName;
    /**
     * 产品类型
     */
    private String productType;
    /**
     * 备注
     */
    private String remark;

    /**
     * 子类
     */
    private List<ProductDataDto> children;
}
