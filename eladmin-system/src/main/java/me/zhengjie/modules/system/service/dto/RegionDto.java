package me.zhengjie.modules.system.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Set;

@Data
public class RegionDto implements Serializable {

    private String id;

    private String pid;

    private String name;

    private String extName;

    private String provinceId;

    private String provinceName;

    private String cityId;

    private String cityName;

    private String areaId;

    private String areaName;

    private String townId;

    private String townName;

    @ApiModelProperty(hidden = true)
    private Set<DeptSmallDto> depts;
}
