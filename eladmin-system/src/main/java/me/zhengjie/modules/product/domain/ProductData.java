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
 * (ProductData)实体类
 *
 * @author makejava
 * @since 2020-04-18 17:19:22
 */
@Data
@Entity
public class ProductData implements Serializable {
    /**
    * ID
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = ProductData.Update.class)
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
    * 生产批次
    */
    private String batchNumber;

    /**
     * 父级ID
     */
    private Long pid;

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
    @OneToOne
    @JoinColumn(name = "dept_id")
    private Dept dept;
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
        ProductData productData = (ProductData) o;
        return Objects.equals(id, productData.id) &&
                Objects.equals(productName, productData.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName);
    }
}
