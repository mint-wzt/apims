package me.zhengjie.modules.product.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
public class InspectionRecordQueryCriteria implements Serializable {

    @Query
    private Long id;

    @Query(propName = "id", type = Query.Type.IN, joinName = "dept")
    private Set<Long> deptIds;

    @Query(blurry = "code,batchNumber,inspector")
    private String blurry;

    @Query
    private Integer isPassed;

    private Long deptId;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> inspectTime;

}
