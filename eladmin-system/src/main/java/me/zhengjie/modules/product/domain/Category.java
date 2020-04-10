package me.zhengjie.modules.product.domain;

import lombok.Data;
import me.zhengjie.modules.system.domain.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
public class Category implements Serializable {
    /**
     * 分类ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Category.Update.class)
    private Long id;
    /**
     * 分类代码
     */
    @NotBlank
    private String code;
    /**
     * 分类名称
     */
    @NotBlank
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 父级ID
     */
    private Long pid;

    private Integer enabled;
    /**
     * 创建时间
     */
    @CreationTimestamp
    private Timestamp createTime;
    /**
     * 创建者ID
     */
    private Long createUid;
    /**
     * 更新时间
     */
    @UpdateTimestamp
    private Timestamp updateTime;
    /**
     * 更新者ID
     */
    private Long updateUid;

    public @interface Update {}
}
