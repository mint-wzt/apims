package me.zhengjie.modules.release.service.dto;

import io.swagger.models.auth.In;
import lombok.Data;
import me.zhengjie.annotation.Query;
import org.omg.PortableServer.ServantActivator;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
public class PurchaseReleaseQueryCriteria implements Serializable {

    @Query(type = Query.Type.IN, propName = "deptId")
    private Set<Long> deptIds;

    @Query(blurry = "deptName,productName,categoryName")
    private String blurry;

    private Long deptId;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> releaseDate;

    @Query
    private Integer releaseStatus;
}
