package me.zhengjie.modules.product.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
public class PurchaseRecordQueryCriteria implements Serializable {
    @Query
    private Long id;

    @Query(propName = "id", type = Query.Type.IN, joinName = "dept")
    private Set<Long> deptIds;

    @Query(blurry = "batchNumber,goodsName,producerName,productName")
    private String blurry;

    private Long deptId;

    @Query
    private Integer recordStatus;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> purchaseTime;
}
