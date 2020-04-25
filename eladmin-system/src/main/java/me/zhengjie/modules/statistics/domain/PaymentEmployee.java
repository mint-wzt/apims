package me.zhengjie.modules.statistics.domain;

import lombok.Data;
import me.zhengjie.modules.system.domain.Dept;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * (PaymentEmployee)实体类
 *
 * @author makejava
 * @since 2020-04-25 19:00:09
 */
@Data
@Entity
public class PaymentEmployee implements Serializable {
    /**
    * ID
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(groups = PaymentEmployee.Update.class)
    private Long id;
    /**
    * 发放酬金表ID
    */
    @OneToOne
    @JoinColumn(name = "payment_id")
    private RemunerationPayment payment;
    /**
    * 酬金表名
    */
    private String paymentName;
    /**
    * 员工姓名
    */
    private String employeeName;
    /**
    * 发放酬金
    */
    private Double paymentAmount;
    /**
    * 发放日期
    */
    private Timestamp paymentTime;

    public @interface Update {}


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PaymentEmployee statistics = (PaymentEmployee) o;
        return Objects.equals(id, statistics.id) &&
                Objects.equals(employeeName, statistics.employeeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employeeName);
    }

}
