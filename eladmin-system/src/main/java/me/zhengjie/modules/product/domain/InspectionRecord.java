package me.zhengjie.modules.product.domain;

import lombok.Data;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.User;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * (InspectionRecord)实体类
 *
 * @author makejava
 * @since 2020-04-13 22:26:02
 */
@Data
@Entity
public class InspectionRecord implements Serializable {
    /**
    * ID
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = InspectionRecord.Update.class)
    private Long id;
    /**
    * 编号
    */
    private String code;
    /**
    * 检测批次
    */
    private String batchNumber;
    /**
    * 检测员
    */
    private String inspector;
    /**
    * 检测日期
    */
    private Timestamp inspectTime;
    /**
    * 是否通过
    */
    private Integer isPassed;
    /**
    * 产品ID
    */
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    /**
    * 所属机构ID
    */
    @OneToOne
    @JoinColumn(name = "dept_id")
    private Dept dept;
    /**
    * 是否提交
    */
    private Integer isSubmit;
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
        InspectionRecord record = (InspectionRecord) o;
        return Objects.equals(id, record.id) &&
                Objects.equals(code, record.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code);
    }

}
