<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--侧边部门数据-->
      <el-col :xs="9" :sm="6" :md="4" :lg="4" :xl="4">
        <div class="head-container">
          <el-input
            v-model="categoryName"
            clearable
            size="small"
            placeholder="输入分类名称搜索"
            prefix-icon="el-icon-search"
            class="filter-item"
            @input="getDeptDatas"
          />
        </div>
        <el-tree
          :data="categoryDatas"
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
              placeholder="输入中文名称或编码搜索"
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
            <el-form-item label="编码" prop="code">
              <el-input v-model="form.code" />
            </el-form-item>
            <el-form-item label="类型" prop="category.id">
              <treeselect
                v-model="form.category.id"
                :options="categories"
                :props="defaultProps"
                style="width: 178px"
                placeholder="选择分类"
              />
            </el-form-item>
            <el-form-item label="中文名称" prop="chineseName">
              <el-input v-model="form.chineseName" />
            </el-form-item>
            <el-form-item label="英文名称" prop="englishName">
              <el-input v-model="form.englishName" />
            </el-form-item>
            <el-form-item label="标记" prop="mark">
              <el-input v-model="form.mark" />
            </el-form-item>
            <el-form-item label="说明" prop="description">
              <el-input v-model="form.description" />
            </el-form-item>
            <el-form-item label="格式/类型" prop="datatypeFormat">
              <el-input v-model="form.datatypeFormat" />
            </el-form-item>
            <el-form-item label="值域" prop="dataRange">
              <el-input v-model="form.dataRange" />
            </el-form-item>
            <el-form-item label="同义词" prop="synonym">
              <el-input v-model="form.synonym" />
            </el-form-item>
            <el-form-item label="约束/条件" prop="restrictions">
              <el-input v-model="form.restrictions" />
            </el-form-item>
            <el-form-item label="计量单位" prop="unit">
              <el-input v-model="form.unit" />
            </el-form-item>
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" />
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
          <el-table-column v-if="columns.visible('chineseName')" :show-overflow-tooltip="true" prop="chineseName" label="中文名称" />
          <el-table-column v-if="columns.visible('englishName')" :show-overflow-tooltip="true" prop="englishName" label="英文名称" />
          <el-table-column v-if="columns.visible('mark')" :show-overflow-tooltip="true" prop="mark" label="标记" />
          <el-table-column v-if="columns.visible('description')" :show-overflow-tooltip="true" prop="description" label="说明" />
          <el-table-column v-if="columns.visible('datatypeFormat')" :show-overflow-tooltip="true" prop="datatypeFormat" label="格式/类型" />
          <el-table-column v-if="columns.visible('dataRange')" :show-overflow-tooltip="true" prop="dataRange" label="值域" />
          <el-table-column v-if="columns.visible('synonym')" :show-overflow-tooltip="true" label="同义词" prop="synonym" />
          <el-table-column v-if="columns.visible('restrictions')" :show-overflow-tooltip="true" prop="restrictions" label="约束/条件" />
          <el-table-column v-if="columns.visible('unit')" :show-overflow-tooltip="true" prop="unit" label="计量单位" />
          <el-table-column v-if="columns.visible('remark')" :show-overflow-tooltip="true" prop="remark" label="备注" />
          <el-table-column v-if="columns.visible('createTime')" :show-overflow-tooltip="true" prop="createTime" width="140" label="创建日期">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-permission="['admin','description:edit','description:del']"
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
import crudDescription from '@/api/product/description'
import { getCategories, getCategoryTree } from '@/api/product/category'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import Treeselect from '@riophae/vue-treeselect'
import { mapGetters } from 'vuex'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

// crud交由presenter持有
const defaultCrud = CRUD({ title: '描述项', url: 'api/description', crudMethod: { ...crudDescription }})
const defaultForm = { id: null, code: null, chineseName: null, englishName: null, mark: null, description: null, datatypeFormat: null, dataRange: null, synonym: null, restrictions: null, unit: null, remark: null, category: { id: null }}
export default {
  name: 'Description',
  components: { Treeselect, crudOperation, rrOperation, udOperation, pagination },
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      height: document.documentElement.clientHeight - 180 + 'px;',
      categoryName: '', categories: [], categoryDatas: [],
      defaultProps: { children: 'children', label: 'name' },
      permission: {
        add: ['admin', 'description:add'],
        edit: ['admin', 'description:edit'],
        del: ['admin', 'description:del']
      },
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
    [CRUD.HOOK.afterAddError](crud) {
      this.afterErrorMethod(crud)
    },
    [CRUD.HOOK.afterEditError](crud) {
      this.afterErrorMethod(crud)
    },
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      getCategoryTree({ enabled: 1 }).then(res => {
        this.categories = res
        // const category = { id: 0, label: '顶级类目', children: [] }
        // category.children = res
        // this.categories.push(category)
      })
    },
    // 打开编辑弹窗前做的操作
    [CRUD.HOOK.beforeToEdit](crud, form) {
    },
    // 提交前做的操作
    [CRUD.HOOK.afterValidateCU](crud) {
      return true
    },
    // 获取左侧部门数据
    getDeptDatas() {
      const sort = 'id,desc'
      const params = { sort: sort }
      if (this.categoryName) { params['name'] = this.categoryName }
      getCategories(params).then(res => {
        this.categoryDatas = res.content
      })
    },
    // 获取弹窗内部门数据
    getDepts() {
      getCategories().then(res => {
        this.categories = res.content
      })
    },
    // 切换部门
    handleNodeClick(data) {
      console.log(JSON.stringify(data))
      this.query.categoryId = data.id
      this.crud.toQuery()
      this.query.categoryId = null
    }
  }
}
</script>

<style scoped>
</style>
