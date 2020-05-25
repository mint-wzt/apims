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
              placeholder="请输入品种名称、品种类型搜索"
              style="width: 200px;"
              class="filter-item"
              @keyup.enter.native="crud.toQuery"
            />
            <el-date-picker
              v-model="query.releaseDate"
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
              v-model="query.releaseStatus"
              clearable
              size="small"
              placeholder="状态"
              class="filter-item"
              style="width: 90px"
              @change="crud.toQuery"
            >
              <el-option
                v-for="item in status"
                :key="item.key"
                :label="item.name"
                :value="item.key"
              />
            </el-select>
            <rrOperation :crud="crud" />
          </div>
          <crudOperation show="" :permission="permission" />
        </div>
        <!--表单渲染-->
        <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="630px">
          <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">
            <el-form-item label="供应品种" prop="productName">
              <el-input v-model="form.productName" placeholder="请输入品种名称" />
            </el-form-item>
            <el-form-item label="品种类型">
              <treeselect
                v-model="form.categoryId"
                style="width: 178px"
                placeholder="请选择品种类型"
                :options="categories"
                @select="getCategoryName"
              />
            </el-form-item>
            <el-form-item label="起批量" prop="batchStart">
              <el-input v-model="form.batchStart" placeholder="请输入起批量" />
            </el-form-item>
            <el-form-item label="单位部门" prop="deptId">
              <treeselect
                v-model="form.deptId"
                style="width: 178px"
                placeholder="请选择组织机构"
                :options="depts"
                @select="getDeptName"
              />
            </el-form-item>
            <el-form-item label="价格" prop="price">
              <el-input v-model="form.price" />
            </el-form-item>
            <el-form-item label="发货地址" prop="receiptAddress">
              <el-input v-model="form.deliveryAddress" placeholder="请输入发货地址" />
            </el-form-item>
            <el-form-item label="发布人" prop="publisher">
              <el-input v-model="form.publisher" placeholder="请输入发布人名称" />
            </el-form-item>
            <el-form-item label="联系方式" prop="contact">
              <el-input v-model="form.contact" />
            </el-form-item>
            <el-form-item label="状态" prop="releaseStatus">
              <el-radio-group v-model="form.releaseStatus">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">停用</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" style="width: 440px;" rows="5" type="textarea" />
            </el-form-item>
            <el-form-item label="上传图片">
              <el-upload
                list-type="picture-card"
                :limit="1"
                :on-preview="handlePictureCardPreview"
                :on-remove="handleRemove"
                :on-success="handleSuccess"
                :on-error="handleError"
                :headers="headers"
                :action="uploadProductImageApi"
              >
                <i class="el-icon-plus" />
              </el-upload>
              <el-dialog :visible.sync="dialogVisible">
                <img width="100%" :src="form.productImage" alt="">
              </el-dialog>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="text" @click="crud.cancelCU">取消</el-button>
            <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
          </div>
        </el-dialog>
        <!--表格渲染-->
        <el-table ref="table" v-loading="crud.loading" :data="crud.data" default-expand-all style="width: 100%;" @selection-change="crud.selectionChangeHandler">
          <el-table-column type="selection" width="55" />
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-row>
                  <el-col :xs="24" :sm="24" :lg="6">
                    <div style="height: 200px;width: 200px">
                      <img class="images" style="height: 200px;width: 200px" :src="getImageURL(props.row.productImage)">
                    </div>
                  </el-col>
                  <el-col :xs="24" :sm="24" :lg="18">
                    <div style="margin-left: 30px">
                      <div style="font-size: 18px;color: cornflowerblue;margin-bottom: 15px">
                        供应详情
                      </div>
                      <el-form-item label="供应品种">
                        <span>{{ props.row.productName }}</span>
                      </el-form-item>
                      <el-form-item label="起批量">
                        <span>{{ props.row.batchStart }}</span>
                      </el-form-item>
                      <el-form-item label="价格">
                        <span>{{ props.row.price }}</span>
                      </el-form-item>
                      <el-form-item label="浏览次数">
                        <span>{{ props.row.views }} 次</span>
                      </el-form-item>
                      <el-form-item label="品种类型">
                        <span>{{ props.row.categoryName }}</span>
                      </el-form-item>
                      <el-form-item label="发布日期">
                        <span>{{ parseTime(props.row.createTime) }}</span>
                      </el-form-item>
                      <el-form-item label="发货地址">
                        <span>{{ props.row.deliveryAddress }}</span>
                      </el-form-item>
                      <el-form-item label="发布人">
                        <span>{{ props.row.publisher }}</span>
                      </el-form-item>
                      <el-form-item label="联系方式">
                        <span>{{ props.row.contact }}</span>
                      </el-form-item>
                      <el-form-item label="组织机构">
                        <span>{{ props.row.deptName }}</span>
                      </el-form-item>
                      <el-form-item label="备注">
                        <span>{{ props.row.remark }}</span>
                      </el-form-item>
                    </div>
                  </el-col>
                </el-row>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('productName')" :show-overflow-tooltip="true" prop="productName" label="供应品种" />
          <el-table-column v-if="columns.visible('categoryName')" :show-overflow-tooltip="true" prop="categoryName" label="品种类型" />
          <el-table-column v-if="columns.visible('batchStart')" :show-overflow-tooltip="true" prop="batchStart" label="起批量" />
          <el-table-column v-if="columns.visible('price')" :show-overflow-tooltip="true" prop="price" label="价格" />
          <el-table-column v-if="columns.visible('deliveryAddress')" :show-overflow-tooltip="true" prop="deliveryAddress" label="发货地址" />
          <el-table-column v-if="columns.visible('releaseStatus')" label="状态" align="center" prop="releaseStatus">
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.releaseStatus === 0 ? 'danger' : 'success'"
                disable-transitions
              >{{ scope.row.releaseStatus === 1 ? '启用' : '停用' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('createTime')" :show-overflow-tooltip="true" prop="createTime" width="140" label="发布日期">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-permission="['admin','sales-release:edit','sales-release:del']"
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
import crudPurchaseRelease from '@/api/release/sales-release'
import { getCategoryTree } from '@/api/product/category'
import { getDepts } from '@/api/system/dept'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import Treeselect from '@riophae/vue-treeselect'
import { mapGetters } from 'vuex'
import { getToken } from '@/utils/auth'
import Avatar from '@/assets/images/product.jpg'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

// crud交由presenter持有
const defaultCrud = CRUD({ title: '供应信息', url: 'api/sales-release', crudMethod: { ...crudPurchaseRelease }})
const defaultForm = { id: null, productName: null, categoryId: null, categoryName: null, productImage: null, batchStart: null, releaseStatus: 1, price: null, priceUnit: null, deliveryAddress: null, publisher: null, views: 0, contact: null, deptId: null, deptName: null, remark: '-' }
export default {
  name: 'SalesRelease',
  components: { Treeselect, crudOperation, rrOperation, udOperation, pagination },
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  // 数据字典
  dicts: ['user_status'],
  data() {
    return {
      Avatar: Avatar,
      dialogImageUrl: '',
      dialogVisible: false,
      expands: [],
      height: document.documentElement.clientHeight - 180 + 'px;',
      deptName: '', depts: [], deptDatas: [], categories: [],
      defaultProps: { children: 'children', label: 'name' },
      headers: {
        'Authorization': getToken()
      },
      permission: {
        add: ['admin', 'sales-release:add'],
        edit: ['admin', 'sales-release:edit'],
        del: ['admin', 'sales-release:del']
      },
      status: [
        {
          key: 1,
          name: `启用`
        },
        {
          key: 0,
          name: `停用`
        }],
      rules: {
        productName: [
          { required: true, message: '请输入品种名称', trigger: 'blur' }
        ],
        batchStart: [
          { required: true, message: '请输入起批量', trigger: 'blur' }
        ],
        price: [
          { required: true, message: '请输入价格', trigger: 'blur' }
        ],
        deliveryAddress: [
          { required: true, message: '请输入发货地址', trigger: 'blur' }
        ],
        publisher: [
          { required: true, message: '请输入发布人名称', trigger: 'blur' }
        ],
        contact: [
          { required: true, message: '请输入联系方式', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters([
      'user',
      'uploadProductImageApi',
      'baseApi'
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
    getImageURL(imageName) {
      return imageName ? this.baseApi + '/avatar/' + imageName : this.Avatar
    },
    [CRUD.HOOK.afterAddError](crud) {
      this.afterErrorMethod(crud)
    },
    [CRUD.HOOK.afterEditError](crud) {
      this.afterErrorMethod(crud)
    },
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      this.getDepts()
      getCategoryTree({ enabled: 1 }).then(res => {
        this.categories = []
        this.categories = res
      })
    },
    // 打开编辑弹窗前做的操作
    [CRUD.HOOK.beforeToEdit](crud, form) {
    },
    // 提交前做的操作
    [CRUD.HOOK.afterValidateCU](crud) {
      if (!crud.form.deptId) {
        this.$message({
          message: '组织机构不能为空',
          type: 'warning'
        })
        return false
      } else if (!crud.form.categoryId) {
        this.$message({
          message: '品种分类不能为空',
          type: 'warning'
        })
        return false
      }
      return true
    },
    regionChange(data) {
      this.form.regionId = data.province === null ? null : data.province.value
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
    // 获取选择分类的名称
    getCategoryName(node, instanceId) {
      this.form.categoryName = node.label
    },
    // 获取选择产品的名称
    getDeptName(node, instanceId) {
      this.form.deptName = node.label
    },
    // 改变状态
    changeEnabled(data, val) {
      this.$confirm('此操作将 "' + this.dict.label.user_status[val] + '" ' + data.username + ', 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        crudPurchaseRelease.edit(data).then(res => {
          this.crud.notify(this.dict.label.user_status[val] + '成功', CRUD.NOTIFICATION_TYPE.SUCCESS)
        }).catch(() => {
          data.enabled = !data.enabled
        })
      }).catch(() => {
        data.enabled = !data.enabled
      })
    },
    handleRemove(file, fileList) {
      this.form.productImage = null
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    handleSuccess(response, file, fileList) {
      this.$notify({
        title: '上传成功',
        type: 'success',
        duration: 2500
      })
      this.form.productImage = response.imageName
    },
    // 监听上传失败
    handleError(e, file, fileList) {
      const msg = JSON.parse(e.message)
      this.$notify({
        title: msg.message,
        type: 'error',
        duration: 2500
      })
    }
  }
}
</script>

<style scoped>
  .images {
    border-radius:15px;
  }
  .demo-table-expand {
    font-size: 0;
    margin-left: 60px;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>
