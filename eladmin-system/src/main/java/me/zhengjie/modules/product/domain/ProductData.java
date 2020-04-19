package me.zhengjie.modules.product.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * (ProductData)实体类
 *
 * @author makejava
 * @since 2020-04-18 17:19:22
 */
public class ProductData implements Serializable {
    private static final long serialVersionUID = -68661713801744097L;
    /**
    * ID
    */
    private Long id;
    /**
    * 产品ID
    */
    private Long productId;
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
    private Date manufactureDate;
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
    private Long deptId;
    /**
    * 产品类型
    */
    private String productType;
    /**
    * 备注
    */
    private String remark;
    /**
    * 创建时间
    */
    private Date createTime;
}
