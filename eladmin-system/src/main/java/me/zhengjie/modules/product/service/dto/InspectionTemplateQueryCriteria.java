package me.zhengjie.modules.product.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class InspectionTemplateQueryCriteria implements Serializable {
    @Query
    private Long id;

    @Query(blurry = "name,itType")
    private String blurry;

    @Query
    private Integer enabled;

    @Query(propName = "id", joinName = "user")
    private Long userId;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
