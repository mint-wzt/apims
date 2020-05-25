<template>
  <div>
    <el-row>
      <div class="chart-wrapper">
        <div style="margin-bottom: 25px;margin-top: 15px">
          <span>品种类型：</span>
          <el-select v-model="queryParams.categoryName" placeholder="请选择">
            <el-option
              v-for="item in categories"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
          <span style="margin-left: 10px">品种名称：</span>
          <el-input
            v-model="query.blurry"
            clearable
            size="small"
            placeholder="输入品种名称"
            style="width: 200px;"
            class="filter-item"
            @keyup.enter.native="crud.toQuery"
          />
          <span style="margin-left: 10px">发布时间：</span>
          <el-date-picker
            v-model="queryParams.releaseDates"
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
            <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="submit">搜索</el-button>
          </span>
        </div>
      </div>
    </el-row>
    <el-row>
      <el-col :xs="24" :sm="24" :lg="24">
        <el-table ref="table" v-loading="crud.loading" :data="crud.data" default-expand-all style="width: 100%;" @selection-change="crud.selectionChangeHandler" @expand-change="addViews">
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-form label-position="left" inline class="demo-table-expand">
                <div style="font-size: 18px;color: cornflowerblue;margin-bottom: 15px">
                  采购详情
                </div>
                <el-form-item label="采购品种">
                  <span>{{ props.row.productName }}</span>
                </el-form-item>
                <el-form-item label="采购数量">
                  <span>{{ props.row.purchaseQuantity }}</span>
                </el-form-item>
                <el-form-item label="规格品质">
                  <span>{{ props.row.specification }}</span>
                </el-form-item>
                <el-form-item label="浏览次数">
                  <span>{{ props.row.views }} 次</span>
                </el-form-item>
                <el-form-item label="品种类型">
                  <span>{{ props.row.categoryName }}</span>
                </el-form-item>
                <el-form-item label="发布日期">
                  <span>{{ props.row.releaseDate }}</span>
                </el-form-item>
                <el-form-item label="收货地址">
                  <span>{{ props.row.receiptAddress }}</span>
                </el-form-item>
                <el-form-item label="期望货源">
                  <span>{{ props.row.supplyAddress }}</span>
                </el-form-item>
                <el-form-item label="发布人">
                  <span>{{ props.row.publisher }}</span>
                </el-form-item>
                <el-form-item label="联系方式">
                  <span>{{ props.row.contact }}</span>
                </el-form-item>
                <el-form-item label="备注">
                  <span>{{ props.row.remark }}</span>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('productName')" :show-overflow-tooltip="true" prop="productName" label="采购品种" />
          <el-table-column v-if="columns.visible('categoryName')" :show-overflow-tooltip="true" prop="categoryName" label="品种类型" />
          <el-table-column v-if="columns.visible('purchaseQuantity')" :show-overflow-tooltip="true" prop="purchaseQuantity" label="采购数量" />
          <el-table-column v-if="columns.visible('supplyAddress')" :show-overflow-tooltip="true" prop="supplyAddress" label="期望货源" />
          <el-table-column v-if="columns.visible('views')" :show-overflow-tooltip="true" prop="views" label="浏览次数" />
          <el-table-column v-if="columns.visible('createTime')" :show-overflow-tooltip="true" prop="createTime" width="140" label="发布日期">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
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
import { getSalesData } from '@/api/statistics/sales-statistics'
import pagination from '@crud/Pagination'
import { addViews } from '@/api/release/purchase-release'

const defaultCrud = CRUD({ url: 'api/purchase-release', sort: ['releaseDate,desc'], query: { releaseStatus: 1 }})

export default {
  name: 'Purchase',
  components: { pagination },
  mixins: [presenter(defaultCrud), header(), crud()],
  data() {
    return {
      queryParams: {
        categoryName: null,
        productName: null,
        releaseDates: []
      },
      categories: [{ // 选择框产品分类
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
    }
  },
  created() {
  },
  mounted: function() {
    const that = this
    window.onresize = function temp() {
      that.height = document.documentElement.clientHeight - 180 + 'px;'
    }
  },
  methods: {
    submit() {
      this.crud.query.categoryName = this.queryParams.categoryName
      this.crud.query.releaseDate = this.queryParams.releaseDates
      this.crud.toQuery()
    },
    getSalesInfo() {
      getSalesData(this.queryParams).then(res => {
        this.tableData = res
      })
    },
    addViews(row, expandedRows) {
      addViews({ id: row.id }).then(res => {})
    }
  }
}
</script>

<style scoped>
  .demo-table-expand {
    font-size: 0;
    margin-left: 60px;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 30%;
  }
</style>
