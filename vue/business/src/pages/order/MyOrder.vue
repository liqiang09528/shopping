<template>
  <div>
    <mt-header title="我的订单">
      <router-link to="/my" slot="left">
        <mt-button  icon="back" @click="back">返回</mt-button>
      </router-link>
      <!--<mt-button icon="more" slot="right"></mt-button>-->
    </mt-header>
    <mt-navbar v-model="selected">
      <mt-tab-item id="1">全部订单</mt-tab-item>
      <mt-tab-item id="2">已付款</mt-tab-item>
      <mt-tab-item id="3">已发货</mt-tab-item>
      <mt-tab-item id="4">未付款</mt-tab-item>
    </mt-navbar>

    <div id="corder_loadmore" ref="wrapper" :style="{height:this.wrapperHeight+'px'}">
      <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom"
                   :bottom-all-loaded="allLoaded" ref="loadmore"
                   :autoFill="autoFill">
        <!-- tab-container -->
        <mt-tab-container v-model="selected">
          <mt-tab-container-item id="1">
            <All_order :orderList="orderList"></All_order>
          </mt-tab-container-item>
          <mt-tab-container-item id="2">
            <Pay_order :orderList="orderList"></Pay_order>
            <!--<mt-cell v-for="(n,id) in 4" :key="id" :title="'测试 ' + n" />-->
          </mt-tab-container-item>
          <mt-tab-container-item  id="3">
           <shipped_order :orderList="orderList"></shipped_order>
          </mt-tab-container-item>
          <mt-tab-container-item id="4">
            <no-pay_order :orderList="orderList"></no-pay_order>
          </mt-tab-container-item>
        </mt-tab-container>
      </mt-loadmore>
    </div>
  </div>
</template>

<script>
    import {mapActions} from 'vuex'
    import All_order from './components/All_order'
    import Pay_order from './components/Pay_order'
    import NoPay_order from './components/NoPay_order'
    import Shipped_order from './components/Shipped_order'
    export default {
      name: "MyOrder",
      data(){
        return{
          selected:"1",
          allLoaded:false,
          pageModel:{},
          autoFill:true,
          wrapperHeight:0
        }
      },
      components:{
        All_order,
        Pay_order,
        NoPay_order,
        Shipped_order
      },
      computed:{
        orderList:function () {
          return this.pageModel.list
        }
      },
      methods:{
        back:function () {
          this.$router.go(-1)
        },
        ...mapActions(['isShowFooterBar']),
        loadTop:function () {
          //获取接口数据
          this.getMyOrderList('refresh',1,1)
          // 加载更多数据
          this.$refs.loadmore.onTopLoaded();
        },
        loadBottom:function() {
          // 加载更多数据
          if(this.pageModel.hasNextPage){
                this.getMyOrderList("loadmore",this.pageModel.pageNum+1,10)
          }else{
            this.allLoaded = true;// 若数据已全部获取完毕
            this.$refs.loadmore.onBottomLoaded();
          }
        },
          getMyOrderList:function(optype,pageNo,pageSize){
          var _vm=this
          this.service.post("/order/selectOrder",{
            "pageNum":pageNo,
            "pageSize":pageSize
          }).then(function (response) {
            console.log(response)
            console.log(response.data.data)
            console.log(response.data.data.list[0])
            if(optype=='refresh'){
              _vm.pageModel=response.data.data
              _vm.$refs.loadmore.onTopLoaded();
              _vm.allLoaded=false
              //_vm.status=response.data.status
            }else if(optype=='loadmore'){
              if(response.data.data.list.length>0){
                const oldOrders=_vm.pageModel.list
                console.log("=====旧数据=====")
                console.log(oldOrders)
                var orderItem;
                var newArrayOrder=oldOrders.concat(response.data.data.list)
                response.data.data.list=newArrayOrder
                console.log("------新数据--------")
                console.log(response.data.data.list)
              }else{
                //加载完成
                _vm.allLoaded=true;//若数据已全部加载完毕
              }
              _vm.pageModel=response.data.data
              console.log("loadmore")
              console.log(_vm.pageModel.list)
              _vm.$refs.loadmore.onBottomLoaded()
            }
          }).catch(function(error){
            console.log(error)
          })
        },
      },

      watch:{
        selected: function (newval,oldval) {
          this.getMyOrderList('refresh',1,10)
          /*
           * 我的上拉下拉是个切换卡，因为loadmore是一个容器，
           * 所以你往上拉的时候，点击另一个tab它的内容在tab切换时增加滚到顶部即可
           */
          var loadme=document.getElementById('corder_loadmore');
          loadme.scrollTop=0
        }
      },
      created(){
          // this.isShowFooterBar(false)
          var _vm=this
          window.onscroll=function () {
            // _vm.isShowFooterBar(true)

            //变量scrollTop是滚动条滚动时，距离顶部的距离
            var scrollTop=document.documentElement.scrollTop||document.body.scrollTop;
            //变量windowHeight是可视区高度
            var windowHeight=document.documentElement.clientHeight||document.body.clientHeight;
            //变量scrollHeight是滚动条高度
            var scrollHeight=document.documentElement.scrollHeight||document.body.scrollHeight;
            //滚动条到底部的条件
            // if((scrollTop+windowHeight+60)>=scrollHeight){
            //   //写后台加载数据的函数
            //   //console.log("距顶部"+scrollTop+"可视区高度"+windowHeight+"滚动条总高度"+scrollHeight);
            //   _vm.isShowFooterBar(false)
            // }
            // else{
            //   _vm.isShowFooterBar(true)
            // }
          }
      },
      mounted(){
        this.getMyOrderList('refresh',1,10)
        //计算外层div的高度
        this.wrapperHeight=document.documentElement.clientHeight
          -this.$refs.wrapper.getBoundingClientRect().top;

        //Toast("gaodu="+this.wrapperHeight)
        this.isShowFooterBar(false)
      }
    }
</script>

<style lang="stylus" scoped>
  @import "~styles/mixins.styl"
  .order-list
    background #f2f2f2
    margin-top 12px
    padding 8px
  .order-item
    border-bottom 1px solid #ddd
    position relative
    /*height 76px*/
    overflow hidden


  .order-item-content

    height 76px
  .order-img
    width 64px
    height 64px
    float left
    overflow hidden
  .order-img img
    width 100%
  .input-label.active
    background #F15A24
  .order-cont
    margin-left 100px

  .order-cont h3
    font-weight normal
    line-height 24px
    font-size 14px
    ellipse()
  .order-price
    position absolute
    right 12px
    bottom 0px
    width 40px
    height 40px
    text-align right
  .order-price span
    display block
    height 24px
    line-height 24px
    width 100%



  .num
    position absolute
    top 28px
    right 25px
    width 120px
  .num span
    display inline-block
    width 28px
    height 28px
    float left
    text-align center
    line-height 28px
    border 1px solid #ddd
    font-size 14px
  /*.orderNo*/
    /*float left*/
  /*.statusDesc*/
    /*float right*/

</style>
