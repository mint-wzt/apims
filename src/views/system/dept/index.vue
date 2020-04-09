<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input v-model="query.name" clearable size="small" placeholder="输入部门名称搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
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
        <el-select v-model="query.enabled" clearable size="small" placeholder="状态" class="filter-item" style="width: 90px" @change="crud.toQuery">
          <el-option v-for="item in enabledTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
        </el-select>
        <rrOperation :crud="crud" />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!--表单组件-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
      <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="新增" prop="type">
          <el-radio v-model="type" label="0" @change="chooseChange">行政部门</el-radio>
          <el-radio v-model="type" label="1" @change="chooseChange">组织机构</el-radio>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" style="width: 370px;" />
        </el-form-item>
        <el-form-item label="状态" prop="enabled">
          <el-radio v-for="item in dict.dept_status" :key="item.id" v-model="form.enabled" :label="item.value">{{ item.label }}</el-radio>
        </el-form-item>
        <el-form-item label="所在地区" prop="region">
          <v-region type="column" :town="true" @values="regionChange" />
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" style="width: 370px;" placeholder="如村、道路、门牌号、小区、楼栋号、单元室等" />
        </el-form-item>
        <el-form-item style="margin-bottom: 0;" label="上级部门" prop="pid">
          <treeselect v-model="form.pid" :options="depts" style="width: 370px;" placeholder="选择上级类目" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="crud.cancelCU">取消</el-button>
        <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
      </div>
    </el-dialog>
    <!--表格渲染-->
    <el-table ref="table" v-loading="crud.loading" :tree-props="{children: 'children', hasChildren: 'hasChildren'}" default-expand-all :data="crud.data" row-key="id" @select="crud.selectChange" @select-all="crud.selectAllChange" @selection-change="crud.selectionChangeHandler">
      <el-table-column :selectable="checkboxT" type="selection" width="55" />
      <el-table-column v-if="columns.visible('name')" label="名称" prop="name" />
      <el-table-column v-if="columns.visible('address')" label="所在地区" prop="address" />
      <el-table-column
        prop="deptType"
        label="类型"
        width="100"
      >
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.deptType === 0 ? 'primary' : 'success'"
            disable-transitions
          >{{ typeTranslate(scope.row.deptType) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="columns.visible('enabled')" label="状态" align="center" prop="enabled">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.enabled"
            :disabled="scope.row.id === 1"
            active-color="#409EFF"
            inactive-color="#F56C6C"
            @change="changeEnabled(scope.row, scope.row.enabled,)"
          />
        </template>
      </el-table-column>
      <el-table-column v-if="columns.visible('createTime')" prop="createTime" label="创建日期">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column v-permission="['admin','dept:edit','dept:del']" label="操作" width="130px" align="center" fixed="right">
        <template slot-scope="scope">
          <udOperation
            :data="scope.row"
            :permission="permission"
            :disabled-dle="scope.row.id === 1"
            msg="确定删除吗,如果存在下级节点则一并删除，此操作不能撤销！"
          />
        </template>
      </el-table-column>
    </el-table>
    <pagination />
  </div>
</template>

<script>
import crudDept from '@/api/system/dept'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

// crud交由presenter持有
const defaultCrud = CRUD({ title: '行政部门/组织机构', url: 'api/dept', crudMethod: { ...crudDept }})
const defaultForm = { id: null, name: null, pid: 0, enabled: 'true', address: '', deptType: 0,
  region: { id: null, provinceId: null, provinceName: null, cityId: null, cityName: null, areaId: null, areaName: null, townId: null, townName: null }}
export default {
  name: 'Dept',
  components: { Treeselect, crudOperation, rrOperation, udOperation, pagination },
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  // 设置数据字典
  dicts: ['dept_status'],
  data() {
    return {
      depts: [],
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ]
      },
      permission: {
        add: ['admin', 'dept:add'],
        edit: ['admin', 'dept:edit'],
        del: ['admin', 'dept:del']
      },
      enabledTypeOptions: [
        { key: 'true', display_name: '正常' },
        { key: 'false', display_name: '禁用' }
      ],
      type: '0'
    }
  },
  created() {
    this.$nextTick(() => {
      this.crud.toQuery()
    })
  },
  methods: {
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      form.enabled = `${form.enabled}`
      // 获取所有部门
      crudDept.getDepts({ enabled: true }).then(res => {
        this.depts = []
        const dept = { id: 0, label: '顶级类型', children: [] }
        dept.children = res.content
        // this.depts = res.content
        this.depts.push(dept)
      })
    },
    // 提交前的验证
    [CRUD.HOOK.afterValidateCU]() {
      if (Object.keys(this.form.region).length === 0 && this.form.pid === 0) {
        this.$message({
          message: '上级部门不能为空',
          type: 'warning'
        })
        return false
      }
      return true
    },
    // 改变状态
    changeEnabled(data, val) {
      this.$confirm('此操作将 "' + this.dict.label.dept_status[val] + '" ' + data.name + '部门, 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        crudDept.edit(data).then(res => {
          this.crud.notify(this.dict.label.dept_status[val] + '成功', CRUD.NOTIFICATION_TYPE.SUCCESS)
        }).catch(err => {
          data.enabled = !data.enabled
          console.log(err.response.data.message)
        })
      }).catch(() => {
        data.enabled = !data.enabled
      })
    },
    checkboxT(row, rowIndex) {
      return row.id !== 1
    },
    // 设置地区属性
    regionChange(data) {
      if (data.town !== null) {
        this.form.region.id = data.town.key
        this.form.region.provinceId = data.province.key
        this.form.region.provinceName = data.province.value
        this.form.region.cityId = data.city.key
        this.form.region.cityName = data.city.value
        this.form.region.areaId = data.area.key
        this.form.region.areaName = data.area.value
        this.form.region.townId = data.town.key
        this.form.region.townName = data.town.value
        this.form.address = data.province.value + data.city.value + data.area.value + data.town.value + this.form.address
        console.log(JSON.stringify(this.form.region))
        console.log(this.form.address)
      } else {
        this.form.address = ''
      }
    },
    chooseChange() {
      this.form.deptType = this.type
      console.log(this.form.deptType)
    },
    typeTranslate(type) {
      return type === 0 ? '行政单位' : '组织机构'
    }
  }
}
</script>

<style scoped>
</style>
