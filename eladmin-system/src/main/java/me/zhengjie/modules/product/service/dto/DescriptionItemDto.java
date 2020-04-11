package me.zhengjie.modules.product.service.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DescriptionItemDto {
    private Long id;
    /**
     * 编码
     */
    private String code;
    /**
     * 所属类型ID
     */
    private CategorySmallDto category;
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
    private Timestamp createTime;
}
