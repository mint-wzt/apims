package me.zhengjie.modules.product.domain;

import lombok.Data;
import me.zhengjie.modules.system.domain.Dept;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * (PurchaseRecord)实体类
 *
 * @author makejava
 * @since 2020-04-18 17:19:22
 */
@Data
@Entity
public class PurchaseRecord implements Serializable {
    /**
    * ID
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = PurchaseRecord.Update.class)
    private Long id;
    /**
    * 产品ID
    */
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * 产品名称
     */
    @NotBlank
    private String productName;

    /**
    * 采购批次
    */
    @NotBlank
    private String batchNumber;
    /**
    * 采购数量
    */
    private Double purchaseQuantity;
    /**
    * 计数单位
    */
    @NotBlank
    private String unit;
    /**
    * 原材料名称
    */
    @NotBlank
    private String goodsName;
    /**
    * 生产者名称
    */
    @NotBlank
    private String producerName;
    /**
    * 生产者地址
    */
    @NotBlank
    private String producerAddress;
    /**
    * 生产者联系方式
    */
    @NotBlank
    private String producerContact;
    /**
    * 采购时间
    */
    private Timestamp purchaseTime;
    /**
    * 所属组织机构
    */
    @OneToOne
    @JoinColumn(name = "dept_id")
    private Dept dept;
    /**
    * 状态
    */
    private Integer recordStatus;
    /**
    * 备注
    */
    private String remark;
    /**
    * 创建时间
    */
    @CreationTimestamp
    private Timestamp createTime;

    public @interface Update {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PurchaseRecord template = (PurchaseRecord) o;
        return Objects.equals(id, template.id) &&
                Objects.equals(purchaseTime, template.purchaseTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, purchaseTime);
    }
}
