<template>
    <div>
      <mt-header title="确认订单">
        <router-link to="/" slot="left">
          <mt-button icon="back" @click="lastpage">返回</mt-button>
        </router-link>
        <!--<mt-button icon="more" slot="right"></mt-button>-->
      </mt-header>

      <p class="bigone">收货地址</p>
      <div style="padding: 0.5rem;background-color: #cccccc">
        <div>
          <p>收货人姓名：{{address.receiverName}}</p>
          <p>收货人手机号：{{address.receiverMobile}}</p>
          <p>收货地址：{{address.receiverProvince}} {{address.receiverCity}} {{address.receiverDistrict}}</p>
          <p>详细地址：{{address.receiverAddress}}</p>
        </div>
      </div>

      <p class="bigone">商品清单</p>
      <div v-for="(item,index) in cart.cartProductVOList" v-if="item.productChecked==1">
        <!--<div style="background-color: #cccccc;margin: 0.2rem 0.2rem 0 0.2rem;">-->
          <!--<p>{{item.productName}}</p>-->
          <!--<p>商品数量：{{item.quantity}}</p>-->
          <!--<p>商品单价：{{item.productPrice}}</p>-->
        <!--</div>-->
        <cart-detail :cartProductVO="item" class="border-bottom"></cart-detail>
      </div>

      <div style="float: right;margin-top: 0.1rem;">
        <p>总价￥：{{cart.carttotalprice}}</p>
        <button  style="width: 100%;height:0.5rem;background-color: red;color: white;line-height: 0.5rem" @click="pay">提交订单</button>
      </div>
    </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import {mapActions} from 'vuex'
  import cartDetail from './components/cartDetail'
  import axios from 'axios'
    export default {
        name: "OrderDetail2",
      data(){
          return{

            cart:{},
            shippingId:0,
            address:{},
            order:{}
          }
      },
      components:{
        cartDetail
      },
      methods:{

        ...mapGetters(['getOrderNo']),
        ...mapActions(['isShowFooterBar']),
        lastpage:function(){
          this.$router.go(-1)
        },
        getAddress:function () {
            console.log(this.shippingId)
          var _vm=this
          //通过axios发请求  post
          this.service.post("/shipping/selectShippingByShippingId",{
            "shippingId":_vm.shippingId
          }).then(function (response) {
            console.log(response)
            _vm.address=response.data.data
          }).catch(function (error) {
            console.log(error)
          })
        },
        pay:function () {

            var _vm=this
          this.service.post("/order/createOrder?shippingId="+this.shippingId)
        .then(function (res) {
            console.log(res.data.data)
            _vm.order=res.data.data

            _vm.toPay()
          });
        },
        toPay(){
          console.log(this.order)
          this.$router.push({name:"pay",query:{"orderNo":this.order.orderNo}})
        },
        getShippingId(){
            this.shippingId=this.$route.query.shippingId
            this.getAddress()
        },
        getCartDetail(){
          var _vm=this
          //通过axios发请求  post
          this.service.post("/cart/select").then(function (response) {
            console.log(response)
            _vm.cart=response.data.data

          }).catch(function (error) {
            console.log(error)
          })
        },



      },
      mounted() {
        this.getShippingId()
        this.getCartDetail()
        this.isShowFooterBar(false)
      //this.orderNo=this.getOrderNo()

       }
    }
</script>

<style scoped lang="stylus">
  @import '~styles/mixins.styl'
  .recommend-title
    line-height :.8rem
    background :#eee
    margin-top:.2rem
    text-indent:.2rem
  .bigone
    height:0.4rem
    font-size:0.3rem
    font:weight
    margin-top:0.3rem
    margin-left:0.2rem
  .item
    overflow :hidden
    height:1.9rem
    display :flex
    .item-img
      width :1.7rem
      height :1.7rem
      padding :.1rem
    .item-info
      flex:1
      padding .1rem
      min-width:0
      .item-title
        line-height: .54rem
        font-size :.32rem
        ellipsis()
      .item-desc
        line-height :.4rem
        color:#ccc
        ellipsis()
      .item-price
        margin-top:.16rem
        line-height :.32rem
        font-size :.32rem
        color:red

</style>
