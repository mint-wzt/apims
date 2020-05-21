<template>
  <div class="app-container">
    <el-divider content-position="left">
      <span style="font-size: 25px;color: #C03639">
        数据统计
      </span>
    </el-divider>
    <el-tabs v-model="selectedTabPane" @tab-click="handleClick">
      <el-tab-pane label="销售数据统计" name="first" />
      <el-tab-pane label="生产数据统计" name="second" />
    </el-tabs>
    <div v-if="selectedTabPane === 'first'">
      <sales-data />
    </div>
    <div v-else>
      <product-data />
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import SalesData from './SalesData'
import ProductData from './ProductData'
/**
   * 记录访问，只有页面刷新或者第一次加载才会记录
   */
export default {
  name: 'ProductStatistics',
  components: {
    ProductData,
    SalesData
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
