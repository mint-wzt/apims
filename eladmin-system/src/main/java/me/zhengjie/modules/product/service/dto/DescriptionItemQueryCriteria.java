package me.zhengjie.modules.product.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class DescriptionItemQueryCriteria implements Serializable {

    @Query(blurry = "chineseName,code")
    private String blurry;

    @Query(propName = "id", joinName = "category")
    private Long categoryId;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
