import variables from '@/assets/styles/element-variables.scss'
import defaultSettings from '@/settings'
import { getInfo } from '@/api/system/setup'

const { title, tagsView, fixedHeader, sidebarLogo, uniqueOpened, showFooter, footerTxt, caseNumber } = defaultSettings

const state = {
  title: title,
  theme: variables.theme,
  showSettings: false,
  tagsView: tagsView,
  fixedHeader: fixedHeader,
  sidebarLogo: sidebarLogo,
  uniqueOpened: uniqueOpened,
  showFooter: showFooter,
  footerTxt: footerTxt,
  caseNumber: caseNumber,
  systemLogo: '@/assets/images/logo.png',
  enterprise: null,
  description: null
}

const mutations = {
  CHANGE_SETTING: (state, { key, value }) => {
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  }
}

const actions = {
  changeSetting({ commit }, data) {
    commit('CHANGE_SETTING', data)
  },
  // 获取系统信息
  GetSysInfo({ commit }) {
    return new Promise((resolve, reject) => {
      getInfo().then(res => {
        this.setSysInfo(res, commit)
        resolve(res)
      }).catch(error => {
        reject(error)
      })
    })
  },
  setSysInfo(res, commit) {
    console.log(res)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

