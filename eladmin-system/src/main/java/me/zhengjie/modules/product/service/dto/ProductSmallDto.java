package me.zhengjie.modules.product.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductSmallDto implements Serializable {
    /**
     * 产品ID
     */
    private Long id;
    /**
     * 产品名称
     */
    private String name;
    /**
     * 产品代码
     */
    private String code;
}
