package me.zhengjie.modules.system.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
public class Employee implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Employee.Update.class)
    private Long id;
    /**
     * 编号
     */
    private String number;
    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     * */
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
    @OneToOne
    @JoinColumn(name = "dept_id")
    private Dept dept;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    @CreationTimestamp
    private Timestamp createTime;
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
    /**
     * 对象属性
     */
    private String objectProperty;
    /**
     * 识别标准
     */
    private String identificationStandard;

    public @interface Update {}
}
