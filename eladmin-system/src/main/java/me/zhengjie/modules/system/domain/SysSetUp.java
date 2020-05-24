package me.zhengjie.modules.system.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * (SysSetUp)实体类
 *
 * @author makejava
 * @since 2020-04-25 15:33:11
 */
@Data
@Entity
public class SysSetUp implements Serializable {
    /**
    * Id
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = SysSetUp.Update.class)
    private Long id;
    /**
    * 系统名称
    */
    private String systemName;
    /**
    * 系统头像
    */
    private String systemLogo;

    /**
     * 图片地址
     */
    private String logoPath;
    /**
    * 区域
    */
    private String region;

    /**
    * 版权
    */
    private String copyright;
    /**
    * 备案号
    */
    private String caseNumber;
    /**
    * 所属企业
    */
    private String enterprise;
    /**
    * 拥有者
    */
    private String owner;
    /**
    * 描述
    */
    private String description;
    /**
    * 状态
    */
    private Integer sysStatus;
    /**
    * 创建日期
    */
    @CreationTimestamp
    private Timestamp createTime;

    public @interface Update {}
}
