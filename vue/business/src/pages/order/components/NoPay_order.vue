<template>
  <div >
    <ul v-if="orderList!=null">
      <li class="order-item" v-for="(order,index) of orderList" :key="index" v-if="order.status==10">
        <div class="orderContainer">
          <div class="order-info">
            <div class="orderNo">订单号{{order.orderNo}}</div>
            <div class="orderStatus">{{order.statusDesc}}</div>
          </div>
          <div class="orderContentContainer">
            <ul>
              <li class="orderItemContainer" v-for="orderItem in order.orderItemVoList">
                <img class="productImage" :src="order.imageHost+orderItem.productImage" />
                <div class="orderItemInfo">
                  <p class="productName">{{orderItem.productName}}</p>
                  <p class="quantity">数量X{{orderItem.quantity}}</p>
                  <p class="totalprice">价格{{orderItem.totalPrice}}</p>
                </div>
              </li>
            </ul>
            <div class="orderBtnDiv" v-if="order.status==10">
              <p class="orderBtnPrice" >总价格￥{{order.payment}}</p>
              <mt-button class="orderBtn" size="small">去支付</mt-button>
            </div>
            <div class="orderBtnDiv" v-else>
              <p class="orderBtnPrice" >总价格￥{{order.payment}}</p>
              <mt-button class="orderBtn" size="small">查看订单详情</mt-button>
            </div>
          </div>
        </div>
      </li>
    </ul>
    <ul v-else>
      无该类订单
    </ul>
  </div>
</template>

<script>
  export default {
    name: "all_order",
    props:{
      orderList:Array
    },
    methods:{

    }
  }
</script>

<style lang="stylus" scoped>

  @import "~styles/mixins.styl"
  .order-item
    width 95%
    margin 0 auto
    background #f0f0f0
    border-radius .1rem
    .orderContainer
      width 90%
      margin 0 auto
      .order-info
        display flex
        margin-top .2rem
        justify-content space-between
        padding-top .2rem
        padding-bottom .2rem
        border-bottom 1px solid #ffffff
    list-style none
    .orderContentContainer
      margin-top .4rem
      .orderItemContainer
        display flex
        margin-top .2rem
        .orderItemInfo
          width 90%
          margin-left .2rem
          position relative
          display flex
          flex-direction column
        .productImage
          width .618rem
          height 1rem
        .productName
          ellipse()
        .quantity
          margin-top .1rem
        .totalprice
          margin-top .1rem
    .orderBtnDiv
      position relative
      height .9rem
    .orderBtn
      position absolute
      right .2rem
      bottom .2rem
    .orderBtnPrice
      position absolute
      right 3rem
      bottom .4rem



</style>
