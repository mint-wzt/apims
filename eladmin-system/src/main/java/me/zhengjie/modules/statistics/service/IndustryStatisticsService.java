package me.zhengjie.modules.statistics.service;

import me.zhengjie.modules.product.domain.InspectionItem;
import me.zhengjie.modules.product.service.dto.InspectionItemDto;
import me.zhengjie.modules.product.service.dto.InspectionItemQueryCriteria;
import me.zhengjie.modules.statistics.domain.IndustryStatistics;
import me.zhengjie.modules.statistics.service.dto.IndustryStatisticsDto;
import me.zhengjie.modules.statistics.service.dto.IndustryStatisticsQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IndustryStatisticsService {

    /**
     * 查询
     * @param regionId
     * @param statisticsItem
     * @return
     */
    IndustryStatistics findIndustryStatistics(String regionId,String statisticsItem);

    /**
     * 新增
     * @param resources /
     * @return /
     */
    IndustryStatistics create(IndustryStatistics resources);

    /**
     * 查询某个地区的数据
     * @param criteria
     * @return
     */
    IndustryStatisticsDto get(IndustryStatisticsQueryCriteria criteria);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<IndustryStatistics> queryAll(IndustryStatisticsQueryCriteria criteria);

}
