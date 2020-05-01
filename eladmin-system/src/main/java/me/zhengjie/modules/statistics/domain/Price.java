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
 * (Price)实体类
 *
 * @author makejava
 * @since 2020-05-01 11:20:17
 */
@Data
@Entity
public class Price implements Serializable {
    /**
    * ID
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(groups = Price.Update.class)
    private Long id;
    /**
    * 农产品ID
    */
    private Long productId;
    /**
    * 产品编码
    */
    private String productCode;
    /**
    * 产品名称
    */
    private String productName;
    /**
    * 产品编码
    */
    private String categoryCode;
    /**
    * 分类名称
    */
    private String categoryName;
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
    * 发布时间
    */
    private String publishTime;
    /**
    * 省市
    */
    private String provinceName;
    /**
    * 采集城市
    */
    private String cityName;
    /**
    * 采集县
    */
    private String areaName;
    /**
    * 采集镇
    */
    private String townName;
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
        Price price = (Price) o;
        return Objects.equals(id, price.id) &&
                Objects.equals(price, price.price)&&
                Objects.equals(productName, price.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName);
    }

}
