package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * 公共查询类
 */
@Data
public class EmployeeQueryCriteria {
    @Query
    private Long id;

    @Query(blurry = "name,address,")
    private String blurry;

    @Query(propName = "id", type = Query.Type.IN, joinName = "dept")
    private Set<Long> deptIds;

    private Long deptId;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
