<template>
  <div class="dashboard-container">
    <div class="head-container chart-wrapper" style="padding-top: 20px;padding-left: 20px">
      <div class="chart-wrapper">
        <span><svg-icon icon-class="location" style="width: 30px;height: 30px" /></span>
        <v-region v-model="selectedProvince" :town="true" class="filter-item" @values="regionChange" />
        <el-button :loading="loading1" class="filter-item" size="mini" type="success" icon="el-icon-search" @click="selectByRegion">搜索</el-button>
      </div>
    </div>
    <div class="dashboard-editor-container" :v-loading="false">
      <el-row :gutter="40" class="panel-group">
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-people">
              <svg-icon icon-class="enterprise1" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">组织机构</div>
              <count-to :start-val="0" :end-val="industryStatistics.enterpriseNumber" :duration="2600" class="card-panel-num" /> 个
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-message">
              <svg-icon icon-class="user-group-fill" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">从业人数</div>
              <count-to :start-val="0" :end-val="industryStatistics.employeeNumber" :duration="2600" class="card-panel-num" /> 人
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-money">
              <svg-icon icon-class="plant" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">种植面积</div>
              <count-to :start-val="0" :end-val="industryStatistics.plantingArea" :duration="2600" class="card-panel-num" /> 亩
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-shopping">
              <svg-icon icon-class="animalx" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">养殖数量</div>
              <count-to :start-val="0" :end-val="industryStatistics.breedingNumber" :duration="2600" class="card-panel-num" /> 只
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="32">
        <el-col :xs="24" :sm="24" :lg="12">
          <div class="chart-wrapper">
            <div style="margin-bottom: 35px">
              <el-divider content-position="left">
                <span style="font-size: 20px;color: cornflowerblue">产业分布</span>
              </el-divider>
            </div>
            <div id="industryChart" :style="{height:height,width:width}" />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="12">
          <div class="chart-wrapper">
            <div style="margin-bottom: 35px">
              <el-divider content-position="left">
                <span style="font-size: 20px;color: cornflowerblue">产品分布</span>
              </el-divider>
            </div>
            <div id="productCategory" :style="{height:height,width:width}" />
          </div>
        </el-col>
      </el-row>
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <div class="chart-wrapper" style="margin-left: 15px">
          <div style="margin-bottom: 40px">
            <el-divider content-position="left">
              <span style="font-size: 20px;color: cornflowerblue">销量排行</span>
            </el-divider>
          </div>
          <span>产品：</span>
          <el-input
            v-model="productNameSale"
            clearable
            size="small"
            placeholder="输入产品名称"
            style="width: 200px;"
            class="filter-item"
          />
          <span>时间：</span>
          <el-date-picker
            v-model="productSalesStatisticTime"
            :default-time="['00:00:00','23:59:59']"
            type="monthrange"
            range-separator=":"
            size="small"
            class="date-item"
            value-format="yyyy-MM-dd HH:mm:ss"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
          <span>
            <el-button :loading="loading2" class="filter-item" size="mini" type="success" icon="el-icon-search" @click="searchProductSalesData">搜索</el-button>
          </span>
          <span style="margin-left: 150px">时间：</span>
          <el-date-picker
            v-model="productRankTime"
            :default-time="['00:00:00','23:59:59']"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="month"
            size="small"
            class="date-item"
            placeholder="选择月"
          />
          <span>
            <el-button :loading="loading3" class="filter-item" size="mini" type="success" icon="el-icon-search" @click="searchProductSalesRank">搜索</el-button>
          </span>
        </div>
        <el-col :xs="24" :sm="24" :lg="12">
          <div class="chart-wrapper">
            <div id="productSalesData" :style="{height:height,width:width}" />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="12">
          <div class="chart-wrapper">
            <div id="productSales" :style="{height:height,width:width}" />
          </div>
        </el-col>
      </el-row>
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <el-divider content-position="left">
          <span style="font-size: 20px;color: cornflowerblue">企业分布</span>
        </el-divider>
        <el-col :xs="24" :sm="24" :lg="12">
          <div class="chart-wrapper" style="margin-left: 15px">
            <span>品种：</span>
            <el-select v-model="productType" placeholder="请选择">
              <el-option
                v-for="item in productTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
            <span>
              <el-button :loading="loading4" class="filter-item" size="mini" type="success" icon="el-icon-search" @click="searchByCategory">搜索</el-button>
            </span>
          </div>
          <div class="chart-wrapper">
            <div id="productOfCategory" :style="{height:height,width:width}" />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="12">
          <div class="chart-wrapper">
            <pie-chart style="margin-top: 70px" />
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import PieChart from '@/components/Echarts/PieChart'
import { count } from '@/api/monitor/visits'
import '@/assets/styles/eladmin.scss'
import CountTo from 'vue-count-to'
import { get } from '@/api/statistics/industry-statistics'
import { getProductStatistics, getProductsByCatagory } from '@/api/statistics/product-statistics'
import { getProductSalesStatistics, getProductSalesRank } from '@/api/statistics/sales-statistics'
import { mapGetters } from 'vuex'
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'

/**
 * 记录访问，只有页面刷新或者第一次加载才会记录
 */
count().then(res => {
})
const animationDuration = 6000

export default {
  name: 'Dashboard',
  components: {
    CountTo,
    PieChart
  },
  props: {
    className: {
      type: String,
      default: 'industry-chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    }
  },
  data() {
    return {
      loading1: false,
      loading2: false,
      loading3: false,
      loading4: false,
      industryChart: null,
      productBarChart: null,
      productCategoryBarChart: null,
      productSalesLineChart: null,
      productSalesDataLineChart: null,
      productCategoryDataPieChart: null,
      regionId: null, // 所在地编码
      regionName: null, // 所在地名
      selectedProvince: {
        province: '430000',
        city: '431100',
        area: '431125',
        town: '' },
      industryStatistics: { // 统计种植面积、养殖数量、企业数量、从业人数、种植业、畜牧业、渔业数据
        plantingArea: 0,
        breedingNumber: 0,
        enterpriseNumber: 0,
        employeeNumber: 0,
        planting: 0,
        animalHusbandry: 0,
        fishery: 0
      },
      productCategory: [], // 产品分类
      productStatistics: [], // 产品分类的数据
      productType: null, // 产品类型
      searchProducts: [], // 搜索获取的产品数
      searchStatistics: [], // 每种产品对应的种类数
      productNameSale: null,
      productSalesStatisticTime: ['2019-11-01 00:00:00', '2020-05-01 00:00:00'], // 查询日期
      // 销售统计日期 产量 销量 销售额
      productSalesStatistics: { productSalesDate: [], productSalesOutput: [], productSaleNumber: [], productSales: [], outputUnit: null, salesUnit: null },
      productRankTime: '2020-04-01 00:00:00',
      productSalesRank: { products: [], sales: [] },
      productTypes: [{ // 选择框产品分类
        value: '粮油',
        label: '粮油'
      }, {
        value: '果品',
        label: '果品'
      }, {
        value: '蔬菜',
        label: '蔬菜'
      }, {
        value: '水产品',
        label: '水产品'
      }, {
        value: '畜产品',
        label: '畜产品'
      }]
      // { grainNumber: 0, fruitNumber: 0, vegetablesNumber: 0, livestockNumber: 0, aquaticNumber: 0 }
    }
  },
  computed: {
    ...mapGetters([
      'user'
    ])
  },
  created() {
    this.regionId = this.user.regionId
    this.regionName = this.user.region
    this.productType = '粮油'
    this.productNameSale = '鲤鱼'
    this.productRankTime = '2020-04-01 00:00:00'
  },
  mounted() {
    this.initIndustryData()
    this.initProductCategory()
    this.searchByCategory()
    this.initProductSales()
    this.initProductSalesDataLineChart()
    this.__resizeHandler = debounce(() => {
      if (this.industryChart) {
        this.industryChart.resize()
      }
    }, 100)
    this.__resizeHandler = debounce(() => {
      if (this.productBarChart) {
        this.productBarChart.resize()
      }
    }, 100)
    this.__resizeHandler = debounce(() => {
      if (this.productCategoryBarChart) {
        this.productCategoryBarChart.resize()
      }
    }, 100)
    this.__resizeHandler = debounce(() => {
      if (this.productSalesLineChart) {
        this.productSalesLineChart.resize()
      }
    }, 100)
    this.__resizeHandler = debounce(() => {
      if (this.productSalesDataLineChart) {
        this.productSalesDataLineChart.resize()
      }
    }, 100)
    window.addEventListener('resize', this.__resizeHandler)
  },
  beforeDestroy() {
    if (!this.industryChart || !this.productBarChart || !this.productCategoryBarChart || !this.productSalesLineChart) {
      return
    }
    window.removeEventListener('resize', this.__resizeHandler)
    this.industryChart.dispose()
    this.productBarChart.dispose()
    this.productCategoryBarChart.dispose()
    this.productSalesLineChart.dispose()
    this.productSalesDataLineChart.dispose()
    this.industryChart = null
    this.productBarChart = null
    this.productCategoryBarChart = null
    this.productSalesLineChart = null
    this.productSalesDataLineChart = null
  },
  methods: {
    regionChange(data) {
      if (data.town !== null) {
        this.regionId = data.town.key
        this.regionName = data.town.value
      } else if (data.area !== null) {
        this.regionId = data.area.key
        this.regionName = data.area.value
      } else if (data.city !== null) {
        this.regionId = data.city.key
        this.regionName = data.city.value
      } else if (data.province !== null) {
        this.regionId = data.province.key
        this.regionName = data.province.value
      }
    },
    selectByRegion() {
      this.initIndustryData()
      this.initProductCategory()
      this.searchByCategory()
      this.initProductSales()
      this.initProductSalesDataLineChart()
    },
    // 初始化产业分布数据
    initIndustryData() {
      // this.industryChart.showLoading()
      this.loading1 = true
      get({ regionName: this.regionName }).then(res => {
        this.loading1 = false
        // this.industryChart.hideLoading()
        this.industryStatistics.plantingArea = res.plantingArea
        this.industryStatistics.breedingNumber = res.breedingNumber
        this.industryStatistics.enterpriseNumber = res.enterpriseNumber
        this.industryStatistics.employeeNumber = res.employeeNumber
        this.industryStatistics.animalHusbandry = res.animalHusbandry
        this.industryStatistics.planting = res.planting
        this.industryStatistics.fishery = res.fishery
        // this.initIndustryChart()
        if (this.industryChart !== null) {
          this.setIndustryData(this.industryStatistics.animalHusbandry, this.industryStatistics.planting, this.industryStatistics.fishery)
        } else {
          this.initIndustryChart()
        }
      })
    },
    initIndustryChart() {
      this.industryChart = echarts.init(document.getElementById('industryChart'), 'macarons')
      this.industryChart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          left: 'center',
          bottom: '10',
          data: ['种植业', '畜牧业', '渔业']
        },
        calculable: true
      })
      this.setIndustryData(this.industryStatistics.animalHusbandry, this.industryStatistics.planting, this.industryStatistics.fishery)
    },
    setIndustryData(animalHusbandry, planting, fishery) {
      this.industryChart.setOption({
        series: [
          {
            name: '产品比例',
            type: 'pie',
            // roseType: 'radius',
            radius: [15, 95],
            center: ['50%', '38%'],
            data: [
              { value: planting, name: '种植业' },
              { value: animalHusbandry, name: '畜牧业' },
              { value: fishery, name: '渔业' }
            ],
            animationEasing: 'cubicInOut',
            animationDuration: 2600
          }
        ]
      })
    },
    initProductCategory() {
      // this.productBarChart.showLoading()
      getProductStatistics({ regionName: this.regionName }).then(res => {
        // this.productBarChart.hideLoading()
        this.productCategory = res.category
        this.productStatistics = res.statisticsData
        if (this.productBarChart !== null) {
          this.setProductOption(this.productCategory, this.productStatistics)
        } else {
          this.initProductBarChart()
        }
      })
    },
    initProductBarChart() {
      this.productBarChart = echarts.init(document.getElementById('productCategory'), 'macarons')
      this.productBarChart.setOption({
        legend: {
          data: ['产品数']
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          top: 30,
          left: '3%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        }
      })
      this.setProductOption(this.productCategory, this.productStatistics)
    },
    setProductOption(category, productStatistics) {
      this.productBarChart.setOption({
        xAxis: [{
          type: 'category',
          data: category,
          // ['粮油', '果品', '蔬菜', '畜产品', '水产品'],
          axisTick: {
            alignWithLabel: true
          }
        }],
        yAxis: [{
          type: 'value',
          name: '数量(个)',
          axisTick: {
            show: false
          }
        }],
        series: [{
          name: '产品数',
          type: 'bar',
          data: productStatistics,
          animationDuration
        }]
      })
    },
    searchByCategory() {
      this.loading4 = true
      getProductsByCatagory({ regionName: this.regionName, category: this.productType }).then(res => {
        this.loading4 = false
        this.searchProducts = res.products
        this.searchStatistics = res.enterprises
        this.initCategoryBarChart(this.searchProducts, this.searchStatistics)
      })
    },
    initCategoryBarChart(xAxisData, seriesData) {
      this.productCategoryBarChart = echarts.init(document.getElementById('productOfCategory'), 'macarons')
      this.productCategoryBarChart.setOption({
        color: ['#3398DB'],
        legend: {
          data: ['企业数量']
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          top: 30,
          left: '3%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            data: xAxisData,
            axisTick: {
              alignWithLabel: true
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '数量(个)'
          }
        ],
        series: [
          {
            name: '组织机构数量',
            type: 'bar',
            // barWidth: '60%',
            data: seriesData
          }
        ]
      })
      // this.setCategoryBarChartData(this.searchProducts, this.searchStatistics)
    },
    searchProductSalesData() {
      this.initProductSales()
    },
    initProductSales() {
      const params = { regionName: this.regionName, productName: this.productNameSale.trim(), timestamps1: this.productSalesStatisticTime === null ? null : this.productSalesStatisticTime[0], timestamps2: this.productSalesStatisticTime === null ? null : this.productSalesStatisticTime[1] }
      if (!params.productName) {
        this.$message({
          message: '产品名称不能为空',
          type: 'warning'
        })
        return false
      }
      this.loading2 = true
      getProductSalesStatistics(params).then(res => {
        this.loading2 = false
        this.productSalesStatistics.productSalesDate = res.date
        this.productSalesStatistics.productSalesOutput = res.output
        this.productSalesStatistics.productSaleNumber = res.saleNumber
        this.productSalesStatistics.productSales = res.sales
        this.productSalesStatistics.outputUnit = res.outputUnit
        this.productSalesStatistics.salesUnit = res.salesUnit
        this.initProductSalesLineChart()
        // this.initProductSalesDataLineChart()
      })
    },
    initProductSalesLineChart() {
      this.productSalesLineChart = echarts.init(document.getElementById('productSalesData'), 'macarons')
      this.setProductSalesLineChartData(this.productSalesStatistics)
    },
    setProductSalesLineChartData({ productSalesDate, productSalesOutput, productSaleNumber, outputUnit, productSales, salesUnit } = {}) {
      this.productSalesLineChart.setOption({
        xAxis: {
          type: 'category',
          data: productSalesDate,
          boundaryGap: true,
          axisTick: {
            show: true
          }
        },
        grid: {
          top: 30,
          left: '2%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        yAxis: [
          {
            type: 'value',
            name: '产量' + '(' + outputUnit + ')'
          },
          {
            type: 'value',
            name: '销售额' + '(' + salesUnit + ')'
          }
        ],
        legend: {
          data: ['产量', '销量', '销售额']
        },
        series: [{
          name: '产量', itemStyle: {
            normal: {
              color: '#d4237a',
              lineStyle: {
                color: '#d4237a',
                width: 2
              }
            }
          },
          smooth: false,
          type: 'bar',
          data: productSalesOutput,
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        },
        {
          name: '销量',
          smooth: false,
          type: 'bar',
          itemStyle: {
            normal: {
              color: '#3888fa',
              lineStyle: {
                color: '#3888fa',
                width: 2
              },
              areaStyle: {
                color: '#f3f8ff'
              }
            }
          },
          data: productSaleNumber,
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        },
        {
          name: '销售额',
          smooth: false,
          type: 'line',
          data: productSales,
          yAxisIndex: 1,
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        }]
      })
    },
    searchProductSalesRank() {
      this.initProductSalesDataLineChart()
    },
    initProductSalesDataLineChart() {
      this.loading3 = true
      getProductSalesRank({ regionName: this.regionName, timestamps1: this.productRankTime }).then(res => {
        this.loading3 = false
        this.productSalesRank.products = res.products
        this.productSalesRank.sales = res.sales
        this.productSalesDataLineChart = echarts.init(document.getElementById('productSales'), 'macarons')
        this.setProductSalesDataLineChartData(this.productSalesRank)
      })
    },
    setProductSalesDataLineChartData({ products, sales } = {}) {
      this.productSalesDataLineChart.setOption({
        color: ['#7dc5eb'],
        xAxis: {
          data: products,
          boundaryGap: true,
          axisTick: {
            show: false
          }
        },
        grid: {
          top: 30,
          left: '2%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        yAxis: {
          name: '销售额(万元)',
          nameLocation: 'end',
          type: 'value'
        },
        legend: {
          data: ['销售额']
        },
        series: [{
          name: '销售额',
          smooth: false,
          type: 'bar',
          data: sales,
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        }]
      })
    }
  }
  // setCategoryBarChartData(xAxisData, seriesData) {
  //   this.productCategoryBarChart.setOption({
  //
  //   })
  // }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .dashboard-editor-container {
    padding: 18px 22px 22px 22px;
    background-color: rgb(240, 242, 245);
    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
  }
  .panel-group {
    margin-top: 18px;
    .card-panel-col{
      margin-bottom: 32px;
    }
    .card-panel {
      height: 108px;
      font-size: 12px;
      position: relative;
      overflow: hidden;
      color: #666;
      background: #fff;
      box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
      border-color: rgba(0, 0, 0, .05);
      .icon-people {
        color: #40c9c6;
      }
      .icon-message {
        color: #36a3f7;
      }
      .icon-money {
        color: #f4516c;
      }
      .icon-shopping {
        color: #34bfa3
      }
      .card-panel-icon-wrapper {
        float: left;
        margin: 14px 0 0 14px;
        padding: 16px;
        transition: all 0.38s ease-out;
        border-radius: 6px;
      }
      .card-panel-icon {
        float: left;
        font-size: 48px;
      }
      .card-panel-description {
        float: right;
        font-weight: bold;
        margin: 26px;
        margin-left: 0px;
        .card-panel-text {
          line-height: 18px;
          color: rgba(0, 0, 0, 0.45);
          font-size: 16px;
          margin-bottom: 12px;
        }
        .card-panel-num {
          font-size: 20px;
        }
      }
    }
  }
</style>
