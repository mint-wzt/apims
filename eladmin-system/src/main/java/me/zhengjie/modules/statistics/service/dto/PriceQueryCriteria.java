package me.zhengjie.modules.statistics.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class PriceQueryCriteria implements Serializable {

    @Query
    private String provinceName;

    @Query
    private String productName;

    @Query(blurry = "statisticsTime")
    private String blurry;

    private Timestamp startTime;

    private Timestamp endTime;

    @Query
    private String market;

    @Query(type = Query.Type.BETWEEN)
    private List<String> statisticsTime;
}
