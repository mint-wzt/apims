package me.zhengjie.modules.product.service.dto;

import lombok.Data;
import me.zhengjie.modules.system.service.dto.DeptSmallDto;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class InspectionRecordDto implements Serializable {
    /**
     * ID
     */
    private Long id;
    /**
     * 编号
     */
    private String code;
    /**
     * 检测批次
     */
    private String batchNumber;
    /**
     * 检测员
     */
    private String inspector;
    /**
     * 检测日期
     */
    private Timestamp inspectTime;
    /**
     * 是否通过
     */
    private Integer isPassed;
    /**
     * 产品ID
     */
    private ProductSmallDto product;
    /**
     * 所属机构ID
     */
    private DeptSmallDto dept;
    /**
     * 是否提交
     */
    private Integer isSubmit;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Timestamp createTime;
}
