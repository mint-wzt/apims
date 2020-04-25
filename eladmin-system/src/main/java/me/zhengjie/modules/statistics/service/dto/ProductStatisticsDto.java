package me.zhengjie.modules.statistics.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductStatisticsDto implements Serializable {

    /**
     * 粮油
     */
    private Double grainNumber;

    /**
     * 果品
     */
    private Double fruitNumber;

    /**
     * 蔬菜
     */
    private Double vegetablesNumber;

    /**
     * 畜产品
     */
    private Double livestockNumber;

    /**
     * 水产品
     */
    private Double aquaticNumber;

}
