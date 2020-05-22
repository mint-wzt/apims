package me.zhengjie.modules.statistics.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
public class ProductStatisticsQueryCriteria implements Serializable {
    @Query
    private Long id;

    @Query(type = Query.Type.IN)
    private List<String> statisticsItem;

    @Query
    private String regionName;

    @Query(blurry = "statisticsItem")
    private String blurry;


    private String category;

    @Query
    private String productName;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> statisticsTime;
}
