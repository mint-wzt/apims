package me.zhengjie.modules.product.domain;

import lombok.Data;
import me.zhengjie.modules.system.domain.Dept;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * (SalesRecord)实体类
 *
 * @author makejava
 * @since 2020-04-21 20:03:36
 */
@Data
@Entity
public class SalesRecord implements Serializable {
    /**
    * ID
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = SalesRecord.Update.class)
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
    @NotBlank
    private String salesArea;
    /**
    * 生产批次
    */
    @NotBlank
    private String batchNumber;
    /**
    * 销量
    */
    @NotNull
    private Double salesNumber;
    /**
    * 计量单位
    */
    @NotBlank
    private String salesUnit;
    /**
    * 单价
    */
    @NotNull
    private Double price;
    /**
    * 价格单位
    */
    @NotBlank
    private String priceUnit;
    /**
    * 销售额
    */
    @NotNull
    private Double sales;
    /**
    * 销售日期
    */
    @NotNull
    private Timestamp salesDate;
    /**
    * 产品ID
    */
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
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
    @OneToOne
    @JoinColumn(name = "dept_id")
    private Dept dept;
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
        SalesRecord salesRecord = (SalesRecord) o;
        return Objects.equals(id, salesRecord.id) &&
                Objects.equals(orderNumber, salesRecord.orderNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderNumber);
    }
}
