<template>
  <div>
    <el-row>
      <div class="chart-wrapper">
        <span>产品：</span>
        <el-input
          v-model="queryLatestParams.productName"
          clearable
          size="small"
          placeholder="输入产品名称"
          style="width: 200px;"
          class="filter-item"
        />
        <span>市场：</span>
        <v-region v-model="selectedProvince" :city="false" :area="false" :town="false" @values="regionChange" />
        <el-select v-model="queryLatestParams.market" style="width: 178px" placeholder="请选择" @change="getMarketsName">
          <el-option
            v-for="(item, index) in markets"
            :key="'market-' + index"
            :label="item.marketName"
            :value="item.id"
          />
        </el-select>
        <span>时间：</span>
        <el-date-picker
          v-model="queryLatestParams.startTime"
          :default-time="['00:00:00','23:59:59']"
          size="small"
          class="date-item"
          type="year"
          style="width: 150px;"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="选择年"
        />
        <span>对比：</span>
        <el-date-picker
          v-model="queryLatestParams.endTime"
          :default-time="['00:00:00','23:59:59']"
          size="small"
          class="date-item"
          type="year"
          style="width: 150px;"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="选择年"
        />
        <span>
          <el-button :loading="loading" class="filter-item" size="mini" type="success" icon="el-icon-search" @click="searchProductLatestPrice">搜索</el-button>
        </span>
      </div>
    </el-row>
    <el-row>
      <el-col :xs="24" :sm="24" :lg="18">
        <div id="latestPriceChart" :class="className" :style="{height:height,width:width}" />
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <div style="background-color: #fffaf6;padding-left: 10px;padding-top: 20px;">
          <span>行情分析：<el-tag :type="resultTagType" effect="dark">{{ analysisResult }}</el-tag></span>
          <el-divider />
          <div>
            <div style="margin-bottom: 15px">最高：{{ latestPrice.startMax }}{{ latestPrice.priceUnit }}</div>
            <div style="margin-bottom: 15px">品种：{{ queryLatestParams.productName }}</div>
            <div style="margin-bottom: 15px">时间：{{ queryLatestParams.startTime.substring(0,4) }}年{{ latestPrice.maxMonth + 1 }}月</div>
            <div>市场：{{ queryLatestParams.market }}</div>
          </div>
          <el-divider />
          <div>
            <div style="margin-bottom: 15px">最低：{{ latestPrice.endMin }}{{ latestPrice.priceUnit }}</div>
            <div style="margin-bottom: 15px">品种：{{ queryLatestParams.productName }}</div>
            <div style="margin-bottom: 15px">时间：{{ queryLatestParams.endTime.substring(0,4) }}年{{ latestPrice.minMonth + 1 }}月</div>
            <div>市场：{{ queryLatestParams.market }}</div>
          </div>
          <el-divider />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'
import { getMarkets } from '@/api/statistics/market'
import { getPriceAnalysisOneToOne } from '@/api/statistics/month-price-statistics'

export default {
  name: 'PriceAnalysisOneToOneChart',
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '450px'
    },
    autoResize: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      chart: null,
      sidebarElm: null,
      markets: null,
      loading: false,
      analysisResult: null,
      resultTagType: 'success',
      selectedProvince: {
        province: '430000',
        city: '431100',
        area: '431125',
        town: '43112510' },
      queryLatestParams: { market: null, productName: null, startTime: null, endTime: null },
      latestPrice: { startYearData: [], endYearData: [], priceUnit: null, startMax: 0, maxMonth: null, endMin: 0, minMonth: null }
    }
  },
  computed: {
  },
  created() {
    this.queryLatestParams.productName = '土鸡'
    this.queryLatestParams.startTime = '2020-03-01 00:00:00'
    this.queryLatestParams.endTime = '2019-05-01 00:00:00'
  },
  mounted() {
    this.queryLatestParams.market = '湖南省江永县农贸市场'
    this.initLatestPriceChart()
    if (this.autoResize) {
      this.__resizeHandler = debounce(() => {
        if (this.chart) {
          this.chart.resize()
        }
      }, 100)
      window.addEventListener('resize', this.__resizeHandler)
    }

    // 监听侧边栏的变化
    this.sidebarElm = document.getElementsByClassName('sidebar-container')[0]
    this.sidebarElm && this.sidebarElm.addEventListener('transitionend', this.sidebarResizeHandler)
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    if (this.autoResize) {
      window.removeEventListener('resize', this.__resizeHandler)
    }

    this.sidebarElm && this.sidebarElm.removeEventListener('transitionend', this.sidebarResizeHandler)

    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initLatestPriceChart() {
      if (!this.queryLatestParams.productName || !this.queryLatestParams.market) {
        this.$message({
          message: '产品或市场不能为空',
          type: 'warning'
        })
        return false
      }
      if (this.queryLatestParams.startTime === null || this.queryLatestParams.endTime === null) {
        this.queryLatestParams.startTime = '2020-03-01 00:00:00'
        this.queryLatestParams.endTime = '2019-05-01 00:00:00'
      }
      this.loading = true
      getPriceAnalysisOneToOne(this.queryLatestParams).then(res => {
        this.loading = false
        this.latestPrice.startYearData = res.startYearData
        this.latestPrice.endYearData = res.endYearData
        this.latestPrice.priceUnit = res.priceUnit
        this.latestPrice.startMax = Math.max.apply(null, this.latestPrice.startYearData)
        this.latestPrice.maxMonth = this.latestPrice.startYearData.indexOf(this.latestPrice.startMax)
        this.latestPrice.endMin = Math.min.apply(null, this.latestPrice.endYearData)
        this.latestPrice.minMonth = this.latestPrice.endYearData.indexOf(this.latestPrice.endMin)
        this.getAnalysisResult(this.latestPrice.startYearData, this.latestPrice.startMax)
        this.initChart()
      })
    },
    searchProductLatestPrice() {
      this.initLatestPriceChart()
    },
    getAnalysisResult(data, max) {
      const t = Math.min.apply(null, data)
      const p = data[data.length - 1]
      const res = (p - t) / (max - t)
      const average = (this.getAverage(data) - t) / (max - t)
      if (res > average + 0.15) {
        this.resultTagType = 'success'
        this.analysisResult = '良好'
      } else if (res < average - 0.15) {
        this.resultTagType = 'warning'
        this.analysisResult = '较差'
      } else {
        this.resultTagType = ''
        this.analysisResult = '一般'
      }
    },
    getAverage(data) {
      let sum = 0
      const size = data.length
      for (let i = 0; i < size; i++) {
        sum += data[i]
      }
      return sum / size
    },
    // 获取该地区的所有市场
    regionChange(data) {
      if (data.province !== null) {
        getMarkets({ regionName: data.province.value }).then(res => {
          this.markets = res
        })
      } else {
        this.markets = null
      }
      this.queryLatestParams.market = null
    },
    getMarketsName(val) {
      let obj = {}
      obj = this.markets.find((item) => {
        return item.id === val
      })
      this.queryLatestParams.market = obj.marketName
    },
    sidebarResizeHandler(e) {
      if (e.propertyName === 'width') {
        this.__resizeHandler()
      }
    },
    setOptions({ startYearData, endYearData, priceUnit } = {}, startYear, endYear) {
      this.chart.setOption({
        xAxis: {
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
          boundaryGap: true,
          axisTick: {
            show: false
          }
        },
        grid: {
          left: '3%',
          right: '2%',
          bottom: '3%',
          top: 30,
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
          axisTick: {
            show: false
          },
          name: '价格' + '(' + priceUnit + ')',
          axisLabel: {
            formatter: '¥{value}'
          }
        },
        legend: {
          data: [startYear, endYear]
        },
        series: [{
          name: startYear, itemStyle: {
            normal: {
              color: '#FF005A',
              lineStyle: {
                color: '#FF005A',
                width: 2
              }
            }
          },
          smooth: false,
          type: 'line',
          data: startYearData,
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        },
        {
          name: endYear,
          smooth: false,
          type: 'line',
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
          data: endYearData,
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        }]
      })
    },
    initChart() {
      this.chart = echarts.init(document.getElementById('latestPriceChart'), 'macarons')
      this.setOptions(this.latestPrice, this.queryLatestParams.startTime.substring(0, 4), this.queryLatestParams.endTime.substring(0, 4))
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
</style>
