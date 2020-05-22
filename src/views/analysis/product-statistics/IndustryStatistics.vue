<template>
  <div>
    <el-row>
      <div class="chart-wrapper">
        <div style="margin-bottom: 25px;margin-top: 15px">
          <span>地区：</span>
          <v-region type="column" :town="true" @values="regionChange" />
          <span style="margin-left: 10px">统计项目：</span>
          <el-select v-model="query.statisticsItem" placeholder="请选择">
            <el-option
              v-for="item in productTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
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
          <el-table-column v-if="columns.visible('regionId')" :show-overflow-tooltip="true" prop="regionId" label="邮编" />
          <el-table-column v-if="columns.visible('statisticsItem')" :show-overflow-tooltip="true" prop="statisticsItem" label="统计项目" />
          <el-table-column v-if="columns.visible('statisticsTotal')" :show-overflow-tooltip="true" prop="statisticsTotal" label="数量" />
          <el-table-column v-if="columns.visible('unit')" :show-overflow-tooltip="true" prop="unit" label="单位" />
          <el-table-column v-if="columns.visible('statisticsTime')" :show-overflow-tooltip="true" prop="statisticsTime" label="统计日期">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.statisticsTime) }}</span>
            </template>
          </el-table-column>
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

const defaultCrud = CRUD({ url: 'api/industry-statistics', optShow: { add: false, edit: false, del: false, download: true }, sort: ['regionName,asc', 'statisticsItem,desc'] })

export default {
  name: 'IndustryStatistics',
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
      },
      productType: null,
      productTypes: [{ // 选择框产品分类
        value: '行政单位',
        label: '行政单位'
      }, {
        value: '组织机构',
        label: '组织机构'
      }, {
        value: '员工',
        label: '员工数量'
      }, {
        value: '种植面积',
        label: '种植面积'
      }, {
        value: '养殖数量',
        label: '养殖数量'
      }, {
        value: '种植业',
        label: '种植业产品'
      }, {
        value: '畜牧业',
        label: '畜牧业产品'
      }, {
        value: '渔业',
        label: '渔业产品'
      }, {
        value: '帮扶项目',
        label: '补助项目'
      }, {
        value: '帮扶资金',
        label: '补助资金'
      }]
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
