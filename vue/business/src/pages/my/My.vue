<template>
  <div>
    <div>
      <mt-header title="用户中心">
        <!--<mt-button icon="more" slot="right"></mt-button>-->
      </mt-header>
      <img class="headimg" src="static/images/timg.jpg"/>
      <span class="tologin" v-text="tologin" @click="clickLogin"></span>
      <hr/>
      <p class="order" @click="clickMyOrder">我的订单</p>
      <p class="order" @click="shipping">收货地址</p>
    </div>
  </div>
</template>

<script>
  import {mapGetters}from 'vuex'
  import {mapActions}from 'vuex'

  export default {
    name: "My",
    data:function () {
      return{
        tologin:"去登录",
        isLogin:false

      }
    },
    computed:{
      ...mapGetters(['getUser']),

    },
    mounted() {
      if(JSON.stringify(this.getUser)=='{}'){
        this.tologin="去登录"
        this.isLogin=false
      }else{
        this.tologin="个人信息"
        this.isLogin=true
      }
      this.setActions()
    },
    methods:{
      ...mapActions(['isShowFooterBar']),
      setActions(){
        this.isShowFooterBar(true)
      },
      clickLogin:function () {
        if (this.isLogin){
          //已登录
          this.$router.push("UserCenter")
        }else {
          //未登录
          this.$router.push("UserLogin")
        }
      },
      clickMyOrder:function(){
        this.$router.push("/myorder")
      },
      shipping:function () {
        this.$router.push("/AddressList")
      }


    }
  }
</script>

<style lang="stylus" scoped>

  .headimg
    width: 20%
    height:20%
    margin-top: 1rem
    margin-left :1rem
    margin-bottom :.3rem
  .tologin
    margin-left :.8rem
    font-size :.4rem
    margin-top :5rem
  .order
    margin-left :.8rem
    font-size :.4rem
    margin-top :.8rem
</style>

