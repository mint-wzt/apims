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
            placeholder="输入部门名称搜索"
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
              placeholder="输入姓名或者地址搜索"
              style="width: 200px;"
              class="filter-item"
              @keyup.enter.native="crud.toQuery"
            />
            <rrOperation :crud="crud" />
          </div>
          <crudOperation show="" :permission="permission" />
        </div>
        <!--表单渲染-->
        <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="570px">
          <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="76px">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" />
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="form.sex" style="width: 178px">
                <el-radio label="男">男</el-radio>
                <el-radio label="女">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="编号" prop="number">
              <el-input v-model="form.number" />
            </el-form-item>
            <el-form-item label="机构" prop="dept.id">
              <treeselect
                v-model="form.dept.id"
                :options="depts"
                style="width: 178px"
                placeholder="选择部门"
                @select="selectFun"
              />
            </el-form-item>
            <el-form-item label="职位" prop="position">
              <el-input v-model="form.position" />
            </el-form-item>
            <el-form-item label="电话" prop="contact">
              <el-input v-model="form.contact" />
            </el-form-item>
            <el-form-item label="身份证" prop="idNumber">
              <el-input v-model="form.idNumber" />
            </el-form-item>
            <el-form-item label="种植面积" prop="cultivatedArea">
              <el-input v-model="form.cultivatedArea" placeholder="xx亩" />
            </el-form-item>
            <el-form-item label="所在地区" prop="region" style="width: 370px;">
              <v-region type="column" :town="true" @values="regionChange" />
            </el-form-item>
            <el-form-item label="详细地址" prop="address">
              <el-input v-model="form.address" style="width: 370px;" placeholder="如村、道路、门牌号、小区、楼栋号、单元室等" />
            </el-form-item>
            <el-form-item label="健康情况" prop="healthCondition">
              <el-input v-model="form.healthCondition" />
            </el-form-item>
            <el-form-item label="致贫原因" prop="causePoverty">
              <el-input v-model="form.causePoverty" />
            </el-form-item>
            <el-form-item label="家庭情况" prop="familySituation">
              <el-input v-model="form.familySituation" placeholder="家庭人口数等情况" />
            </el-form-item>
            <el-form-item label="教育水平" prop="educationLevel">
              <!--              <el-input v-model="form.educationLevel" />-->
              <el-select v-model="form.educationLevel" placeholder="请选择教育水平">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="负责人" prop="responsiblePerson">
              <el-input v-model="form.responsiblePerson" />
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
          <el-table-column v-if="columns.visible('number')" :show-overflow-tooltip="true" prop="number" label="编号" />
          <el-table-column v-if="columns.visible('name')" :show-overflow-tooltip="true" prop="name" label="姓名" />
          <el-table-column v-if="columns.visible('sex')" :show-overflow-tooltip="true" prop="sex" label="性别" />
          <el-table-column v-if="columns.visible('dept')" :show-overflow-tooltip="true" width="125" prop="dept" label="部门 / 岗位">
            <template slot-scope="scope">
              <div>{{ scope.row.dept.name }} / {{ scope.row.position }}</div>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('address')" :show-overflow-tooltip="true" width="125" prop="address" label="家庭住址" />
          <el-table-column v-if="columns.visible('contact')" :show-overflow-tooltip="true" prop="contact" width="100" label="电话" />
          <el-table-column v-if="columns.visible('idNumber')" :show-overflow-tooltip="true" prop="idNumber" width="100" label="身份证号" />
          <el-table-column v-if="columns.visible('cultivatedArea')" :show-overflow-tooltip="true" prop="cultivatedArea" width="100" label="种植面积">
            <template slot-scope="scope">
              <span>{{ scope.row.cultivatedArea }} 亩</span>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('responsiblePerson')" :show-overflow-tooltip="true" prop="responsiblePerson" width="100" label="负责人" />
          <el-table-column
            v-permission="['admin','employee:edit','employee:del']"
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
import crudEmployee from '@/api/system/employee'
import { isvalidPhone } from '@/utils/validate'
import { getDepts } from '@/api/system/dept'
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
const defaultCrud = CRUD({ title: '用户', url: 'api/employee', crudMethod: { ...crudEmployee }})
const defaultForm = { id: null, number: null, name: null, sex: '男', position: null, dept: { id: null, name: null },
  contact: null, idNumber: null, address: null, healthCondition: null, causePoverty: null,
  responsiblePerson: null, educationLevel: null, familySituation: null, cultivatedArea: null, email: null, enabled: 'false', phone: null }
export default {
  name: 'Employee',
  components: { Treeselect, crudOperation, rrOperation, udOperation, pagination },
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  // 数据字典
  dicts: ['user_status'],
  data() {
    // 自定义验证
    const validPhone = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入电话号码'))
      } else if (!isvalidPhone(value)) {
        callback(new Error('请输入正确的11位手机号码'))
      } else {
        callback()
      }
    }
    // 自定义验证
    // const validIDNumber = (rule, value, callback) => {
    //   if (!value) {
    //     callback(new Error('请输入身份证号码'))
    //   } else if (!validateIdNo(value)) {
    //     callback(new Error('请输入正确的身份证号码'))
    //   } else {
    //     callback()
    //   }
    // }
    return {
      height: document.documentElement.clientHeight - 180 + 'px;',
      deptName: '', depts: [], deptDatas: [], jobs: [], level: 3, roles: [],
      defaultProps: { children: 'children', label: 'name' },
      permission: {
        add: ['admin', 'employee:add'],
        edit: ['admin', 'employee:edit'],
        del: ['admin', 'employee:del']
      },
      enabledTypeOptions: [
        { key: 'true', display_name: '激活' },
        { key: 'false', display_name: '锁定' }
      ],
      rules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        contact: [
          { required: true, trigger: 'blur', validator: validPhone }
        ]
        // idNumber: [
        //   { required: true, trigger: 'blur', validator: validateIdNo }
        // ]
      },
      options: [{
        value: '小学',
        label: '小学'
      }, {
        value: '初中',
        label: '初中'
      }, {
        value: '高中',
        label: '高中'
      }, {
        value: '专科',
        label: '专科'
      }, {
        value: '本科',
        label: '本科'
      }, {
        value: '研究生',
        label: '研究生'
      }, {
        value: '博士',
        label: '博士'
      }, {
        value: '-',
        label: '-'
      }]
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
      // this.getRoles()
      // this.getRoleLevel()
      // form.enabled = form.enabled.toString()
    },
    // 打开编辑弹窗前做的操作
    [CRUD.HOOK.beforeToEdit](crud, form) {
      // this.getJobs(this.form.dept.id)
      // selectedInspections = []
      // const roles = []
      // form.roles.forEach(function(role, index) {
      //   roles.push(role.id)
      //   // 初始化编辑时候的角色
      //   const rol = { id: role.id }
      //   selectedInspections.push(rol)
      // })
      // form.roles = roles
    },
    // 提交前做的操作
    [CRUD.HOOK.afterValidateCU](crud) {
      if (!crud.form.dept.id) {
        this.$message({
          message: '部门不能为空',
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
      if (this.deptName) {
        params['name'] = this.deptName
      }
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
      if (data.pid === 0) {
        this.query.deptId = null
      } else {
        this.query.deptId = data.id
      }
      this.crud.toQuery()
    },
    // 改变状态
    changeEnabled(data, val) {
      this.$confirm('此操作将 "' + this.dict.label.user_status[val] + '" ' + data.username + ', 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        crudEmployee.edit(data).then(res => {
          this.crud.notify(this.dict.label.user_status[val] + '成功', CRUD.NOTIFICATION_TYPE.SUCCESS)
        }).catch(() => {
          data.enabled = !data.enabled
        })
      }).catch(() => {
        data.enabled = !data.enabled
      })
    },
    // // 获取弹窗内角色数据
    // getRoles() {
    //   getAll().then(res => {
    //     this.roles = res
    //   }).catch(() => {
    //   })
    // },
    // // 获取弹窗内岗位数据
    // getJobs(id) {
    //   getAllJob(id).then(res => {
    //     this.jobs = res.content
    //   }).catch(() => {
    //   })
    // },
    // // 点击部门搜索对应的岗位
    // selectFun(node, instanceId) {
    //   // this.getJobs(node.id)
    //   // this.form.job.id = null
    // },
    // // 获取权限级别
    // getRoleLevel() {
    //   getLevel().then(res => {
    //     this.level = res.level
    //   }).catch(() => {
    //   })
    // },
    checkboxT(row, rowIndex) {
      return row.id !== this.user.id
    },
    // 设置地区属性
    regionChange(data) {
      if (data.town !== null) {
        this.form.address = data.province.value + data.city.value + data.area.value + data.town.value + this.form.address
        console.log(this.form.address)
      } else {
        this.form.address = ''
      }
    }
  }
}
</script>

<style scoped>
</style>
