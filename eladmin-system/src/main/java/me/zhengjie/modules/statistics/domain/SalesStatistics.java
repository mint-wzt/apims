package me.zhengjie.modules.statistics.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;;
import java.util.Objects;

/**
 * (SalesStatistics)实体类
 *
 * @author makejava
 * @since 2020-04-29 16:10:46
 */
@Data
@Entity
public class SalesStatistics implements Serializable {
    /**
    * ID
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(groups = SalesStatistics.Update.class)
    private Long id;
    /**
    * 行政编码
    */
    private String regionId;
    /**
    * 行政名称
    */
    private String regionName;
    /**
    * 产品编码
    */
    private String productCode;
    /**
    * 产品名称
    */
    private String productName;
    /**
    * 产量
    */
    private Double output;
    /**
    * 产量单位
    */
    private String outputUnit;
    /**
    * 销量
    */
    private Double saleNumber;
    /**
    * 销量单位
    */
    private String saleUnit;
    /**
    * 销售额
    */
    private Double sales;
    /**
    * 销售额单位
    */
    private String salesUnit;
    /**
    * 统计时间
    */
    private String statisticsTime;
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
        SalesStatistics statistics = (SalesStatistics) o;
        return Objects.equals(id, statistics.id) &&
                Objects.equals(regionId, statistics.regionId)&&
                Objects.equals(productName, statistics.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, regionId);
    }

}
