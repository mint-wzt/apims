<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--侧边部门数据-->
      <el-col :xs="9" :sm="6" :md="4" :lg="4" :xl="4">
        <div class="head-container">
          <el-input
            v-model="deptName"
            clearable
            size="small"
            placeholder="输入机构名称搜索"
            prefix-icon="el-icon-search"
            class="filter-item"
            @input="getDeptDatas"
          />
        </div>
        <el-tree
          :data="deptDatas"
          :props="defaultProps"
          :expand-on-click-node="false"
          :default-expand-all="false"
          @node-click="handleNodeClick"
        />
      </el-col>
      <!--用户数据-->
      <el-col :xs="15" :sm="18" :md="20" :lg="20" :xl="20">
        <!--工具栏-->
        <div class="head-container">
          <div v-if="crud.props.searchToggle">
            <!-- 搜索 -->
            <el-input
              v-model="query.blurry"
              clearable
              size="small"
              placeholder="请地区/企业/产品名称搜索"
              style="width: 200px;"
              class="filter-item"
              @keyup.enter.native="crud.toQuery"
            />
            <el-date-picker
              v-model="query.salesDate"
              :default-time="['00:00:00','23:59:59']"
              type="daterange"
              range-separator=":"
              size="small"
              class="date-item"
              value-format="yyyy-MM-dd HH:mm:ss"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            />
            <rrOperation :crud="crud" />
          </div>
          <crudOperation show="" :permission="permission" />
        </div>
        <!--表单渲染-->
        <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="630px">
          <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">
            <el-form-item label="订单号" prop="orderNumber">
              <el-input v-model="form.orderNumber" />
            </el-form-item>
            <el-form-item label="销售日期" required>
              <el-date-picker
                v-model="form.salesDate"
                type="date"
                placeholder="选择日期"
                size="small"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
            <el-form-item label="销售区域" prop="salesArea">
              <el-input v-model="form.salesArea" />
            </el-form-item>
            <el-form-item label="产品" required>
              <el-select v-model="form.product.id" style="width: 178px" placeholder="请选择产品" @change="getProductName">
                <el-option
                  v-for="(item, index) in products"
                  :key="'products-' + index"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="销量" prop="salesNumber">
              <el-input v-model="form.salesNumber" />
            </el-form-item>
            <el-form-item label="销量单位" prop="salesUnit">
              <el-input v-model="form.salesUnit" placeholder="请输入销量单位" />
            </el-form-item>
            <el-form-item label="单价" prop="price">
              <el-input v-model="form.price" />
            </el-form-item>
            <el-form-item label="价格单位" prop="priceUnit">
              <el-input v-model="form.priceUnit" placeholder="请输入价格单位" />
            </el-form-item>
            <el-form-item label="销售额" prop="sales">
              <el-input v-model="form.sales" />
            </el-form-item>
            <el-form-item label="生产批次" prop="batchNumber">
              <el-input v-model="form.batchNumber" />
            </el-form-item>
            <el-form-item label="组织机构" prop="dept.id" required>
              <treeselect
                v-model="form.dept.id"
                :options="depts"
                style="width: 178px"
                placeholder="请选择组织机构"
              />
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="text" @click="crud.cancelCU">取消</el-button>
            <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
          </div>
        </el-dialog>
        <!--表格渲染-->
        <el-table ref="table" v-loading="crud.loading" :data="crud.data" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
          <el-table-column type="selection" width="55" />
          <el-table-column v-if="columns.visible('name')" :show-overflow-tooltip="true" prop="orderNumber" label="订单号" />
          <el-table-column v-if="columns.visible('dept')" :show-overflow-tooltip="true" prop="product.name" label="产品名称" />
          <el-table-column v-if="columns.visible('dept')" :show-overflow-tooltip="true" prop="product.code" label="产品编码" />
          <el-table-column v-if="columns.visible('code')" :show-overflow-tooltip="true" prop="salesArea" label="销售区域" />
          <el-table-column v-if="columns.visible('category.name')" :show-overflow-tooltip="true" prop="batchNumber" label="生产批次" />
          <el-table-column v-if="columns.visible('category.code')" :show-overflow-tooltip="true" prop="salesNumber" label="销量">
            <template slot-scope="scope">
              <div>{{ scope.row.salesNumber }}  {{ scope.row.salesUnit }}</div>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('scope')" :show-overflow-tooltip="true" prop="price" label="单价">
            <template slot-scope="scope">
              <div>{{ scope.row.price }}  {{ scope.row.priceUnit }}</div>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('region')" :show-overflow-tooltip="true" prop="sales" label="销售额">
            <template slot-scope="scope">
              <div>{{ scope.row.sales }} 元</div>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('remark')" :show-overflow-tooltip="true" prop="dept.name" label="组织机构" />
          <el-table-column v-if="columns.visible('createTime')" :show-overflow-tooltip="true" prop="salesDate" width="140" label="销售日期">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.salesDate) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-permission="['admin','sales-record:edit','sales-record:del']"
            label="操作"
            width="125"
            align="center"
            fixed="right"
          >
            <template slot-scope="scope">
              <udOperation
                :data="scope.row"
                :permission="permission"
              />
            </template>
          </el-table-column>
        </el-table>
        <!--分页组件-->
        <pagination />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import crudSalesRecord from '@/api/product/product-sales'
import { getAllProducts } from '@/api/product/product'
import { getDepts } from '@/api/system/dept'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import Treeselect from '@riophae/vue-treeselect'
import { mapGetters } from 'vuex'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

// crud交由presenter持有
const defaultCrud = CRUD({ title: '销售数据', url: 'api/sales-record', crudMethod: { ...crudSalesRecord }})
const defaultForm = { id: null, orderNumber: null, regionId: null, salesArea: null, salesNumber: null, salesUnit: null, price: null, priceUnit: null, sales: null, salesDate: null, salesStatus: null, remark: null, product: { id: null }, productName: null, productCode: null, dept: { id: null }, deptName: null, batchNumber: null }
export default {
  name: 'ProductsSale',
  components: { Treeselect, crudOperation, rrOperation, udOperation, pagination },
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  // 数据字典
  dicts: ['user_status'],
  data() {
    return {
      height: document.documentElement.clientHeight - 180 + 'px;',
      deptName: '', depts: [], deptDatas: [], products: [],
      defaultProps: { children: 'children', label: 'name' },
      permission: {
        add: ['admin', 'sales-record:add'],
        edit: ['admin', 'sales-record:edit'],
        del: ['admin', 'sales-record:del']
      },
      rules: {
        orderNumber: [
          { required: true, message: '请输入订单号', trigger: 'blur' }
        ],
        salesArea: [
          { required: true, message: '请输入销售地区', trigger: 'blur' }
        ],
        salesNumber: [
          { required: true, message: '请输入产品销量', trigger: 'blur' }
        ],
        salesUnit: [
          { required: true, message: '请输入销量单位', trigger: 'blur' }
        ],
        price: [
          { required: true, message: '请输入产品单价', trigger: 'blur' }
        ],
        priceUnit: [
          { required: true, message: '请输入价格单位', trigger: 'blur' }
        ],
        sales: [
          { required: true, message: '请输入产品销售额', trigger: 'blur' }
        ],
        batchNumber: [
          { required: true, message: '请输入产品生产批次', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters([
      'user'
    ])
  },
  created() {
    this.$nextTick(() => {
      this.getDeptDatas()
      this.crud.toQuery()
    })
  },
  mounted: function() {
    const that = this
    window.onresize = function temp() {
      that.height = document.documentElement.clientHeight - 180 + 'px;'
    }
  },
  methods: {
    [CRUD.HOOK.afterAddError](crud) {
      this.afterErrorMethod(crud)
    },
    [CRUD.HOOK.afterEditError](crud) {
      this.afterErrorMethod(crud)
    },
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      this.getDepts()
      this.form.dept.id = this.user.deptId
      this.form.deptName = this.user.dept
      // 获取产品
      this.getProducts(this.user.deptId)
      this.form.orderNumber = this.randomOrder()
    },
    // 打开编辑弹窗前做的操作
    [CRUD.HOOK.beforeToEdit](crud, form) {
    },
    // 提交前做的操作
    [CRUD.HOOK.afterValidateCU](crud) {
      if (crud.form.dept.id !== this.user.deptId) {
        this.$message({
          message: '不能选择其他组织机构',
          type: 'warning'
        })
        return false
      } else if (!crud.form.product.id) {
        this.$message({
          message: '产品不能为空',
          type: 'warning'
        })
        return false
      } else if (!crud.form.salesDate) {
        this.$message({
          message: '销售时间不能为空',
          type: 'warning'
        })
        return false
      }
      return true
    },
    // 获取左侧部门数据
    getDeptDatas() {
      const sort = 'id,desc'
      const params = { sort: sort }
      if (this.deptName) { params['name'] = this.deptName }
      getDepts(params).then(res => {
        this.deptDatas = res.content
      })
    },
    // 获取弹窗内部门数据
    getDepts() {
      getDepts({ enabled: true }).then(res => {
        this.depts = res.content
      })
    },
    // 切换部门
    handleNodeClick(data) {
      this.query.deptId = data.id
      this.crud.toQuery()
      this.query.deptId = null
    },
    // 获取所有产品
    getProducts(id) {
      getAllProducts(id).then(res => {
        this.products = res.content
      }).catch(() => { })
    },
    // 获取选择产品的名称
    getProductName(val) {
      let obj = {}
      obj = this.products.find((item) => {
        return item.id === val
      })
      this.form.productName = obj.name
      this.form.productCode = obj.code
    },
    getStatus() {
      if (this.form.dataStatus === 0) {
        this.form.pid = 0
      } else {
        this.form.pid = 1
      }
    },
    randomOrder() {
      var random_no = 'JX'
      for (var i = 0; i < 3; i++) {
        random_no += Math.floor(Math.random() * 10)
      }
      random_no = random_no + new Date().getTime()
      return random_no
    },
    // 改变状态
    changeEnabled(data, val) {
      this.$confirm('此操作将 "' + this.dict.label.user_status[val] + '" ' + data.username + ', 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        crudSalesRecord.edit(data).then(res => {
          this.crud.notify(this.dict.label.user_status[val] + '成功', CRUD.NOTIFICATION_TYPE.SUCCESS)
        }).catch(() => {
          data.enabled = !data.enabled
        })
      }).catch(() => {
        data.enabled = !data.enabled
      })
    }
  }
}
</script>

<style scoped>
</style>
