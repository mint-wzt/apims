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
            placeholder="输入组织机构名称搜索"
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
              placeholder="输入检测码、批次或检测员搜索"
              style="width: 200px;"
              class="filter-item"
              @keyup.enter.native="crud.toQuery"
            />
            <el-date-picker
              v-model="query.inspectTime"
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
              v-model="query.isPassed"
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
            <el-form-item label="检测码" required>
              <el-input v-model="form.inspectionRecord.code" />
            </el-form-item>
            <el-form-item label="检测时间">
              <el-date-picker
                v-model="form.inspectionRecord.inspectTime"
                type="date"
                placeholder="选择日期"
                size="small"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
            <el-form-item label="检测批次" required>
              <el-input v-model="form.inspectionRecord.batchNumber" />
            </el-form-item>
            <el-form-item label="组织机构">
              <treeselect
                v-model="form.inspectionRecord.dept.id"
                :options="depts"
                style="width: 178px"
                placeholder="选择组织/机构"
              />
            </el-form-item>
            <el-form-item label="检测员" required>
              <el-input v-model="form.inspectionRecord.inspector" />
            </el-form-item>
            <el-form-item label="检测产品" prop="product">
              <el-select v-model="form.inspectionRecord.product.id" style="width: 178px" placeholder="请选择产品">
                <el-option
                  v-for="(item, index) in products"
                  :key="'products-' + index"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="检测方法" prop="inspectMethod">
              <el-input v-model="form.inspectMethod" />
            </el-form-item>
            <el-form-item label="检测模板" prop="template">
              <el-select v-model="form.inspectionTemplate.id" style="width: 178px" placeholder="请选择模板" @change="getInspectionItems">
                <el-option
                  v-for="(item, index) in templates"
                  :key="'products-' + index"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-divider>检测项</el-divider>
            <el-form-item
              v-for="item in form.inspectionItems"
              :key="item.id+'/'+item.code"
              :label="item.name"
              prop="inspectItem"
            >
              <el-input v-model="item.itemValue" :placeholder="'输入检测值: '+item.unit" />
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
          <el-table-column v-if="columns.visible('code')" :show-overflow-tooltip="true" prop="code" label="检测编码" />
          <el-table-column v-if="columns.visible('batchNumber')" :show-overflow-tooltip="true" prop="batchNumber" label="批次" />
          <el-table-column v-if="columns.visible('product.name')" :show-overflow-tooltip="true" prop="product.name" label="产品名称" />
          <el-table-column v-if="columns.visible('dept')" :show-overflow-tooltip="true" prop="dept.name" label="组织机构" />
          <el-table-column v-if="columns.visible('inspector')" prop="inspector" label="检测员" />
          <el-table-column v-if="columns.visible('inspectTime')" :show-overflow-tooltip="true" prop="inspectTime" label="检测日期">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.inspectTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('isPassed')" :show-overflow-tooltip="true" label="检测结果" align="center" prop="isPassed">
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.isPassed === 0 ? 'danger' : 'success'"
                disable-transitions
              >{{ scope.row.isPassed === 1 ? '合格' : '不合格' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column
            v-permission="['admin','products-detection:edit','products-detection:del']"
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
import crudInspectionRecord from '@/api/product/pruducts-detection'
import { getDepts } from '@/api/system/dept'
import { getAll, getLevel } from '@/api/system/role'
import { getAllJob } from '@/api/system/job'
import { getAllProducts } from '@/api/product/product'
import { getAllTemplates } from '@/api/template/inspection-template'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import Treeselect from '@riophae/vue-treeselect'
import { mapGetters } from 'vuex'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

let userRoles = []
// crud交由presenter持有
const defaultCrud = CRUD({ title: '产品检测', url: 'api/inspection-record', crudMethod: { ...crudInspectionRecord }})
const defaultForm = { inspectionRecord: { id: null, code: null, batchNumber: null, inspector: null, inspectTime: null, product: { id: null }, dept: { id: null }}, inspectionTemplate: { id: null }, inspectMethod: null, inspectionItems: [], enabled: 'false', roles: [], job: { id: null }, dept: { id: null }, phone: null }
export default {
  name: 'ProductsDetection',
  components: { Treeselect, crudOperation, rrOperation, udOperation, pagination },
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  // 数据字典
  dicts: ['user_status'],
  data() {
    return {
      height: document.documentElement.clientHeight - 180 + 'px;',
      deptName: '', depts: [], deptDatas: [], jobs: [], level: 3, products: [], templates: [], inspections: [],
      defaultProps: { children: 'children', label: 'name' },
      permission: {
        add: ['admin', 'products-detection:add'],
        edit: ['admin', 'products-detection:edit'],
        del: ['admin', 'products-detection:del']
      },
      enabledTypeOptions: [
        { key: 1, display_name: '合格' },
        { key: 0, display_name: '不合格' }
      ],
      rules: {

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
    changeRole(value) {
      userRoles = []
      value.forEach(function(data, index) {
        const role = { id: data }
        userRoles.push(role)
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
      const initRoles = []
      userRoles.forEach(function(role, index) {
        initRoles.push(role.id)
      })
      crud.form.roles = initRoles
    },
    deleteTag(value) {
      userRoles.forEach(function(data, index) {
        if (data.id === value) {
          userRoles.splice(index, value)
        }
      })
    },
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      // 获取组织机构
      this.getDepts()
      this.form.inspectionRecord.dept.id = this.user.deptId
      // 获取产品
      this.getProducts(this.user.deptId)
      // 获取模板
      this.getTemplates()
      // this.getRoles()
      // this.getRoleLevel()
      // form.enabled = form.enabled.toString()
    },
    // 打开编辑弹窗前做的操作
    [CRUD.HOOK.beforeToEdit](crud, form) {
      this.$message({
        message: '检测记录不能修改',
        type: 'warning'
      })
      return false
    },
    // 提交前做的操作
    [CRUD.HOOK.afterValidateCU](crud) {
      if (!crud.form.inspectionRecord.code || !crud.form.inspectionRecord.batchNumber || !crud.form.inspectionRecord.inspector) {
        this.$message({
          message: '必填项不能为空',
          type: 'warning'
        })
        return false
      } else if (crud.form.inspectionRecord.dept.id !== this.user.deptId) {
        this.$message({
          message: '不能选择其他组织机构',
          type: 'warning'
        })
        return false
      } else if (!crud.form.inspectionRecord.inspectTime) {
        this.$message({
          message: '检测时间不能为空',
          type: 'warning'
        })
        return false
      } else if (!crud.form.inspectionRecord.product.id) {
        this.$message({
          message: '检测产品不能为空',
          type: 'warning'
        })
        return false
      } else if (!crud.form.inspectionTemplate.id) {
        this.$message({
          message: '检测模板不能为空',
          type: 'warning'
        })
        return false
      }
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
    // 改变状态
    changeEnabled(data, val) {
      this.$confirm('此操作将 "' + this.dict.label.user_status[val] + '" ' + data.username + ', 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        crudInspectionRecord.edit(data).then(res => {
          this.crud.notify(this.dict.label.user_status[val] + '成功', CRUD.NOTIFICATION_TYPE.SUCCESS)
        }).catch(() => {
          data.enabled = !data.enabled
        })
      }).catch(() => {
        data.enabled = !data.enabled
      })
    },
    // 获取弹窗内角色数据
    getRoles() {
      getAll().then(res => {
        this.roles = res
      }).catch(() => { })
    },
    // 获取弹窗内岗位数据
    getJobs(id) {
      getAllJob(id).then(res => {
        this.jobs = res.content
      }).catch(() => { })
    },
    // 点击部门搜索对应的岗位
    selectFun(node, instanceId) {
      this.getJobs(node.id)
      this.form.job.id = null
    },
    // 获取权限级别
    getRoleLevel() {
      getLevel().then(res => {
        this.level = res.level
      }).catch(() => { })
    },
    // 获取所有产品
    getProducts(id) {
      getAllProducts(id).then(res => {
        this.products = res.content
      }).catch(() => { })
    },
    // 获取所有模板
    getTemplates() {
      getAllTemplates({ userId: this.user.id }).then(res => {
        this.templates = res.content
      }).catch(() => { })
    },
    // 获取模板的检测项
    getInspectionItems(id) {
      getAllTemplates({ id: id }).then(res => {
        this.form.inspectionItems = res.content[0].inspectionItems
        console.log(JSON.stringify(this.form.inspectionItems))
      }).catch(() => { })
    }
  }
}
</script>

<style scoped>
</style>
