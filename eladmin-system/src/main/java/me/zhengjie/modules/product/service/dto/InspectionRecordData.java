package me.zhengjie.modules.product.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class InspectionRecordData implements Serializable {

    /**
     * 检测记录
     */
    private InspectionRecordDto inspectionRecord;

    /**
     * 检测模板
     */
    private InspectionTemplateSmallDto inspectionTemplate;

    /**
     * 检测项和值
     */
    private Set<InspectionItemSmallDto> inspectionItems;

    /**
     * 检测方法
     */
    private String inspectMethod;
}
