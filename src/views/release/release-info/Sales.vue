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
      <el-col v-for="(item, index) in crud.data" :key="index" v-loading="crud.loading" :xs="24" :sm="24" :lg="6" :span="6" style="padding: 20px" @selection-change="crud.selectionChangeHandler">
        <el-card :body-style="{ padding: '0px' }">
          <img style="height: 200px; border-radius:10px;" :src="getImageURL(item.productImage)" class="image">
          <div style="padding: 14px;">
            <span style="font-size: 20px;color: red">{{ item.price }}</span>
            <div class="bottom clearfix">
              <span class="product">{{ item.productName }} / {{ item.categoryName }}</span>
              <el-button type="text" class="button" @click="getProductInfo(item)">查看详情</el-button>
            </div>
            <div style="margin-top: 10px;">
              <span style="margin-right: 10px" class="time">{{ item.deliveryAddress }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-dialog :visible.sync="dialog" title="供应详情">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span style="font-size: 17px">品种名称：</span>
          <span style="color: #00a0e9;font-size: 17px">{{ dialogItem.productName }}</span>
        </div>
        <el-row>
          <el-col :xs="24" :sm="24" :lg="16">
            <div class="text item">
              <span class="tap">品种类型：</span>{{ dialogItem.categoryName }}
            </div>
            <div class="text item">
              <span class="tap">起批量：</span>{{ dialogItem.batchStart }}
            </div>
            <div class="text item">
              <span class="tap">价格：</span>{{ dialogItem.price }}
            </div>
            <div class="text item">
              <span class="tap">发货地址：</span>{{ dialogItem.deliveryAddress }}
            </div>
            <div class="text item">
              <span class="tap">联系人：</span>{{ dialogItem.publisher }}
            </div>
            <div class="text item">
              <span class="tap">联系方式：</span>{{ dialogItem.contact }}
            </div>
            <div class="text item">
              <span class="tap">备注：</span>{{ dialogItem.remark }}
            </div>
            <div class="text item">
              <span class="tap">浏览量：</span>{{ dialogItem.views }}
            </div>
          </el-col>
          <el-col :xs="24" :sm="24" :lg="8">
            <img style="height: 200px;width: 200px; margin-right: 20px" :src="getImageURL(dialogItem.productImage)">
          </el-col>
        </el-row>
      </el-card>
    </el-dialog>
    <pagination />
    <el-row v-show="false">
      <el-table ref="table" />
    </el-row>
  </div>
</template>
<script>
import CRUD, { presenter, header, crud } from '@crud/crud'
import { getSalesData } from '@/api/statistics/sales-statistics'
import pagination from '@crud/Pagination'
import { addViews } from '@/api/release/sales-release'
import Avatar from '@/assets/images/product.jpg'
import { mapGetters } from 'vuex'

const defaultCrud = CRUD({ url: 'api/sales-release', sort: ['releaseDate,desc'], query: { releaseStatus: 1 }})

export default {
  name: 'Sales',
  components: { pagination },
  mixins: [presenter(defaultCrud), header(), crud()],
  data() {
    return {
      dialogItem: {
        productName: null,
        categoryName: null,
        batchStart: null,
        price: null,
        deliveryAddress: null,
        publisher: null,
        views: 0,
        contact: null,
        remark: '-',
        productImage: null
      },
      dialog: false,
      productInfo: null,
      currentDate: new Date(),
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
  computed: {
    ...mapGetters([
      'user',
      'uploadProductImageApi',
      'baseApi'
    ])
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
    getProductInfo(item) {
      this.dialog = true
      this.dialogItem.productName = item.productName
      this.dialogItem.categoryName = item.categoryName
      this.dialogItem.batchStart = item.batchStart
      this.dialogItem.price = item.price
      this.dialogItem.deliveryAddress = item.deliveryAddress
      this.dialogItem.publisher = item.publisher
      this.dialogItem.views = item.views
      this.dialogItem.contact = item.contact
      this.dialogItem.remark = item.remark
      this.dialogItem.productImage = item.productImage
      addViews({ id: item.id }).then(res => {})
    },
    getImageURL(imageName) {
      return imageName ? this.baseApi + '/avatar/' + imageName : Avatar
    },
    submit() {
      this.crud.query.categoryName = this.queryParams.categoryName
      this.crud.query.releaseDate = this.queryParams.releaseDates
      this.crud.toQuery()
    },
    getSalesInfo() {
      getSalesData(this.queryParams).then(res => {
        this.tableData = res
      })
    }
  }
}
</script>

<style scoped>
  .tap {
    font-size: 15px;
    font-weight: bold;
  }
  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }
  .product{
    font-size: 14px;
  }
  .time {
    font-size: 13px;
    color: #999;
  }

  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    font-size: 13px;
    padding: 0;
    float: right;
  }

  .image {
    width: 100%;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }
</style>
