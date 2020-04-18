package me.zhengjie.modules.product.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class InspectionItemSmallDto implements Serializable {
    /**
     * ID
     */
    private Long id;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;

    /**
     * 检测项对应的值
     */
    private Double itemValue;

    /**
     * 单位
     */
    private String unit;
}
