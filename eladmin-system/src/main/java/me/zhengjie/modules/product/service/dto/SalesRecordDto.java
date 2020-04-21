package me.zhengjie.modules.product.service.dto;

import lombok.Data;
import me.zhengjie.modules.system.service.dto.DeptSmallDto;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class SalesRecordDto implements Serializable {
    /**
     * ID
     */
    private Long id;
    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 销售区域ID
     */
    private String regionId;
    /**
     * 销售区域
     */
    private String salesArea;
    /**
     * 生产批次
     */
    private String batchNumber;
    /**
     * 销量
     */
    private Double salesNumber;
    /**
     * 计量单位
     */
    private String salesUnit;
    /**
     * 单价
     */
    private Double price;
    /**
     * 价格单位
     */
    private String priceUnit;
    /**
     * 销售额
     */
    private Double sales;
    /**
     * 销售日期
     */
    private Timestamp salesDate;
    /**
     * 产品ID
     */
    private ProductSmallDto product;
    /**
     * 产品编码
     */
    private String productCode;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 部门ID
     */
    private DeptSmallDto dept;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 状态
     */
    private Integer salesStatus;
    /**
     * 备注
     */
    private String remark;
}
