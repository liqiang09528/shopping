<template>
  <div class="header">
    <div class="header-left"><span class="iconfont">&#xe624;</span></div>
    <div class="header-content">
      <span class="iconfont">&#xe632;</span>
      <span class="warn">输入要搜索的商品</span>
    </div>
  </div>

</template>

<script>
    export default {
        name: "Header"
    }
</script>

<style lang="stylus" scoped>
  /*.header*/
    /*width :100rem*/
    /*background-color :#25a4bb*/

  @import '~styles/varibles.styl'

  .header
    height .88rem
    background-color $bgColor
    display flex
    line-height .88rem
    .header-left
      width 0.72rem
      height 0.72rem
    .header-content
      flex 1
      background-color white
      height 0.6rem
      line-height .6rem
      margin-top 0.14rem
      margin-right 0.2rem
      border-radius 0.1rem
      -webkit-border-radius .1rem
      -moz-border-radius .1rem





</style>
