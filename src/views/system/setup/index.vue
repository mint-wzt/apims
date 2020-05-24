<template>
  <div class="setup">
    <el-row>
      <el-col style="width: 500px;height: 500px">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>关于系统</span>
          </div>
          <div>
            <div style="text-align: center">
              <el-upload
                :show-file-list="false"
                :on-success="handleSuccess"
                :on-error="handleError"
                :headers="headers"
                :action="updateSysLogoApi"
                class="avatar-uploader"
              >
                <img :src="Logo" title="点击上传头像" class="avatar">
              </el-upload>
            </div>
            <ul class="user-info">
              <li><div style="height: 100%"><svg-icon icon-class="login" /> 系统名称<div class="user-right"><el-input v-model="systemInfo.systemName" style="width: 250px" /></div></div></li>
              <li><svg-icon icon-class="user1" /> 版权 <div class="user-right"><el-input v-model="systemInfo.caseNumber" style="width: 250px" /></div></li>
              <li><svg-icon icon-class="user2" /> 备案号 <div class="user-right"><el-input v-model="systemInfo.copyright" style="width: 250px" /></div></li>
              <li><svg-icon icon-class="" /> <div class="user-right">
                <el-button v-permission="permission.edit" :loading="saveLoading" style="width: 100px" size="mini" type="primary" @click="doSubmit">保存</el-button>
              </div></li>
            </ul>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getToken } from '@/utils/auth'
import { isvalidPhone } from '@/utils/validate'
import { parseTime } from '@/utils/index'
import crud from '@/mixins/crud'
import { getInfo, edit } from '@/api/system/setup'
import Logo from '@/assets/images/logo.png'
export default {
  name: 'Center',
  mixins: [crud],
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
    return {
      Logo: Logo,
      activeName: 'first',
      saveLoading: false,
      systemInfo: {
        systemLogo: Logo,
        systemName: '农产品基础数据库系统',
        caseNumber: '© 2020 Wang Zhitong',
        copyright: '湘ICP备20003851号'
      },
      permission: {
        edit: ['admin']
      },
      headers: {
        'Authorization': getToken()
      },
      form: {},
      rules: {
        nickName: [
          { required: true, message: '请输入用户昵称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        phone: [
          { required: true, trigger: 'blur', validator: validPhone }
        ]
      }
    }
  },
  computed: {
    ...mapGetters([
      'user',
      'updateSysLogoApi',
      'baseApi'
    ])
  },
  created() {
    getInfo().then(res => {
      this.systemInfo = res
    })
  },
  methods: {
    parseTime,
    handleClick(tab, event) {
      if (tab.name === 'second') {
        this.init()
      }
    },
    handleSuccess(response, file, fileList) {
      this.$notify({
        title: '系统Logo修改成功',
        type: 'success',
        duration: 2500
      })
      this.systemInfo.systemLogo = response.systemLogo
    },
    // 监听上传失败
    handleError(e, file, fileList) {
      const msg = JSON.parse(e.message)
      this.$notify({
        title: msg.message,
        type: 'error',
        duration: 2500
      })
    },
    doSubmit() {
      this.saveLoading = true
      edit(this.systemInfo).then(res => {
        this.saveLoading = false
        getInfo().then(res => {
          this.systemInfo = res
        })
        this.$notify({
          title: '修改成功',
          type: 'success',
          duration: 2500
        })
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .setup {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    margin-top: 5%;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    width: 120px;
    height: 120px;
    line-height: 120px;
    text-align: center
  }

  .avatar {
    width: 120px;
    height: 120px;
    display: block;
    border-radius: 50%
  }
  .user-info {
    padding-left: 0;
    list-style: none;
    li{
      border-bottom: 1px solid #F0F3F4;
      padding: 11px 0;
      font-size: 13px;
    }
    .user-right {
      float: right;

      a{
        color: #317EF3;
      }
    }
  }
</style>
