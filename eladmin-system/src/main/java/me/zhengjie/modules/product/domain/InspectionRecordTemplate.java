package me.zhengjie.modules.product.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * (InspectionRecordTemplate)实体类
 *
 * @author makejava
 * @since 2020-04-13 22:26:21
 */
@Data
@Entity
public class InspectionRecordTemplate implements Serializable {
    /**
    * ID
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = InspectionRecordTemplate.Update.class)
    private Long id;
    /**
    * 检测记录ID
    */
    private Long inspectionRecordId;
    /**
    * 检测模板ID
    */
    private Long inspectionTemplateId;
    /**
    * 模板名称
    */
    private String inspectionTemplateName;
    /**
    * 检测项目ID
    */
    private Long inspectionItemId;
    /**
    * 检测项目名称
    */
    private String inspectionItemName;
    /**
     * 是否检出
     */
    private Integer isDetected;
    /**
    * 是否超标
    */
    private Integer isExceeded;
    /**
    * 检测方法
    */
    private String inspectMethod;

    public @interface Update {}

}
