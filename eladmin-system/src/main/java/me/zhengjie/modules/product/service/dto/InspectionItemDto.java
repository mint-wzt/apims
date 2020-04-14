package me.zhengjie.modules.product.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;


@Data
public class InspectionItemDto implements Serializable {
    /**
     * ID
     */
    private Long id;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 单位
     */
    private String unit;
    /**
     * 比较方向
     */
    private String compareDirection;
    /**
     * 检出类型
     */
    private String checkoutType;
    /**
     * 参考下限
     */
    private Double lowerLimit;
    /**
     * 下限比较符
     */
    private String lowerLimitComparator;
    /**
     * 参考上限
     */
    private Double upperLimit;
    /**
     * 上限比较符
     */
    private String upperLimitComparator;

    /**
     * 参考值1
     */
    private String referenceValue1;
    /**
     * 参考值2
     */
    private String referenceValue2;
    /**
     * 参考值3
     */
    private String referenceValue3;
    /**
     * 分类代码
     */
    private String classificationCode;
    /**
     * 分类名称
     */
    private String classificationName;
    /**
     * 版本号
     */
    private String version;
    /**
     * 备用1
     */
    private String backup1;
    /**
     * 备用2
     */
    private String backup2;
    /**
     * 备用3
     */
    private String backup3;
    /**
     * 备用4
     */
    private String backup4;
    /**
     * 备用5
     */
    private String backup5;
    /**
     * 创建时间
     */
    private Timestamp createTime;
}
