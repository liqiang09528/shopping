<template>
  <div id="app">

    <router-view/>
      <!--<div v-show="this.isShowFooterBar">-->
      <!--<mt-tabbar v-model="selected" :fixed="fixed">-->
        <!--<mt-tab-item id="0">-->
          <!--&lt;!&ndash;<img slot="icon" src="../assets/100x100.png">&ndash;&gt;-->
          <!--<span class="iconfont">&#xe62e;</span><br/>-->
          <!--首页-->
        <!--</mt-tab-item>-->
        <!--&lt;!&ndash;<mt-tab-item id="订单">&ndash;&gt;-->
        <!--&lt;!&ndash;&lt;!&ndash;<img slot="icon" src="../assets/100x100.png">&ndash;&gt;&ndash;&gt;-->
        <!--&lt;!&ndash;订单&ndash;&gt;-->
        <!--&lt;!&ndash;</mt-tab-item>&ndash;&gt;-->
        <!--<mt-tab-item id="1">-->
          <!--&lt;!&ndash;<img slot="icon" src="../assets/100x100.png">&ndash;&gt;-->
          <!--<span class="iconfont">&#xe600;</span><br/>-->
          <!--购物车-->
        <!--</mt-tab-item>-->
        <!--<mt-tab-item id="2">-->
          <!--&lt;!&ndash;<img slot="icon" src="../assets/100x100.png">&ndash;&gt;-->
          <!--<span class="iconfont">&#xe62f;</span><br/>-->
          <!--我的-->
        <!--</mt-tab-item>-->
      <!--</mt-tabbar>-->
      <!--</div>-->
    <div v-show="this.isShowFooterBar">
    <van-tabbar v-model="selected" :fixed="fixed">
      <van-tabbar-item icon="home-o">首页</van-tabbar-item>
      <van-tabbar-item icon="shopping-cart-o">购物车</van-tabbar-item>
      <van-tabbar-item icon="user-o">我的</van-tabbar-item>
    </van-tabbar>
    </div>
  </div>

</template>

<script>
  import {mapGetters} from 'vuex'
export default {
  name: 'App',
  data:function () {
    return{

      fixed:true,
      selected:0
    }
  },
  computed:{
    ...mapGetters(['isShowFooterBar'])
  },
  watch:{
    selected: function (value) {
      if (value==0){
        this.$router.push("Home");
      }else if (value==1){
        this.$router.push("Cart");
      }else if (value==2){
        this.$router.push("My");
      }
    }
  },
  mounted() {
    this.getSelected()
  },
  methods:{
    getSelected(){
      const value=this.$route.query.selected
      if (value!=null||value!="")
        this.selected==value
      if (this.selected==0){
        this.$router.push("Home");
      }else if (this.selected==1){
        this.$router.push("Cart");
      }else if (this.selected==2){
        this.$router.push("My");
      }
    }
  }
}
</script>

<style>
#app {
  /*font-family: 'Avenir', Helvetica, Arial, sans-serif;*/
  /*-webkit-font-smoothing: antialiased;*/
  /*-moz-osx-font-smoothing: grayscale;*/
  /*!*text-align: center;*!*/
  /*color: #2c3e50;*/
  /*margin-top: 0;*/
}
</style>
