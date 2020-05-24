package me.zhengjie.modules.release.domain;

import lombok.Data;
import me.zhengjie.modules.system.domain.Dept;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * (PurchaseRelease)实体类
 *
 * @author makejava
 * @since 2020-05-22 20:59:14
 */

@Data
@Entity
public class PurchaseRelease implements Serializable {
    /**
    * ID
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = PurchaseRelease.Update.class)
    private Long id;
    /**
    * 采购品种名称
    */
    @NotBlank
    private String productName;
    /**
    * 品种ID
    */
    private Long categoryId;
    /**
    * 品种类型
    */
    @NotBlank
    private String categoryName;
    /**
    * 采购数量
    */
    @NotBlank
    private String purchaseQuantity;
    /**
    * 数量单位
    */
    private String quantityUnit;
    /**
    * 规格品质
    */
    private String specification;
    /**
    * 发布日期
    */
    @CreationTimestamp
    private Timestamp releaseDate;
    /**
    * 浏览次数
    */
    private Long views;
    /**
    * 收获地址
    */
    @NotBlank
    private String receiptAddress;
    /**
    * 期望货源
    */
    private String supplyAddress;
    /**
    * 发布人
    */
    @NotBlank
    private String publisher;
    /**
    * 联系方式
    */
    @NotBlank
    private String contact;
    /**
    * 部门ID
    */
    private Long deptId;
    /**
    * 部门名称
    */
    private String deptName;
    /**
    * 备注
    */
    private String remark;
    /**
     * 状态
     */
    private Integer releaseStatus;
    /**
    * 更新时间
    */
    @UpdateTimestamp
    private Timestamp updateTime;
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
        PurchaseRelease purchaseRelease = (PurchaseRelease) o;
        return Objects.equals(id, purchaseRelease.id) &&
                Objects.equals(productName, purchaseRelease.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName);
    }
}
