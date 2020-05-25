package me.zhengjie.modules.release.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * (SalesRelease)实体类
 *
 * @author makejava
 * @since 2020-05-24 19:08:04
 */
@Data
@Entity
public class SalesRelease implements Serializable {
    /**
    * ID
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = SalesRelease.Update.class)
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
    * 品种图片
    */
    private String productImage;
    /**
    * 价格
    */
    private String price;
    /**
    * 价格单位
    */
    private String priceUnit;
    /**
    * 起批量
    */
    private String batchStart;
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
    * 发货地址
    */
    private String deliveryAddress;
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
        SalesRelease release = (SalesRelease) o;
        return Objects.equals(id, release.id) &&
                Objects.equals(productName, release.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName);
    }

}
