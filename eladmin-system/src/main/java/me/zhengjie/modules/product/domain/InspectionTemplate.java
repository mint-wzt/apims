package me.zhengjie.modules.product.domain;

import lombok.Data;
import me.zhengjie.modules.system.domain.User;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * (InspectionTemplate)实体类
 *
 * @author makejava
 * @since 2020-04-13 22:24:27
 */
@Data
@Entity
public class InspectionTemplate implements Serializable {
    /**
    * ID
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = InspectionTemplate.Update.class)
    private Long id;
    /**
    * 模板名称
    */
    private String name;
    /**
    * 类别
    */
    private String itType;
    /**
    * 使用范围
    */
    private String scope;
    /**
    * 使用者ID
    */
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    /**
    * 产品ID
    */
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToMany
    @JoinTable(name = "inspection_template_item", joinColumns = {@JoinColumn(name = "inspection_template_id",referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "inspection_item_id",referencedColumnName = "id")})
    private Set<InspectionItem> inspectionItems;

    /**
    * 是否启用
    */
    private Integer enabled;
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

}
