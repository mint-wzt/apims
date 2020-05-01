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
 * (Market)实体类
 *
 * @author makejava
 * @since 2020-05-01 13:29:42
 */
@Data
@Entity
public class Market implements Serializable {
    /**
    * ID
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(groups = Market.Update.class)
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
    * 市场名称
    */
    private String marketName;
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
        Market market = (Market) o;
        return Objects.equals(id, market.id) &&
                Objects.equals(marketName, market.marketName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, marketName);
    }

}
