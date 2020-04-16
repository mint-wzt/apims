package me.zhengjie.modules.product.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class InspectionTemplateSmallDto implements Serializable {
    /**
     * ID
     */
    private Long id;
    /**
     * 模板名称
     */
    private String name;
}
