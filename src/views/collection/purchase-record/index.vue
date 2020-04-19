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
              placeholder="模糊搜索"
              style="width: 200px;"
              class="filter-item"
              @keyup.enter.native="crud.toQuery"
            />
            <el-date-picker
              v-model="query.purchaseTime"
              :default-time="['00:00:00','23:59:59']"
              type="daterange"
              range-separator=":"
              size="small"
              class="date-item"
              value-format="yyyy-MM-dd HH:mm:ss"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            />
            <el-select
              v-model="query.recordStatus"
              clearable
              size="small"
              placeholder="状态"
              class="filter-item"
              style="width: 90px"
              @change="crud.toQuery"
            >
              <el-option
                v-for="item in enabledTypeOptions"
                :key="item.key"
                :label="item.display_name"
                :value="item.key"
              />
            </el-select>
            <rrOperation :crud="crud" />
          </div>
          <crudOperation show="" :permission="permission" />
        </div>
        <!--表单渲染-->
        <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="620px">
          <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">
            <el-form-item label="原料名称" prop="goodsName">
              <el-input v-model="form.goodsName" />
            </el-form-item>
            <el-form-item label="采购时间" required>
              <el-date-picker
                v-model="form.purchaseTime"
                type="date"
                placeholder="选择日期"
                size="small"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
            <el-form-item label="采购批次" prop="batchNumber">
              <el-input v-model.number="form.batchNumber" />
            </el-form-item>
            <el-form-item label="采购数量" prop="purchaseQuantity">
              <el-input v-model="form.purchaseQuantity" />
            </el-form-item>
            <el-form-item label="计数单位" prop="unit">
              <el-input v-model="form.unit" placeholder="千克/kg、克/g、升/L、毫升/mL等" />
            </el-form-item>
            <el-form-item label="生产商" prop="producerName">
              <el-input v-model="form.producerName" />
            </el-form-item>
            <el-form-item label="地址" prop="producerAddress">
              <el-input v-model="form.producerAddress" />
            </el-form-item>
            <el-form-item label="联系方式" prop="producerContact">
              <el-input v-model="form.producerContact" placeholder="电话或者email" />
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
            <el-form-item label="组织机构" prop="dept.id">
              <treeselect
                v-model="form.dept.id"
                :options="depts"
                style="width: 178px"
                placeholder="选择部门"
              />
            </el-form-item>
            <el-form-item label="状态" prop="enabled">
              <el-radio-group v-model="form.recordStatus">
                <el-radio :label="1">提交</el-radio>
                <el-radio :label="0">草稿</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="描述信息" prop="remark">
              <el-input v-model="form.remark" style="width: 430px;" rows="5" type="textarea" />
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
          <el-table-column v-if="columns.visible('goodsName')" :show-overflow-tooltip="true" prop="goodsName" label="原料名称" />
          <el-table-column v-if="columns.visible('batchNumber')" :show-overflow-tooltip="true" prop="batchNumber" label="批次" />
          <el-table-column v-if="columns.visible('purchaseTime')" :show-overflow-tooltip="true" prop="purchaseTime" label="采购时间">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.purchaseTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('purchaseQuantity')" :show-overflow-tooltip="true" prop="purchaseQuantity" label="采购数量">
            <template slot-scope="scope">
              <div>{{ scope.row.purchaseQuantity }}  {{ scope.row.unit }}</div>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('producerName')" :show-overflow-tooltip="true" prop="producerName" label="生产商" />
          <el-table-column v-if="columns.visible('producerAddress')" :show-overflow-tooltip="true" prop="producerAddress" label="生产商地址" />
          <el-table-column v-if="columns.visible('producerContact')" :show-overflow-tooltip="true" prop="producerContact" label="联系方式" />
          <el-table-column v-if="columns.visible('product.name')" :show-overflow-tooltip="true" prop="product.name" label="产品名称" />
          <el-table-column v-if="columns.visible('product.code')" :show-overflow-tooltip="true" prop="product.code" label="产品代码" />
          <el-table-column v-if="columns.visible('recordStatus')" label="状态" align="center" prop="recordStatus">
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.recordStatus === 0 ? 'danger' : 'success'"
                disable-transitions
              >{{ scope.row.recordStatus === 1 ? '已提交' : '草稿' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column
            v-permission="['admin','purchase-record:edit','purchase-record:del']"
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
import crudProduct from '@/api/product/purchase-record'
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
const defaultCrud = CRUD({ title: '原材料采购记录', url: 'api/purchase-record', crudMethod: { ...crudProduct }})
const defaultForm = { id: null, product: { id: null }, dept: { id: null }, productName: null, batchNumber: null, purchaseQuantity: null, unit: null, goodsName: null, producerName: null, producerAddress: null, producerContact: null, purchaseTime: null, recordStatus: 1, remark: '-' }
export default {
  name: 'PurchaseRecord',
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
        add: ['admin', 'purchase-record:add'],
        edit: ['admin', 'purchase-record:edit'],
        del: ['admin', 'purchase-record:del']
      },
      enabledTypeOptions: [
        { key: 1, display_name: '已提交' },
        { key: 0, display_name: '草稿' }
      ],
      rules: {
        goodsName: [
          { required: true, message: '请输入原料名称', trigger: 'blur' }
        ],
        batchNumber: [
          { required: true, message: '请输入采购批次', trigger: 'blur' }
        ],
        purchaseQuantity: [
          { required: true, message: '请输入采购数量', trigger: 'blur' }
        ],
        unit: [
          { required: true, message: '请输入计数单位', trigger: 'blur' }
        ],
        producerName: [
          { required: true, message: '请输入生产商名称', trigger: 'blur' }
        ],
        producerAddress: [
          { required: true, message: '请输入生产商地址', trigger: 'blur' }
        ],
        producerContact: [
          { required: true, message: '请输入生产商联系方式', trigger: 'blur' }
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
      // 获取产品
      this.getProducts(this.user.deptId)
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
      } else if (!crud.form.purchaseTime) {
        this.$message({
          message: '采购时间不能为空',
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
    },
    // 改变状态
    changeEnabled(data, val) {
      this.$confirm('此操作将 "' + this.dict.label.user_status[val] + '" ' + data.username + ', 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        crudProduct.edit(data).then(res => {
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
