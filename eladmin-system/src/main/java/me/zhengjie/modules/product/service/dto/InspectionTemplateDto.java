package me.zhengjie.modules.product.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.modules.system.service.dto.UserSmallDto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@Data
public class InspectionTemplateDto implements Serializable {
    /**
     * ID
     */
    private Long id;
    /**
     * 模板名称
     */
    private String name;
    /**
     * 类别
     */
    private String itType;
    /**
     * 使用范围
     */
    private String scope;
    /**
     * 使用者ID
     */
    @ApiModelProperty(hidden = true)
    private UserSmallDto user;
    /**
     * 产品ID
     */
    @ApiModelProperty(hidden = true)
    private ProductSmallDto product;

    @ApiModelProperty(hidden = true)
    private Set<InspectionItemSmallDto> inspectionItems;

    /**
     * 是否启用
     */
    private Integer enabled;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Timestamp createTime;
}
