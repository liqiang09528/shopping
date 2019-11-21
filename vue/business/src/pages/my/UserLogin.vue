<template>
  <div>
    <mt-header title="用户登录">
      <router-link to="/" slot="left">
        <mt-button icon="back" @click="back">返回</mt-button>
      </router-link>
      <!--<mt-button icon="more" slot="right"></mt-button>-->
    </mt-header>
    <mt-field label="用户名" placeholder="请输入用户名" name="username" v-model="username"></mt-field>
    <mt-field label="密码" placeholder="请输入密码" type="password" name="password" v-model="password"></mt-field>
    <mt-button type="primary" size="large" @click="login">登录</mt-button>
  </div>
</template>

<script>

  import {mapActions} from 'vuex'
  export default {
    name: "UserLogin",
    data:function () {
      return{
        username:"admin",
        password:"12345"
      }
    },
    methods:{
      ...mapActions(['setUserInfo']),

      login:function () {
        var _vm=this;
        this.service.post("user/login/"+this.username+"/"+this.password).then(function (response) {
          console.log(response.data.data)
          //用户成功后保存到vuex
          if (response.data.status == 0) {
            //登录成功
            _vm.setUserInfo(response.data.data)
            //组件后退到上一层
            _vm.$router.go(-1)
          }
          mapActions(['setUserInfo'])
        }).catch(function (err) {
          console.log(err)
        })
      },
      back:function () {
        this.$router.go(-1)
      }
    }
  }
</script>

<style scoped>

</style>
