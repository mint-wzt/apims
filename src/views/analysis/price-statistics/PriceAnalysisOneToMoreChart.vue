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
          style="width: 150px;"
          class="filter-item"
        />
        <span>市场：</span>
        <v-region v-model="selectedProvince" :city="false" :area="false" :town="false" @values="regionChange1" />
        <el-select v-model="queryLatestParams.startMarket" style="width: 178px" placeholder="请选择" @change="getMarketsName1">
          <el-option
            v-for="(item, index) in markets1"
            :key="'market-' + index"
            :label="item.marketName"
            :value="item.id"
          />
        </el-select>
        <span>对比：</span>
        <v-region v-model="selectedProvince1" :city="false" :area="false" :town="false" @values="regionChange2" />
        <el-select v-model="queryLatestParams.endMarket" style="width: 178px" placeholder="请选择" @change="getMarketsName2">
          <el-option
            v-for="(item, index) in markets2"
            :key="'market-' + index"
            :label="item.marketName"
            :value="item.id"
          />
        </el-select>
        <span>时间：</span>
        <el-date-picker
          v-model="year"
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
        <div id="priceAnalysisOneToMoreChart" :class="className" :style="{height:height,width:width}" />
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <div style="background-color: #fffaf6;padding-left: 10px;padding-top: 20px">
          <el-divider />
          <div>
            <div style="margin-bottom: 15px">最高：{{ latestPrice.startMax }}{{ latestPrice.priceUnit }}</div>
            <div style="margin-bottom: 15px">品种：{{ queryLatestParams.productName }}</div>
            <div style="margin-bottom: 15px">时间：{{ queryLatestParams.statisticsYear.substring(0,4) }}年{{ latestPrice.maxMonth + 1 }}月</div>
            <div>市场：{{ queryLatestParams.startMarket }}</div>
          </div>
          <el-divider />
          <div>
            <div style="margin-bottom: 15px">最低：{{ latestPrice.endMin }}{{ latestPrice.priceUnit }}</div>
            <div style="margin-bottom: 15px">品种：{{ queryLatestParams.productName }}</div>
            <div style="margin-bottom: 15px">时间：{{ queryLatestParams.statisticsYear.substring(0,4) }}年{{ latestPrice.minMonth + 1 }}月</div>
            <div>市场：{{ queryLatestParams.endMarket }}</div>
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
import { getMonthPriceOneToMoreMarket } from '@/api/statistics/month-price-statistics'

export default {
  name: 'PriceAnalysisOneToMoreChart',
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
      markets1: null,
      markets2: null,
      loading: false,
      year: null,
      selectedProvince: {
        province: '430000',
        city: '431100',
        area: '431125',
        town: '43112510' },
      selectedProvince1: {
        province: '430000',
        city: '431100',
        area: '431125',
        town: '43112510' },
      queryLatestParams: { startMarket: null, endMarket: null, productName: null, statisticsYear: null },
      latestPrice: { startMarketData: [], endMarketData: [], priceUnit: null, startMax: 0, maxMonth: null, endMin: 0, minMonth: null }
    }
  },
  computed: {
  },
  created() {
    this.queryLatestParams.productName = '土鸡'
    this.queryLatestParams.statisticsYear = '2020-03-01 00:00:00'
    this.year = '2020-03-01 00:00:00'
  },
  mounted() {
    this.queryLatestParams.startMarket = '湖南省江永县农贸市场'
    this.queryLatestParams.endMarket = '湖南省永州市冷水滩区批发市场'
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
      if (!this.year || !this.queryLatestParams.productName || !this.queryLatestParams.startMarket || !this.queryLatestParams.endMarket) {
        this.$message({
          message: '产品或市场或时间不能为空',
          type: 'warning'
        })
        return false
      }
      this.queryLatestParams.statisticsYear = this.year
      this.queryLatestParams.statisticsYear = this.queryLatestParams.statisticsYear.substring(0, 4)
      this.loading = true
      getMonthPriceOneToMoreMarket(this.queryLatestParams).then(res => {
        this.loading = false
        this.latestPrice.startMarketData = res.startMarketData
        this.latestPrice.endMarketData = res.endMarketData
        this.latestPrice.priceUnit = res.priceUnit
        this.latestPrice.startMax = Math.max.apply(null, this.latestPrice.startMarketData)
        this.latestPrice.maxMonth = this.latestPrice.startMarketData.indexOf(this.latestPrice.startMax)
        this.latestPrice.endMin = Math.min.apply(null, this.latestPrice.endMarketData)
        this.latestPrice.minMonth = this.latestPrice.endMarketData.indexOf(this.latestPrice.endMin)
        this.initChart()
      })
    },
    searchProductLatestPrice() {
      this.initLatestPriceChart()
    },
    // 获取该地区的所有市场
    regionChange1(data) {
      if (data.province !== null) {
        getMarkets({ regionName: data.province.value }).then(res => {
          this.markets1 = res
        })
      } else {
        this.markets1 = null
      }
      this.queryLatestParams.startMarket = null
    },
    // 获取该地区的所有市场
    regionChange2(data) {
      if (data.province !== null) {
        getMarkets({ regionName: data.province.value }).then(res => {
          this.markets2 = res
        })
      } else {
        this.markets2 = null
      }
      this.queryLatestParams.endMarket = null
    },
    getMarketsName1(val) {
      let obj = {}
      obj = this.markets1.find((item) => {
        return item.id === val
      })
      this.queryLatestParams.startMarket = obj.marketName
    },
    getMarketsName2(val) {
      let obj = {}
      obj = this.markets2.find((item) => {
        return item.id === val
      })
      this.queryLatestParams.endMarket = obj.marketName
    },
    sidebarResizeHandler(e) {
      if (e.propertyName === 'width') {
        this.__resizeHandler()
      }
    },
    setOptions({ startMarketData, endMarketData, priceUnit } = {}) {
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
          data: [this.queryLatestParams.startMarket, this.queryLatestParams.endMarket]
        },
        series: [{
          name: this.queryLatestParams.startMarket, itemStyle: {
            normal: {
              color: '#FF005A',
              lineStyle: {
                color: '#FF005A',
                width: 2
              }
            }
          },
          smooth: false,
          type: 'bar',
          data: startMarketData,
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        },
        {
          name: this.queryLatestParams.endMarket,
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
          data: endMarketData,
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        }]
      })
    },
    initChart() {
      this.chart = echarts.init(document.getElementById('priceAnalysisOneToMoreChart'), 'macarons')
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
