<template>
  <div class="dashboard-container">
    <div class="dashboard-editor-container">
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <el-divider content-position="left">
          <span style="font-size: 25px;color: #C03639">
            价格查询
          </span>
        </el-divider>
        <latest-price-chart />
      </el-row>
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <el-divider content-position="left">
          <span style="font-size: 25px;color: #C03639">
            价格分析
          </span>
        </el-divider>
        <el-tabs v-model="selectedTabPane" @tab-click="handleClick">
          <el-tab-pane label="单一产品单一市场年度走势" name="first" />
          <el-tab-pane label="单一产品多个市场年度走势" name="second" />
        </el-tabs>
        <div v-if="selectedTabPane === 'first'">
          <price-analysis-one-to-one-chart />
        </div>
        <div v-else>
          <price-analysis-one-to-more-chart />
        </div>
      </el-row>
    </div>
  </div>
</template>

<script>
import LatestPriceChart from './LatestPriceChart'
import PriceAnalysisOneToOneChart from './PriceAnalysisOneToOneChart'
import PriceAnalysisOneToMoreChart from './PriceAnalysisOneToMoreChart'
import { mapGetters } from 'vuex'
/**
   * 记录访问，只有页面刷新或者第一次加载才会记录
   */
export default {
  name: 'PriceStatistics',
  components: {
    PriceAnalysisOneToMoreChart,
    LatestPriceChart,
    PriceAnalysisOneToOneChart
  },
  data() {
    return {
      selectedTabPane: 'first'
    }
  },
  computed: {
    ...mapGetters([
      'user'
    ])
  },
  created() {
  },
  mounted() {
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event)
      this.selectedTabPane = tab.name
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
</style>
