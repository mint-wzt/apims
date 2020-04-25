package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeSmallDto implements Serializable {
    /**
     * ID
     */
    private Long id;
    /**
     * 编号
     */
    private String number;
    /**
     * 姓名
     */
    private String name;

    private String sex;

    /**
     * 家庭住址
     */
    private String address;
}
