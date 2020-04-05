package me.zhengjie.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="region")
public class Region implements Serializable {

    @Id
    @Column(name = "id")
    @NotNull(groups = Update.class)
    private String id;

    @Column(name = "pid")
    private String pid;

    @Column(name = "level")
    private Integer level;

    @Column(name = "name")
    private String name;

    @Column(name = "pinyin")
    private String pinyin;

    @Column(name = "ext_id")
    private String extId;

    @Column(name = "ext_name")
    private String extName;

    @Column(name = "province_id")
    private String provinceId;

    @Column(name = "province_name")
    private String provinceName;

    @Column(name = "city_id")
    private String cityId;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "area_id")
    private String areaId;

    @Column(name = "area_name")
    private String areaName;

    @Column(name = "town_id")
    private String townId;

    @Column(name = "town_name")
    private String townName;

    @JsonIgnore
    @OneToMany(mappedBy = "region")
    private Set<Dept> depts;

    public @interface Update {}

}
