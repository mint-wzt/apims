<template>
  <div class="dashboard-container">
    <div class="head-container" style="padding-top: 10px;padding-left: 20px">
      <v-region type="column" :town="true" class="filter-item" />
      <el-input clearable size="small" placeholder="模糊搜索" style="width: 200px;" class="filter-item" />
      <span>
        <el-button class="filter-item" size="mini" type="success" icon="el-icon-search">搜索</el-button>
        <el-button class="filter-item" size="mini" type="warning" icon="el-icon-refresh-left">重置</el-button>
      </span>
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
              <svg-icon icon-class="employee5" class-name="card-panel-icon" />
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
              <svg-icon icon-class="planting" class-name="card-panel-icon" />
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
              <svg-icon icon-class="animal1" class-name="card-panel-icon" />
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
            <div id="industryChart" :style="{height:height,width:width}" />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="12">
          <div class="chart-wrapper">
            <div id="productCategory" :style="{height:height,width:width}" />
          </div>
        </el-col>
      </el-row>
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <el-input clearable size="small" placeholder="模糊搜索" style="width: 200px;" class="filter-item" />
            <span>
              <el-button class="filter-item" size="mini" type="success" icon="el-icon-search">搜索</el-button>
              <el-button class="filter-item" size="mini" type="warning" icon="el-icon-refresh-left">重置</el-button>
            </span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="16">
          <div class="chart-wrapper">
            <line-chart />
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="32">
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <radar-chart />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <pie-chart />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <bar-chart />
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import LineChart from './dashboard/LineChart'
import RadarChart from '@/components/Echarts/RadarChart'
import PieChart from '@/components/Echarts/PieChart'
import BarChart from '@/components/Echarts/BarChart'
import { count } from '@/api/monitor/visits'
import '@/assets/styles/eladmin.scss'
import CountTo from 'vue-count-to'
import { get } from '@/api/statistics/industry-statistics.js'
import { getProductStatistics } from '@/api/statistics/product-statistics.js'
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
    LineChart,
    RadarChart,
    PieChart,
    BarChart
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
      industryChart: null,
      productBarChart: null,
      regionId: null,
      industryStatistics: { plantingArea: 0, breedingNumber: 0, enterpriseNumber: 0, employeeNumber: 0, planting: 0, animalHusbandry: 0, fishery: 0 },
      productCategory: [],
      productStatistics: []
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
  },
  mounted() {
    this.initIndustryData()
    this.initProductCategory()
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
    window.addEventListener('resize', this.__resizeHandler)
  },
  beforeDestroy() {
    if (!this.industryChart || !this.productBarChart) {
      return
    }
    window.removeEventListener('resize', this.__resizeHandler)
    this.industryChart.dispose()
    this.productBarChart.dispose()
    this.industryChart = null
    this.productBarChart = null
  },
  methods: {
    // 初始化产业分布数据
    initIndustryData() {
      this.initIndustryChart()
      // this.industryChart.showLoading()
      get({ regionId: this.regionId }).then(res => {
        // this.industryChart.hideLoading()
        this.industryStatistics.plantingArea = res.plantingArea
        this.industryStatistics.breedingNumber = res.breedingNumber
        this.industryStatistics.enterpriseNumber = res.enterpriseNumber
        this.industryStatistics.employeeNumber = res.employeeNumber
        this.industryStatistics.animalHusbandry = res.animalHusbandry
        this.industryStatistics.planting = res.planting
        this.industryStatistics.fishery = res.fishery
        this.setIndustryData(res)
      })
    },
    initIndustryChart() {
      this.industryChart = echarts.init(document.getElementById('industryChart'), 'macarons')
      this.industryChart.setOption({
        title: {
          text: '产业分布'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          left: 'center',
          bottom: '10',
          data: ['种植业', '畜牧业', '渔业']
        },
        calculable: true })
    },
    setIndustryData(res) {
      this.industryChart.setOption({
        series: [
          {
            name: '产品比例',
            type: 'pie',
            // roseType: 'radius',
            radius: [15, 95],
            center: ['50%', '38%'],
            data: [
              { value: res.planting, name: '种植业' },
              { value: res.animalHusbandry, name: '畜牧业' },
              { value: res.fishery, name: '渔业' }
            ],
            animationEasing: 'cubicInOut',
            animationDuration: 2600
          }
        ]
      })
    },
    initProductCategory() {
      this.initProductBarChart()
      // this.productBarChart.showLoading()
      getProductStatistics({ regionId: this.regionId }).then(res => {
        // this.productBarChart.hideLoading()
        this.productCategory = res.category
        this.productStatistics = res.statisticsData
        this.initProductBarChart()
      })
    },
    initProductBarChart() {
      this.productBarChart = echarts.init(document.getElementById('productCategory'), 'macarons')
      this.setProductOption(this.productCategory, this.productStatistics)
    },
    setProductOption(category, productStatistics) {
      this.productBarChart.setOption({
        title: {
          text: '产品分布'
        },
        legend: {
          // left: 'center',
          // // bottom: '10',
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
          left: '15%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
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
    }
  }
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
