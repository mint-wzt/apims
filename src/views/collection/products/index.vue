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
              placeholder="输入产品名称或代码搜索"
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
            <rrOperation :crud="crud" />
          </div>
          <crudOperation show="" :permission="permission" />
        </div>
        <!--表单渲染-->
        <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="600px">
          <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">
            <el-form-item label="产品名称" prop="name">
              <el-input v-model="form.name" />
            </el-form-item>
            <el-form-item label="产品代码" prop="code">
              <el-input v-model.number="form.code" />
            </el-form-item>
            <el-form-item label="使用范围" prop="scope">
              <el-input v-model="form.scope" />
            </el-form-item>
            <el-form-item label="产品分类" prop="category.id">
              <treeselect
                v-model="form.category.id"
                :options="categories"
                :props="defaultProps"
                style="width: 178px"
                placeholder="选择分类"
              />
            </el-form-item>
            <el-form-item label="生产商" prop="dept.id">
              <treeselect
                v-model="form.dept.id"
                :options="depts"
                style="width: 178px"
                placeholder="选择部门"
              />
            </el-form-item>
            <el-form-item label="状态" prop="enabled">
              <el-radio-group v-model="form.enabled">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">停用</el-radio>
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
          <el-table-column v-if="columns.visible('name')" :show-overflow-tooltip="true" prop="name" label="产品名称" />
          <el-table-column v-if="columns.visible('code')" :show-overflow-tooltip="true" prop="code" label="产品代码" />
          <el-table-column v-if="columns.visible('category.name')" :show-overflow-tooltip="true" prop="category.name" label="分类名称" />
          <el-table-column v-if="columns.visible('category.code')" :show-overflow-tooltip="true" prop="category.code" label="分类编码" />
          <el-table-column v-if="columns.visible('scope')" :show-overflow-tooltip="true" prop="scope" label="使用范围" />
          <el-table-column v-if="columns.visible('region')" :show-overflow-tooltip="true" prop="region.extName" label="地区" />
          <el-table-column v-if="columns.visible('dept')" :show-overflow-tooltip="true" prop="dept.name" label="生产商" />
          <el-table-column v-if="columns.visible('enabled')" label="状态" align="center" prop="enabled">
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.enabled === 0 ? 'danger' : 'success'"
                disable-transitions
              >{{ scope.row.enabled === 1 ? '启用' : '停用' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('remark')" :show-overflow-tooltip="true" prop="remark" label="说明" />
          <el-table-column v-if="columns.visible('createTime')" :show-overflow-tooltip="true" prop="createTime" width="140" label="创建日期">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-permission="['admin','product:edit','product:del']"
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
import crudProduct from '@/api/product/product'
import { getCategoryTree } from '@/api/product/category'
import { getDepts } from '@/api/system/dept'
import { getAll, getLevel } from '@/api/system/role'
import { getAllJob } from '@/api/system/job'
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
const defaultCrud = CRUD({ title: '产品', url: 'api/product', crudMethod: { ...crudProduct }})
const defaultForm = { id: null, name: null, code: null, productType: null, scope: null, enabled: 1, remark: '-', roles: [], category: { id: null }, region: { id: null }, dept: { id: null }, job: { id: null }}
export default {
  name: 'Products',
  components: { Treeselect, crudOperation, rrOperation, udOperation, pagination },
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  // 数据字典
  dicts: ['user_status'],
  data() {
    return {
      height: document.documentElement.clientHeight - 180 + 'px;',
      deptName: '', depts: [], categories: [], deptDatas: [], jobs: [], level: 3, roles: [],
      defaultProps: { children: 'children', label: 'name' },
      permission: {
        add: ['admin', 'product:add'],
        edit: ['admin', 'product:edit'],
        del: ['admin', 'product:del']
      },
      status: [
        { id: 1, value: 1, label: '启用' },
        { id: 0, value: 0, label: '停用' }
      ],
      enabledTypeOptions: [
        { key: 'true', display_name: '激活' },
        { key: 'false', display_name: '锁定' }
      ],
      rules: {
        name: [
          { required: true, message: '请输入产品名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入产品编码', trigger: 'blur' }
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
      this.getDepts()
      this.getCategories()
      this.getRoles()
      this.getRoleLevel()
      form.enabled = form.enabled.toString()
    },
    // 打开编辑弹窗前做的操作
    [CRUD.HOOK.beforeToEdit](crud, form) {
      this.getJobs(this.form.dept.id)
      userRoles = []
      const roles = []
      form.roles.forEach(function(role, index) {
        roles.push(role.id)
        // 初始化编辑时候的角色
        const rol = { id: role.id }
        userRoles.push(rol)
      })
      form.roles = roles
    },
    // 提交前做的操作
    [CRUD.HOOK.afterValidateCU](crud) {
      if (!crud.form.dept.id) {
        this.$message({
          message: '部门不能为空',
          type: 'warning'
        })
        return false
      } else if (!crud.form.category.id) {
        this.$message({
          message: '产品类型不能为空',
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
    getCategories() {
      getCategoryTree({ enabled: 1 }).then(res => {
        this.categories = res
      })
    },
    // 切换部门
    handleNodeClick(data) {
      // if (data.pid === 0) {
      //   this.query.deptId = null
      // } else {
      //   this.query.deptId = data.id
      // }
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
        crudProduct.edit(data).then(res => {
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
    // 获取权限级别
    getRoleLevel() {
      getLevel().then(res => {
        this.level = res.level
      }).catch(() => { })
    }
  }
}
</script>

<style scoped>
</style>
