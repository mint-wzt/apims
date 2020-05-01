package me.zhengjie.modules.statistics.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

@Data
public class MarketQueryCriteria implements Serializable {
    @Query
    private String regionName;
}
