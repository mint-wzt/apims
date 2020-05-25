package me.zhengjie.modules.release.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
public class SalesReleaseQueryCriteria implements Serializable {
    @Query(type = Query.Type.IN, propName = "deptId")
    private Set<Long> deptIds;

    @Query(blurry = "productName,categoryName")
    private String blurry;

    private Long deptId;

    @Query
    private String categoryName;

    @Query
    private String productName;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> releaseDate;

    @Query
    private Integer releaseStatus;
}
