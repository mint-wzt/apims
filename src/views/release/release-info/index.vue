<template>
  <div class="app-container">
    <el-divider content-position="left">
      <span style="font-size: 25px;color: #C03639">
        最新消息
      </span>
    </el-divider>
    <el-tabs v-model="selectedTabPane" type="card" style="margin-top: 40px" @tab-click="handleClick">
      <el-tab-pane label="最新供应" name="first">
        <div v-if="selectedTabPane === 'first'">
          <sales />
        </div>
      </el-tab-pane>
      <el-tab-pane label="最新采购" name="second">
        <div v-if="selectedTabPane === 'second'">
          <purchase />
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Purchase from './Purchase'
import Sales from './Sales'
/**
   * 记录访问，只有页面刷新或者第一次加载才会记录
   */
export default {
  name: 'ReleaseInfo',
  components: {
    Sales,
    Purchase
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
