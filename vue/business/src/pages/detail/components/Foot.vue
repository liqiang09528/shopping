<template>
  <!--<div class="car-footer">-->
    <!--<div class="btn-box" @click="getAddCart">-->
      <!--<button>加入购物车</button>-->
    <!--</div>-->
  <!--</div>-->
  <van-goods-action>
    <van-goods-action-icon icon="chat-o" >
      客服
    </van-goods-action-icon>
    <van-goods-action-icon icon="cart-o" >
      购物车
    </van-goods-action-icon>
    <van-goods-action-button type="warning" @click="getAddCart">
      加入购物车
    </van-goods-action-button>
    <van-goods-action-button type="danger" >
      立即购买
    </van-goods-action-button>
  </van-goods-action>
</template>

<script>

  import axios from 'axios'
    export default {
      name: "foot",
      props:{
        id:String
      },

      methods:{
        getAddCart(){
          axios.get("http://localhost:8888/cart/add/"+this.id+"/1")
            .then(this.getDetailInfoRes)
        },
        getAddCartInfo(res){
          if (res.data.status==0&&res.data.data){
              console.log(res.data.data)
             }
        }
      }
    }
</script>

<style lang="stylus" scoped>

  .car-footer
    height 60px
    background #f5f5f5
    position fixed
    bottom .8rem
    left 0
    right 0
  .btn-box
    float right
    height 60px
    line-height 60px
  .btn-box button
    height 100%
    width: 100px
    border none
    background #F15A24
    color #fff
    font-size 16px
</style>
