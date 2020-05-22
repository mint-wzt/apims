<template>
  <div>
    <el-row>
      <div class="chart-wrapper">
        <div style="margin-bottom: 25px;margin-top: 15px">
          <span>地区：</span>
          <v-region type="column" :town="true" @values="regionChange" />
          <span style="margin-left: 10px">产品名称：</span>
          <el-input
            v-model="query.productName"
            clearable
            size="small"
            placeholder="输入产品名称"
            style="width: 200px;"
            class="filter-item"
            @keyup.enter.native="crud.toQuery"
          />
          <span style="margin-left: 10px">时间：</span>
          <el-date-picker
            v-model="query.statisticsTimes"
            :default-time="['00:00:00','23:59:59']"
            type="monthrange"
            range-separator=":"
            size="small"
            class="date-item"
            value-format="yyyy-MM-dd HH:mm:ss"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
          <rrOperation :crud="crud" />
          <el-button
            v-if="crud.optShow.download"
            style="margin-left: 10px"
            :loading="crud.downloadLoading"
            :disabled="!crud.data.length"
            class="filter-item"
            size="mini"
            type="warning"
            icon="el-icon-download"
            @click="crud.doExport"
          >导出</el-button>
        </div>
      </div>
    </el-row>
    <el-row>
      <el-col :xs="24" :sm="24" :lg="24">
        <el-table ref="table" v-loading="crud.loading" :data="crud.data" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
          <el-table-column type="selection" width="55" />
          <el-table-column v-if="columns.visible('regionName')" :show-overflow-tooltip="true" prop="regionName" label="地区" />
          <el-table-column v-if="columns.visible('productName')" :show-overflow-tooltip="true" prop="productName" label="产品名称" />
          <el-table-column v-if="columns.visible('outputs')" :show-overflow-tooltip="true" prop="outputs" label="产量">
            <template slot-scope="scope">
              <div>{{ scope.row.output }} {{ scope.row.outputUnit }}</div>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('saleNumbers')" :show-overflow-tooltip="true" prop="saleNumbers" label="销量">
            <template slot-scope="scope">
              <div>{{ scope.row.saleNumber }} {{ scope.row.saleUnit }}</div>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('sale')" :show-overflow-tooltip="true" prop="sale" label="销售额">
            <template slot-scope="scope">
              <div>{{ scope.row.sales }} {{ scope.row.salesUnit }}</div>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('statisticsTime')" :show-overflow-tooltip="true" prop="statisticsTime" label="统计日期" />
        </el-table>
        <pagination />
      </el-col>
    </el-row>
  </div>
</template>
<script>
import CRUD, { presenter, header, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import { getSalesData } from '@/api/statistics/sales-statistics'
import pagination from '@crud/Pagination'

const defaultCrud = CRUD({ url: 'api/sales-statistics', optShow: { add: false, edit: false, del: false, download: true }, sort: ['statisticsTime,desc', 'regionName,asc'] })

export default {
  name: 'SalesData',
  mixins: [presenter(defaultCrud), header(), crud()],
  components: {
    rrOperation,
    pagination
  },
  data() {
    return {
      queryParams: {
        regionId: null,
        regionName: null,
        productName: null,
        statisticsTimes: null
      },
      loading: false,
      tableData: [],
      permission: {
        download: ['admin']
      }
    }
  },
  created() {
    // this.$nextTick(() => {
    //   console.log('1')
    //   this.crud.toQuery()
    // })
  },
  mounted: function() {
    const that = this
    window.onresize = function temp() {
      that.height = document.documentElement.clientHeight - 180 + 'px;'
    }
  },
  methods: {
    getSalesInfo() {
      getSalesData(this.queryParams).then(res => {
        this.tableData = res
      })
    },
    regionChange(data) {
      if (data.town !== null) {
        // this.crud.query.regionId = data.town.key
        this.crud.query.regionName = data.town.value
      } else if (data.area !== null) {
        // this.crud.query.regionId = data.area.key
        this.crud.query.regionName = data.area.value
      } else if (data.city !== null) {
        // this.crud.query.regionId = data.city.key
        this.crud.query.regionName = data.city.value
      } else if (data.province !== null) {
        // this.crud.query.regionId = data.province.key
        this.crud.query.regionName = data.province.value
      } else {
        this.crud.query.regionName = null
      }
    }
  }
}
</script>

<style scoped>

</style>
