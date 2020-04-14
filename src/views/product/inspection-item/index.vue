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
          placeholder="输入检测项名称/编码搜索"
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
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="95px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input v-model="form.code" />
        </el-form-item>
        <el-form-item label="比较方向" prop="compareDirection">
          <el-input v-model="form.compareDirection" />
        </el-form-item>
        <el-form-item label="检出类型" prop="checkoutType">
          <el-input v-model="form.checkoutType" />
        </el-form-item>
        <el-form-item label="参考下限" prop="lowerLimit">
          <el-input v-model="form.lowerLimit" />
        </el-form-item>
        <el-form-item label="下限比较符" prop="lowerLimitComparator">
          <el-input v-model="form.lowerLimitComparator" />
        </el-form-item>
        <el-form-item label="参考上限" prop="upperLimit">
          <el-input v-model="form.upperLimit" />
        </el-form-item>
        <el-form-item label="上限比较符" prop="upperLimitComparator">
          <el-input v-model="form.upperLimitComparator" />
        </el-form-item>
        <el-form-item label="计量单位" prop="unit">
          <el-input v-model="form.unit" />
        </el-form-item>
        <el-form-item label="分类编码" prop="classificationCode">
          <el-input v-model="form.classificationCode" />
        </el-form-item>
        <el-form-item label="分类名称" prop="classificationName">
          <el-input v-model="form.classificationName" />
        </el-form-item>
        <el-form-item label="版本" prop="version">
          <el-input v-model="form.version" />
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
      <el-table-column v-if="columns.visible('code')" :show-overflow-tooltip="true" prop="code" label="编码" />
      <el-table-column v-if="columns.visible('name')" :show-overflow-tooltip="true" prop="name" label="名称" />
      <el-table-column v-if="columns.visible('unit')" :show-overflow-tooltip="true" prop="unit" label="单位" />
      <el-table-column v-if="columns.visible('compareDirection')" :show-overflow-tooltip="true" prop="compareDirection" label="比较方向" />
      <el-table-column v-if="columns.visible('checkoutType')" :show-overflow-tooltip="true" prop="checkoutType" label="检出类型" />
      <el-table-column v-if="columns.visible('lowerLimit')" :show-overflow-tooltip="true" prop="lowerLimit" label="参考下限" />
      <el-table-column v-if="columns.visible('lowerLimitComparator')" :show-overflow-tooltip="true" prop="lowerLimitComparator" label="下限比较符" />
      <el-table-column v-if="columns.visible('upperLimit')" :show-overflow-tooltip="true" label="参考上限" prop="upperLimit" />
      <el-table-column v-if="columns.visible('upperLimitComparator')" :show-overflow-tooltip="true" prop="upperLimitComparator" label="上限比较符" />
      <el-table-column v-if="columns.visible('classificationCode')" :show-overflow-tooltip="true" prop="classificationCode" label="分类编码" />
      <el-table-column v-if="columns.visible('classificationName')" :show-overflow-tooltip="true" prop="classificationName" label="分类名称" />
      <el-table-column v-if="columns.visible('version')" :show-overflow-tooltip="true" prop="version" label="版本号" />
      <el-table-column v-if="columns.visible('createTime')" :show-overflow-tooltip="true" prop="createTime" label="创建日期">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        v-permission="['admin','inspection-item:edit','inspection-item:del']"
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
import crudInspectionItem from '@/api/product/inspection-item'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import { mapGetters } from 'vuex'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

// crud交由presenter持有
const defaultCrud = CRUD({ title: '检测项', url: 'api/inspection-item', crudMethod: { ...crudInspectionItem }})
const defaultForm = { id: null, code: null, name: null, unit: null, compareDirection: null, checkoutType: null, lowerLimit: null, lowerLimitComparator: null, upperLimit: null, upperLimitComparator: null, classificationCode: null, classificationName: null, version: null }
export default {
  name: 'InspectionItem',
  components: { crudOperation, rrOperation, udOperation, pagination },
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      height: document.documentElement.clientHeight - 180 + 'px;',
      categoryName: '', categories: [], categoryDatas: [],
      defaultProps: { children: 'children', label: 'name' },
      permission: {
        add: ['admin', 'inspection-item:add'],
        edit: ['admin', 'inspection-item:edit'],
        del: ['admin', 'inspection-item:del']
      },
      rules: {
        code: [
          { required: true, message: '请输入检测项代码', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入检测项名称', trigger: 'blur' }
        ],
        compareDirection: [
          { required: true, message: '请输入比较方向', trigger: 'blur' }
        ],
        checkoutType: [
          { required: true, message: '请输入检出类型', trigger: 'blur' }
        ],
        lowerLimit: [
          { required: true, message: '请输入参考下限', trigger: 'blur' }
        ],
        lowerLimitComparator: [
          { required: true, message: '请输入下限比较符', trigger: 'blur' }
        ],
        upperLimit: [
          { required: true, message: '请输入参考上限', trigger: 'blur' }
        ],
        upperLimitComparator: [
          { required: true, message: '请输入参考上限', trigger: 'blur' }
        ],
        unit: [
          { required: true, message: '请输入计量单位', trigger: 'blur' }
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
    [CRUD.HOOK.afterAddError](crud) {
      this.afterErrorMethod(crud)
    },
    [CRUD.HOOK.afterEditError](crud) {
      this.afterErrorMethod(crud)
    },
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      // getCategoryTree({ enabled: 1 }).then(res => {
      //   this.categories = res
      //   // const category = { id: 0, label: '顶级类目', children: [] }
      //   // category.children = res
      //   // this.categories.push(category)
      // })
    },
    // 打开编辑弹窗前做的操作
    [CRUD.HOOK.beforeToEdit](crud, form) {
    },
    // 提交前做的操作
    [CRUD.HOOK.afterValidateCU](crud) {
      // if (!crud.form.category.id) {
      //   this.$message({
      //     message: '分类不能为空',
      //     type: 'warning'
      //   })
      //   return false
      // }
      return true
    }
  }
}
</script>

<style scoped>
</style>
