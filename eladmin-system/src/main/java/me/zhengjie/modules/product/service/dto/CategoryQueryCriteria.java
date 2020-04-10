package me.zhengjie.modules.product.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Zheng Jie
 * 公共查询类
 */
@Data
public class CategoryQueryCriteria implements Serializable {
    @Query(blurry = "name,code")
    private String blurry;

    @Query
    private Integer enabled;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
