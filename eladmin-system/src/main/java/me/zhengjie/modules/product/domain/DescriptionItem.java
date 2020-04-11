package me.zhengjie.modules.product.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * (DescriptionItem)实体类
 *
 * @author makejava
 * @since 2020-04-11 11:41:07
 */
@Data
@Entity
public class DescriptionItem implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = DescriptionItem.Update.class)
    private Long id;
    /**
     * 编码
     */
    private String code;
    /**
     * 所属类型ID
     */
    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;
    /**
     * 中文名称
     */
    private String chineseName;
    /**
     * 英文名称
     */
    private String englishName;
    /**
     * 标记
     */
    private String mark;
    /**
     * 说明
     */
    private String description;
    /**
     * 数据类型及格式
     */
    private String datatypeFormat;
    /**
     * 值域
     */
    private String dataRange;
    /**
     * 同义词
     */
    private String synonym;
    /**
     * 约束/条件
     */
    private String restrictions;
    /**
     * 计量单位
     */
    private String unit;
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
