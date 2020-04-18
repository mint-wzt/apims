package me.zhengjie.modules.product.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;
import java.util.Set;

@Data
public class InspectionRecordTemplateQueryCriteria implements Serializable {
    @Query
    private Long id;

    @Query(propName = "id", type = Query.Type.IN, joinName = "dept")
    private Set<Long> deptIds;

    @Query(blurry = "inspectionTemplateName,inspectionItemName,inspectMethod")
    private String blurry;

    private Long deptId;
}
