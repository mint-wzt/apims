<template>
  <div>
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
        v-model="gapTime"
        :default-time="['00:00:00','23:59:59']"
        type="daterange"
        range-separator=":"
        size="small"
        class="date-item"
        value-format="yyyy-MM-dd HH:mm:ss"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
      />
      <span>
        <el-button :loading="loading" class="filter-item" size="mini" type="success" icon="el-icon-search" @click="searchProductLatestPrice">搜索</el-button>
      </span>
    </div>
    <div id="priceAnalysisChart" :class="className" :style="{height:height,width:width}" />
  </div>
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'
import { getLatestPrice } from '@/api/statistics/price-statistics'
import { getMarkets } from '@/api/statistics/market'

export default {
  name: 'LatestPriceChart',
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
      gapTime: ['2020-03-01 00:00:00', '2020-05-01 00:00:00'],
      markets: null,
      loading: false,
      selectedProvince: {
        province: '430000',
        city: '431100',
        area: '431125',
        town: '43112510' },
      queryLatestParams: { market: null, productName: null, startTime: null, endTime: null },
      latestPrice: { date: [], price: [], priceUnit: null }
    }
  },
  created() {
    this.queryLatestParams.productName = '鸡'
    this.queryLatestParams.startTime = '2020-03-01 00:00:00'
    this.queryLatestParams.endTime = '2020-05-01 00:00:00'
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
      this.loading = true
      getLatestPrice(this.queryLatestParams).then(res => {
        this.loading = false
        this.latestPrice.date = res.date
        this.latestPrice.price = res.price
        this.latestPrice.priceUnit = res.priceUnit
        this.initChart()
      })
    },
    searchProductLatestPrice() {
      this.initLatestPriceChart()
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
    setOptions({ date, price, priceUnit } = {}) {
      this.chart.setOption({
        xAxis: {
          data: date,
          boundaryGap: true,
          axisTick: {
            show: false
          }
        },
        grid: {
          left: '2%',
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
          name: '价格' + '(' + priceUnit + ')'
        },
        legend: {
          data: ['价格']
        },
        series: [{
          name: '价格',
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
          data: price,
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        }]
      })
    },
    initChart() {
      this.chart = echarts.init(document.getElementById('priceAnalysisChart'), 'macarons')
      this.setOptions(this.latestPrice)
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
