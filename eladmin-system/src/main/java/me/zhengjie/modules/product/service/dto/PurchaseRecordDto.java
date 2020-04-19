package me.zhengjie.modules.product.service.dto;

import lombok.Data;
import me.zhengjie.modules.system.service.dto.DeptSmallDto;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class PurchaseRecordDto implements Serializable {
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
     * 采购批次
     */
    private String batchNumber;
    /**
     * 采购数量
     */
    private Double purchaseQuantity;
    /**
     * 计数单位
     */
    private String unit;
    /**
     * 原材料名称
     */
    private String goodsName;
    /**
     * 生产者名称
     */
    private String producerName;
    /**
     * 生产者地址
     */
    private String producerAddress;
    /**
     * 生产者联系方式
     */
    private String producerContact;
    /**
     * 采购时间
     */
    private Timestamp purchaseTime;
    /**
     * 所属组织机构
     */
    private DeptSmallDto dept;
    /**
     * 状态
     */
    private Integer recordStatus;
    /**
     * 备注
     */
    private String remark;
}
