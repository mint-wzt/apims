package me.zhengjie.modules.statistics.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * (MonthPrice)实体类
 *
 * @author makejava
 * @since 2020-05-01 20:44:47
 */
@Data
@Entity
public class MonthPrice implements Serializable {
    /**
    * ID
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(groups = MonthPrice.Update.class)
    private Long id;
    /**
    * 行政编码
    */
    private String regionId;
    /**
    * 行政区划
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
    * 价格
    */
    private Double price;
    /**
    * 价格单位
    */
    private String priceUnit;
    /**
    * 采集市场
    */
    private String market;
    /**
    * 年份
    */
    private String year;
    /**
    * 月份
    */
    private String month;
    /**
    * 统计时间
    */
    private String statisticsTime;
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
        MonthPrice market = (MonthPrice) o;
        return Objects.equals(id, market.id) &&
                Objects.equals(statisticsTime, market.statisticsTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, statisticsTime);
    }

}
