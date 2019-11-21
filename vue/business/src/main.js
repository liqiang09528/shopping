// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import './assets/styles/reset.css'
import './assets/styles/border.css'
import './assets/styles/iconfont.css'
import VueAwesomeSwiper from 'vue-awesome-swiper'
import MintUI from 'mint-ui'
import Vant from 'vant'
import 'vant/lib/index.css'
import 'mint-ui/lib/style.css'
// require styles
import 'swiper/dist/css/swiper.css'
import fastclick from 'fastclick'
import axios from 'axios'
import Vuex from 'vuex'
import {store} from './store/index'

Vue.config.productionTip = false;
Vue.use(Vant);
Vue.use(MintUI);

Vue.use(VueAwesomeSwiper,)
fastclick.attach(document.body);

var service=axios.create({

  baseURL:"http://localhost:8888",
  // 请求预处理函数 可以把你传的param进行处理
  withCredentials: true, // 允许携带cookie
  transformRequest: [
    data => {
      // data 就是你post请求传的值
      // 一下主要是吧数据拼接成 类似get格式
      let params = ''
      for (var index in data) {
        params += index + '=' + data[index] + '&'
      }
      return params
    }
  ]

});
Vue.prototype.service=service;

axios.defaults.withCredentials=true;

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
