package me.zhengjie.modules.statistics.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
public class IndustryStatisticsQueryCriteria implements Serializable {

    @Query
    private Long id;

    @Query(propName = "id", type = Query.Type.IN, joinName = "region")
    private Set<String> regionIds;

    @Query
    private String regionName;

    @Query(blurry = "statisticsItem")
    private String blurry;

    @Query
    private String regionId;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> statisticsTime;
}
