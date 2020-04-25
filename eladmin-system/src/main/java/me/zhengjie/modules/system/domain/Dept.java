package me.zhengjie.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

/**
* @author Zheng Jie
* @date 2019-03-25
*/
@Entity
@Getter
@Setter
@Table(name="dept")
public class Dept implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(groups = Update.class)
    private Long id;

    @Column(name = "name",nullable = false)
    @NotBlank
    private String name;

    @NotNull
    private Boolean enabled;

    @Column(name = "pid",nullable = false)
    private Long pid;

    @JsonIgnore
    @ManyToMany(mappedBy = "depts")
    private Set<Role> roles;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "dept_type")
    private Integer deptType;

    @Column(name = "address")
    private String address;

    @Column(name = "code")
    private String code;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "remark")
    private String remark;

    @Column(name = "create_uid")
    private Long createUid;

    @Column(name = "create_time")
    @CreationTimestamp
    private Timestamp createTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    private Timestamp updateTime;

    @OneToMany(mappedBy = "dept")
    private Set<Employee> employees;

    public @interface Update {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dept dept = (Dept) o;
        return Objects.equals(id, dept.id) &&
                Objects.equals(name, dept.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
