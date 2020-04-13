package me.zhengjie.modules.product.domain;

import lombok.Data;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.Region;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * (Product)实体类
 *
 * @author makejava
 * @since 2020-04-12 17:31:31
 */
@Data
@Entity
public class Product implements Serializable {
    /**
     * 产品ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Product.Update.class)
    private Long id;
    /**
     * 产品名称
     */
    private String name;
    /**
     * 产品代码
     */
    private String code;
    /**
     * 分类ID
     */
    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;
    /**
     * 类型
     */
    private String productType;
    /**
     * 使用范围
     */
    private String scope;
    /**
     * 启用
     */
    private Integer enabled;
    /**
     * 所属区域ID
     */
    @OneToOne
    @JoinColumn(name = "region_id")
    private Region region;
    /**
     * 所属部门ID
     */
    @OneToOne
    @JoinColumn(name = "dept_id")
    private Dept dept;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建人ID
     */
    private Long createUid;
    /**
     * 创建时间
     */
    @CreationTimestamp
    private Timestamp createTime;
    /**
     * 修改者ID
     */
    private Long updateUid;
    /**
     * 修改时间
     */
    @UpdateTimestamp
    private Timestamp updateTime;

    public @interface Update {}
}
