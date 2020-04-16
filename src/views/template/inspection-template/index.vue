<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input
          v-model="query.blurry"
          clearable
          size="small"
          placeholder="输入名称或者类型搜索"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <el-date-picker
          v-model="query.createTime"
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
          v-model="query.enabled"
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
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="600px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="78px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="类型" prop="itType">
          <el-input v-model="form.itType" />
        </el-form-item>
        <el-form-item label="范围" prop="scope">
          <el-input v-model="form.scope" />
        </el-form-item>
        <el-form-item label="组织/机构">
          <treeselect
            v-model="form.dept.id"
            :options="depts"
            style="width: 178px"
            placeholder="选择组织/机构"
          />
          <!--            @select="selectFun"-->
        </el-form-item>
        <el-form-item label="产品" prop="product.id">
          <el-select v-model="form.product.id" style="width: 178px" placeholder="请选择产品">
            <el-option
              v-for="(item, index) in products"
              :key="'products-' + index"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="enabled">
          <el-radio-group v-model="form.enabled">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" style="width: 435px;" />
        </el-form-item>
        <el-form-item label="检测项" prop="roles">
          <el-select
            v-model="form.inspectionItems"
            style="width: 437px"
            multiple
            placeholder="请选择检测项"
            @remove-tag="deleteTag"
            @change="changeRole"
          >
            <el-option
              v-for="item in inspections"
              :key="'inspections-' + item.id"
              :label="item.name"
              :value="item.id"
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
    <el-table ref="table" v-loading="crud.loading" :data="crud.data" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
      <el-table-column :selectable="checkboxT" type="selection" width="55" />
      <el-table-column v-if="columns.visible('name')" :show-overflow-tooltip="true" prop="name" label="模板名称" />
      <el-table-column v-if="columns.visible('itType')" :show-overflow-tooltip="true" prop="itType" label="类型" />
      <el-table-column v-if="columns.visible('scope')" prop="scope" label="使用范围" />
      <el-table-column v-if="columns.visible('user.username')" :show-overflow-tooltip="true" width="125" prop="user.username" label="使用者" />
      <el-table-column v-if="columns.visible('product.name')" :show-overflow-tooltip="true" width="110" prop="product.name" label="检测产品" />
      <el-table-column v-if="columns.visible('remark')" :show-overflow-tooltip="true" width="125" prop="remark" label="备注" />
      <el-table-column v-if="columns.visible('enabled')" label="状态" align="center" prop="enabled">
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.enabled === 0 ? 'danger' : 'success'"
            disable-transitions
          >{{ scope.row.enabled === 1 ? '启用' : '停用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="columns.visible('createTime')" :show-overflow-tooltip="true" prop="createTime" width="140" label="创建日期">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        v-permission="['admin','inspection-template:edit','inspection-template:del']"
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
  </div>
</template>

<script>
import crudInspectionTemplate from '@/api/template/inspection-template'
import { getDepts } from '@/api/system/dept'
import { getAllInspections } from '@/api/product/inspection-item'
import { getAllProducts } from '@/api/product/product'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import Treeselect from '@riophae/vue-treeselect'
import { mapGetters } from 'vuex'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

let selectedInspections = []
// crud交由presenter持有
const defaultCrud = CRUD({ title: '检测模板', url: 'api/inspection-template', crudMethod: { ...crudInspectionTemplate }})
const defaultForm = { id: null, name: null, itType: null, scope: null, remark: '-', enabled: 1, inspectionItems: [], product: { id: null }, dept: { id: null }}
export default {
  name: 'InspectionTemplate',
  components: { Treeselect, crudOperation, rrOperation, udOperation, pagination },
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  // 数据字典
  dicts: ['user_status'],
  data() {
    return {
      height: document.documentElement.clientHeight - 180 + 'px;',
      depts: [], jobs: [], level: 3, roles: [], inspections: [], products: [],
      defaultProps: { children: 'children', label: 'name' },
      dept: { id: null },
      permission: {
        add: ['admin', 'inspection-template:add'],
        edit: ['admin', 'inspection-template:edit'],
        del: ['admin', 'inspection-template:del']
      },
      enabledTypeOptions: [
        { key: 1, display_name: '启用' },
        { key: 0, display_name: '停用' }
      ],
      rules: {
        name: [
          { required: true, message: '请输入检测模板名称', trigger: 'blur' }
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
    changeRole(value) {
      selectedInspections = []
      value.forEach(function(data, index) {
        const inspection = { id: data }
        selectedInspections.push(inspection)
      })
    },
    [CRUD.HOOK.afterAddError](crud) {
      this.afterErrorMethod(crud)
    },
    [CRUD.HOOK.afterEditError](crud) {
      this.afterErrorMethod(crud)
    },
    afterErrorMethod(crud) {
      // 恢复select
      const initItems = []
      selectedInspections.forEach(function(item, index) {
        initItems.push(item.id)
      })
      crud.form.inspectionItems = initItems
    },
    deleteTag(value) {
      selectedInspections.forEach(function(data, index) {
        if (data.id === value) {
          selectedInspections.splice(index, value)
        }
      })
    },
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      this.getDepts()
      // this.getRoles()
      // this.getProducts()
      this.getInspections()
      this.form.dept.id = this.user.deptId
      this.getProducts(this.form.dept.id)
    },
    // 打开编辑弹窗前做的操作
    [CRUD.HOOK.beforeToEdit](crud, form) {
      this.getProducts(this.form.dept.id)
      selectedInspections = []
      const inspections = []
      form.inspectionItems.forEach(function(item, index) {
        inspections.push(item.id)
        // 初始化编辑时候的角色
        const rol = { id: item.id }
        selectedInspections.push(rol)
      })
      form.inspectionItems = inspections
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
      } else if (this.inspections.length === 0) {
        this.$message({
          message: '检测项不能为空',
          type: 'warning'
        })
        return false
      }
      crud.form.inspectionItems = selectedInspections
      return true
    },
    // 获取弹窗内部门数据
    getDepts() {
      getDepts({ enabled: true }).then(res => {
        this.depts = res.content
      })
    },
    // 获取所有的检测项
    getInspections() {
      getAllInspections().then(res => {
        this.inspections = res
      }).catch(() => { })
    },
    // 获取所有产品
    getProducts(id) {
      getAllProducts(id).then(res => {
        this.products = res.content
        console.log(JSON.stringify(this.products))
      }).catch(() => { })
    },
    checkboxT(row, rowIndex) {
      return row.username !== this.user.username
    }
  }
}
</script>

<style scoped>
</style>
