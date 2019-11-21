<template>
    <div>
      <div class="title">热销推荐</div>

      <dl>
        <router-link tag="li" :to="'/detail/'+item.id" class="item border-bottom" v-for="(item,index) of hotList" :key="index">
          <img class="item-img" :src="item.imageHost+item.mainImage">
          <div class="item-info">
            <p class="item-title">{{item.name}}</p>
            <p class="item-desc">{{item.subtitle}}</p>
            <p class="item-price">￥{{item.price}}</p>
          </div>
        </router-link>
      </dl>
    </div>
</template>

<script>
    export default {
        name: "Hot",
        props:{
          hotList:Array
        },
        data(){
          return{
          }
        },

    }
</script>

<style lang="stylus" scoped>

  @import "~styles/mixins.styl"
  .title
    background #eee
    line-height .8rem
    text-indent .2rem
  .item
    display flex
    overflow hidden
    height 2rem
    .item-img
      width 1.8rem
      height 1.8rem
      padding .1rem
    .item-info
      flex 1
      padding .1rem
      min-width 0
      .item-title
        line-height .54rem
        font-size .30rem
        ellipse()
      .item-desc
        line-height .54rem
        font-size .22rem
        ellipse()
      .item-price
        color #ff261E



</style>
