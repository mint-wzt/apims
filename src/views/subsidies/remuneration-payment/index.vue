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
              v-model="query.paymentTime"
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
        <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="620px">
          <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">
            <el-form-item label="标题" prop="name">
              <el-input v-model="form.name" placeholder="请输入标题名" />
            </el-form-item>
            <el-form-item label="补助单位" prop="dept.id" required>
              <treeselect
                v-model="form.dept.id"
                :options="depts"
                style="width: 178px"
                placeholder="选择部门"
                @select="selectFun"
              />
            </el-form-item>
            <el-form-item label="发放金额" prop="paymentTotal">
              <el-input v-model="form.paymentTotal" /><span style=" position: absolute; top: 1%; right: 4%;color: #adadad; display: table-cell;white-space: nowrap; padding: 1px 8px;">元</span>
            </el-form-item>
            <el-form-item label="发放日期" required>
              <el-date-picker
                v-model="form.paymentTime"
                type="date"
                placeholder="选择日期"
                size="small"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
            <el-form-item label="发放人" prop="distributor">
              <el-input v-model.number="form.distributor" placeholder="请输入发放人姓名" />
            </el-form-item>
            <el-form-item label="发放事由" prop="paymentReason">
              <el-input v-model="form.paymentReason" style="width: 220px;" />
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
          <el-table-column v-if="columns.visible('name')" :show-overflow-tooltip="true" prop="name" label="名称" />
          <el-table-column v-if="columns.visible('distributor')" :show-overflow-tooltip="true" prop="distributor" label="发放人" />
          <el-table-column v-if="columns.visible('deptName')" :show-overflow-tooltip="true" prop="deptName" label="补助单位" />
          <el-table-column v-if="columns.visible('paymentTotal')" :show-overflow-tooltip="true" prop="paymentTotal" label="补助资金">
            <template slot-scope="scope">
              <div>{{ scope.row.paymentTotal }}  元</div>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('regionId')" :show-overflow-tooltip="true" prop="regionId" label="区域编码" />
          <el-table-column v-if="columns.visible('regionName')" :show-overflow-tooltip="true" prop="regionName" label="区域名" />
          <el-table-column v-if="columns.visible('paymentReason')" :show-overflow-tooltip="true" prop="paymentReason" label="发放事由" />
          <el-table-column v-if="columns.visible('paymentTime')" :show-overflow-tooltip="true" prop="paymentTime" label="发放时间">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.paymentTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-permission="['admin','remuneration-payment:edit','remuneration-payment:del']"
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
import crudPayment from '@/api/subsidies/remuneration-payment'
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
const defaultCrud = CRUD({ title: '补助发放', url: 'api/remuneration-payment', crudMethod: { ...crudPayment }})
const defaultForm = { id: null, name: null, distributor: null, paymentReason: null, paymentTime: null, paymentTotal: null, regionId: null, regionName: null, dept: { id: null }, deptName: null, remark: '-' }
export default {
  name: 'RemunerationPayment',
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
        add: ['admin', 'remuneration-payment:add'],
        edit: ['admin', 'remuneration-payment:edit'],
        del: ['admin', 'remuneration-payment:del']
      },
      enabledTypeOptions: [
        { key: 1, display_name: '已提交' },
        { key: 0, display_name: '草稿' }
      ],
      rules: {
        name: [
          { required: true, message: '请输入标题', trigger: 'blur' }
        ],
        distributor: [
          { required: true, message: '请输入发放人姓名', trigger: 'blur' }
        ],
        paymentTotal: [
          { required: true, message: '请输入发放金额', trigger: 'blur' }
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
    },
    // 打开编辑弹窗前做的操作
    [CRUD.HOOK.beforeToEdit](crud, form) {
    },
    // 提交前做的操作
    [CRUD.HOOK.afterValidateCU](crud) {
      if (!crud.form.dept.id) {
        this.$message({
          message: '不能选择其他组织机构',
          type: 'warning'
        })
        return false
      } else if (!crud.form.paymentTime) {
        this.$message({
          message: '发放日期不能为空',
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
    // 点击部门搜索对应的岗位
    selectFun(node, instanceId) {
      this.form.deptName = node.label
      console.log(this.form.deptName)
    },
    // 改变状态
    changeEnabled(data, val) {
      this.$confirm('此操作将 "' + this.dict.label.user_status[val] + '" ' + data.username + ', 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        crudPayment.edit(data).then(res => {
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
