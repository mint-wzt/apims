package me.zhengjie.modules.statistics.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class IndustryStatisticsDto implements Serializable {

    /**
     * 种植面积
     */
    private Double plantingArea;

    /**
     * 养殖数量
     */
    private Double breedingNumber;


    /**
     * 组织机构
     */
    private Double enterpriseNumber;

    /**
     * 从业人数
     */
    private Double employeeNumber;

    /**
     * 种植业产品数
     */
    private Double planting;

    /**
     * 畜牧业产品数
     */
    private Double animalHusbandry;


    /**
     * 渔业产品数
     */
    private Double fishery;

}
