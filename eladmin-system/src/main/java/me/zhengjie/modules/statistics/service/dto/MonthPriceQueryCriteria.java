package me.zhengjie.modules.statistics.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class MonthPriceQueryCriteria implements Serializable {

    @Query
    private String regionName;

    @Query
    private String productName;

    private Timestamp startTime;

    private Timestamp endTime;

    @Query
    private String statisticsYear;

    @Query
    private String market;

    private String startMarket;

    private String endMarket;

    @Query(type = Query.Type.BETWEEN)
    private List<String> statisticsTime;
}
