package me.zhengjie.modules.statistics.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * (ProductStatistics)实体类
 *
 * @author makejava
 * @since 2020-04-22 20:53:28
 */
@Data
@Entity
public class ProductStatistics implements Serializable {
    /**
    * ID
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(groups = ProductStatistics.Update.class)
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
    * 统计项目
    */
    private String statisticsItem;
    /**
    * 统计总量
    */
    private Double statisticsTotal;
    /**
    * 计量单位
    */
    private String unit;
    /**
    * 统计人ID
    */
    private Long createUid;
    /**
    * 统计人
    */
    private String createUsername;
    /**
    * 统计时间
    */
    private Timestamp statisticsTime;


    public @interface Update {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductStatistics statistics = (ProductStatistics) o;
        return Objects.equals(id, statistics.id) &&
                Objects.equals(regionId, statistics.regionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, regionId);
    }

}
