package me.zhengjie.modules.system.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
* @author Zheng Jie
* @date 2019-03-25
*/
@Data
public class DeptDto implements Serializable {

    private Long id;

    private String name;

    private String address;

    private Integer deptType;

    @NotNull
    private Boolean enabled;

    private Long pid;

    private RegionSmallDto region;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DeptDto> children;

    @ApiModelProperty(hidden = true)
    Set<EmployeeSmallDto> employees;

    private Timestamp createTime;

    public String getLabel() {
        return name;
    }
}
