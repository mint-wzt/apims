package me.zhengjie.modules.statistics.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
public class SalesStatisticsQueryCriteria implements Serializable {

    @Query
    private String regionName;

    @Query
    private String productName;

    @Query(blurry = "statisticsTime")
    private String blurry;

    private Timestamp timestamps1;
    private Timestamp timestamps2;

    @Query(type = Query.Type.BETWEEN)
    private List<String> statisticsTime;
}
