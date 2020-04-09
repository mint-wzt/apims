package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class EmployeeDto implements Serializable {
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
     * 职位
     */
    private String position;
    /**
     * 员工类型
     */
    private String employeeType;
    /**
     * 所属机构ID
     */
    private DeptSmallDto dept;
    /**
     * 联系方式
     */
    private String contact;
    /**
     * 身份证号码
     */
    private String idNumber;
    /**
     * 家庭住址
     */
    private String address;
    /**
     * 健康情况
     */
    private String healthCondition;
    /**
     * 致贫原因
     */
    private String causePoverty;
    /**
     * 帮扶责任人
     */
    private String responsiblePerson;
    /**
     * 文化程度
     */
    private String educationLevel;
    /**
     * 家庭情况
     */
    private String familySituation;
    /**
     * 耕地面积
     */
    private Double cultivatedArea;
}
