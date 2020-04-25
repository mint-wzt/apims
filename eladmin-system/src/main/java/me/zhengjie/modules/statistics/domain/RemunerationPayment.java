package me.zhengjie.modules.statistics.domain;

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
 * (RemunerationPayment)实体类
 *
 * @author makejava
 * @since 2020-04-25 19:00:33
 */
@Data
@Entity
public class RemunerationPayment implements Serializable {
    /**
    * ID
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(groups = RemunerationPayment.Update.class)
    private Long id;
    /**
    * 名称
    */
    @NotBlank
    private String name;
    /**
    * 发放人
    */
    @NotBlank
    private String distributor;
    /**
    * 发放事由
    */
    private String paymentReason;
    /**
    * 发放时间
    */
    @NotNull
    private Timestamp paymentTime;
    /**
    * 发放总额
    */
    @NotNull
    private Double paymentTotal;
    /**
    * 行政区域ID
    */
    private String regionId;
    /**
    * 行政区域名称
    */
    private String regionName;
    /**
    * 所属机构ID
    */
    @OneToOne
    @JoinColumn(name = "dept_id")
    private Dept dept;
    /**
    * 所属机构名称
    */
    @NotBlank
    private String deptName;
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
        RemunerationPayment statistics = (RemunerationPayment) o;
        return Objects.equals(id, statistics.id) &&
                Objects.equals(regionId, statistics.regionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, regionId);
    }

}
