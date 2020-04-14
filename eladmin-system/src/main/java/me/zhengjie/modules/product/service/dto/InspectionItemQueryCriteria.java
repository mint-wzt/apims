package me.zhengjie.modules.product.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class InspectionItemQueryCriteria implements Serializable {
    @Query
    private Long id;

    @Query(blurry = "name,code")
    private String blurry;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
