package me.zhengjie.modules.product.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategorySmallDto implements Serializable {

    private Long id;
    /**
     * 分类代码
     */
    private String code;
    /**
     * 分类名称
     */
    private String name;

}
