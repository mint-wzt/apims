package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserSmallDto implements Serializable {
    private Long id;

    private String username;

    private DeptSmallDto dept;
}
