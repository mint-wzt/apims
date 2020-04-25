package me.zhengjie.modules.statistics.service.dto;

import lombok.Data;
import me.zhengjie.modules.system.service.dto.DeptSmallDto;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class RemunerationPaymentDto implements Serializable {
    /**
     * ID
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 发放人
     */
    private String distributor;
    /**
     * 发放事由
     */
    private String paymentReason;
    /**
     * 发放时间
     */
    private Timestamp paymentTime;
    /**
     * 发放总额
     */
    private Double paymentTotal;
    /**
     * 行政区域ID
     */
    private String regionId;
    /**
     * 行政区域名称
     */
    private String regionName;
    /**
     * 所属机构ID
     */
    private DeptSmallDto dept;
    /**
     * 所属机构名称
     */
    private String deptName;
    /**
     * 备注
     */
    private String remark;
}
