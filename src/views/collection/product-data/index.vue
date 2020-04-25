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
              v-model="query.manufactureDate"
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
            <el-form-item label="信息类型" prop="type">
              <el-radio-group v-model="form.dataStatus" size="small" style="width: 178px">
                <el-radio-button :label="0" @change="getStatus">产品信息</el-radio-button>
                <el-radio-button :label="1" @change="getStatus">生产信息</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="组织机构" prop="dept.id" required>
              <treeselect
                v-model="form.dept.id"
                :options="depts"
                style="width: 178px"
                placeholder="选择部门"
              />
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
            <el-form-item label="生产日期" required>
              <el-date-picker
                v-model="form.manufactureDate"
                type="date"
                placeholder="选择日期"
                size="small"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
            <el-form-item v-show="form.dataStatus.toString() !== '0'" label="生产批次" prop="batchNumber">
              <el-input v-model="form.batchNumber" />
            </el-form-item>
            <el-form-item v-show="form.dataStatus.toString() !== '0'" label="原料批次" prop="materialBatch">
              <el-input v-model="form.materialBatch" />
            </el-form-item>
            <el-form-item v-show="form.dataStatus.toString() === '0'" label="种植面积" prop="area">
              <el-input v-model="form.area" />
            </el-form-item>
            <el-form-item v-show="form.dataStatus.toString() === '0'" label="面积单位" prop="areaUnit">
              <el-input v-model="form.areaUnit" placeholder="请输入面积单位" />
            </el-form-item>
            <el-form-item v-show="form.dataStatus.toString() === '0'" label="养殖数量" prop="count">
              <el-input v-model="form.count" />
            </el-form-item>
            <el-form-item v-show="form.dataStatus.toString() === '0'" label="数量单位" prop="countUnit">
              <el-input v-model="form.countUnit" placeholder="请输入数量单位" />
            </el-form-item>
            <el-form-item label="产量" prop="output">
              <el-input v-model="form.output" />
            </el-form-item>
            <el-form-item label="产量单位" prop="outputUnit">
              <el-input v-model="form.outputUnit" placeholder="请输入产量单位" />
            </el-form-item>
            <el-form-item v-show="form.dataStatus.toString() === '0'" label="行业" prop="productType">
              <el-select v-model="form.productType" style="width: 450px" placeholder="请选择行业" @change="getIndustry">
                <el-option
                  v-for="(item, index) in industries"
                  :key="'industry-' + index"
                  :label="item.name"
                  :value="item.name"
                />
              </el-select>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="text" @click="crud.cancelCU">取消</el-button>
            <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
          </div>
        </el-dialog>
        <!--表格渲染-->
        <el-table ref="table" v-loading="crud.loading" :data="crud.data" :tree-props="{children: 'children', hasChildren: 'hasChildren'}" row-key="id" @select="crud.selectChange" @select-all="crud.selectAllChange" @selection-change="crud.selectionChangeHandler">
          <el-table-column type="selection" width="55" />
          <el-table-column v-if="columns.visible('product.name')" :show-overflow-tooltip="true" label="产品名称" prop="product.name" />
          <el-table-column v-if="columns.visible('product.code')" :show-overflow-tooltip="true" prop="product.code" label="产品代码" />
          <el-table-column v-if="columns.visible('batchNumber')" :show-overflow-tooltip="true" prop="batchNumber" label="生产批次" />
          <el-table-column v-if="columns.visible('materialBatch')" :show-overflow-tooltip="true" prop="materialBatch" label="原料批次" />
          <el-table-column v-if="columns.visible('manufactureDate')" :show-overflow-tooltip="true" prop="manufactureDate" label="生产日期">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.manufactureDate) }}</span>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('area')" :show-overflow-tooltip="true" prop="area" label="种植面积">
            <template slot-scope="scope">
              <div>{{ scope.row.area }}  {{ scope.row.areaUnit }}</div>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('count')" :show-overflow-tooltip="true" prop="count" label="养殖数量">
            <template slot-scope="scope">
              <div>{{ scope.row.count }}  {{ scope.row.countUnit }}</div>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('output')" :show-overflow-tooltip="true" prop="output" label="产量">
            <template slot-scope="scope">
              <div>{{ scope.row.output }}  {{ scope.row.outputUnit }}</div>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('dept.name')" :show-overflow-tooltip="true" prop="dept.name" label="组织机构" />
          <el-table-column v-if="columns.visible('productType')" :show-overflow-tooltip="true" prop="productType" label="行业" />
          <el-table-column v-permission="['admin','product-data:edit','product-data:del']" label="操作" width="130px" align="center" fixed="right">
            <template slot-scope="scope">
              <udOperation
                :data="scope.row"
                :permission="permission"
                msg="确定删除吗,如果存在下级节点则一并删除，此操作不能撤销！"
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
import crudProductData from '@/api/product/product-data'
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
const defaultCrud = CRUD({ title: '生产记录', url: 'api/product-data', crudMethod: { ...crudProductData }})
const defaultForm = { id: null, product: { id: null }, productName: null, dept: { id: null }, deptName: null, batchNumber: null, pid: 0, materialBatch: null, manufactureDate: null, area: null, areaUnit: null, count: null, countUnit: null, output: null, outputUnit: null, productType: null, remark: '-', dataStatus: 0 }
export default {
  name: 'ProductData',
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
        add: ['admin', 'product-data:add'],
        edit: ['admin', 'product-data:edit'],
        del: ['admin', 'product-data:del']
      },
      rules: {
        output: [
          { required: true, message: '请输入产量', trigger: 'blur' }
        ],
        outputUnit: [
          { required: true, message: '请输入计数单位', trigger: 'blur' }
        ]
      },
      industries: [
        {
          id: 1,
          name: '种植业'
        },
        {
          id: 2,
          name: '畜牧业'
        },
        {
          id: 3,
          name: '渔业'
        }
      ]
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
      } else if (!crud.form.manufactureDate) {
        this.$message({
          message: '采购时间不能为空',
          type: 'warning'
        })
        return false
      } else if (crud.form.dataStatus === 0 && !crud.form.areaUnit && !crud.form.countUnit) {
        this.$message({
          message: '种植面积或养殖数量不能为空',
          type: 'warning'
        })
        return false
      } else if (crud.form.dataStatus === 1 && !crud.form.batchNumber) {
        this.$message({
          message: '生产批次不能为空',
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
    getIndustry(value) {

    },
    getStatus() {
      if (this.form.dataStatus === 0) {
        this.form.pid = 0
      } else {
        this.form.pid = 1
      }
    },
    // 改变状态
    changeEnabled(data, val) {
      this.$confirm('此操作将 "' + this.dict.label.user_status[val] + '" ' + data.username + ', 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        crudProductData.edit(data).then(res => {
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
