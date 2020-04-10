<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input v-model="query.blurry" clearable size="small" placeholder="模糊搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="toQuery" />
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
      <crudOperation :permission="permission" />
    </div>
    <!--表单渲染-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="580px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="分类名称" />
        </el-form-item>
        <el-form-item label="分类代码" prop="code">
          <el-input v-model="form.code" placeholder="分类代码" style="width: 178px;" />
        </el-form-item>
        <el-form-item label="上级类目" prop="pid">
          <treeselect v-model="form.pid" :options="categories" placeholder="选择上级类目" style="width: 178px;" />
        </el-form-item>
        <el-form-item label="状态" prop="enabled">
          <el-radio v-for="item in category_status" :key="item.id" v-model="form.enabled" :label="item.value">{{ item.label }}</el-radio>
        </el-form-item>
        <el-form-item label="描述信息" prop="description">
          <el-input v-model="form.description" style="width: 440px;" rows="5" type="textarea" />
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
      <el-table-column v-if="columns.visible('name')" :show-overflow-tooltip="true" label="分类名称" prop="name" />
      <el-table-column v-if="columns.visible('code')" :show-overflow-tooltip="true" prop="code" label="分类代码" />
      <el-table-column v-if="columns.visible('description')" :show-overflow-tooltip="true" prop="description" label="描述" />
      <el-table-column v-if="columns.visible('pName')" :show-overflow-tooltip="true" prop="pname" label="父类名称" />
      <el-table-column v-if="columns.visible('pCode')" :show-overflow-tooltip="true" prop="pcode" label="父类编码" />
      <el-table-column v-if="columns.visible('enabled')" :show-overflow-tooltip="true" prop="enabled" align="center" label="状态">
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.enabled === 0 ? 'danger' : 'success'"
            disable-transitions
          >{{ scope.row.enabled === 1 ? '启用' : '停用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="columns.visible('createTime')" :show-overflow-tooltip="true" prop="createTime" label="创建日期" width="135px">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column v-permission="['admin','category:edit','category:del']" label="操作" width="130px" align="center" fixed="right">
        <template slot-scope="scope">
          <udOperation
            :data="scope.row"
            :permission="permission"
            msg="确定删除吗,如果存在下级节点则一并删除，此操作不能撤销！"
          />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import crudCategory from '@/api/product/category'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'

// crud交由presenter持有
const defaultCrud = CRUD({ title: '农产品分类', url: 'api/category', crudMethod: { ...crudCategory }})
const defaultForm = { id: null, name: null, code: null, description: '-', pid: 0, enabled: 1, createTime: null, createUid: null }
export default {
  name: 'Category',
  components: { Treeselect, crudOperation, rrOperation, udOperation },
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      categories: [],
      permission: {
        add: ['admin', 'category:add'],
        edit: ['admin', 'category:edit'],
        del: ['admin', 'category:del']
      },
      category_status: [
        {
          id: 1,
          value: 1,
          label: '启用'
        },
        {
          id: 2,
          value: 0,
          label: '停用'
        }
      ],
      rules: {
        name: [
          { required: true, message: '请输入分类名称', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入分类代码', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      crudCategory.getCategoryTree({ enabled: 1 }).then(res => {
        this.categories = []
        const category = { id: 0, label: '顶级类目', children: [] }
        category.children = res
        this.categories.push(category)
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  /deep/ .el-input-number .el-input__inner {
    text-align: left;
  }
</style>
