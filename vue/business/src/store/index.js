import Vue from 'vue'
import Vuex from 'vuex'
import * as getters from './getters'
import * as actions from './actions'
import * as mutations from './mutations'

//vue全局注册
Vue.use(Vuex)

//将登录信息放在userInfo中
const state={
  userInfo:{},
  isShowFooterBar:true,
  goodsList:[],
  orderNo:null
}

//new实例
export const store=new Vuex.Store({
  state,
  getters,
  actions,
  mutations
})
