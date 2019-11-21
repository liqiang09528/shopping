<template>
   <div>
     <div class="header-abs" v-show="showHeader">
       <router-link tag="div" to="/" class="iconfont header-abs-back">&#xe624;</router-link>
     </div>
     <div class="header-fixed" :style="opacityStyle" v-show="!showHeader">
       <router-link tag="div" to="/" class="iconfont header-abs-back">&#xe624;</router-link>
     </div>
   </div>
</template>

<script>
    export default {
      name: "Header",
      data(){
        return{
          showHeader:true,
          opacityStyle:{
            opacity:0
          }
        }
      },
      mounted() {
        window.addEventListener('scroll',this.handleScroll)
      },
      methods:{
        handleScroll () {
          const top=document.documentElement.scrollTop;
          if (top>60){
            //60-140之间
            let opacity=top/140;
            opacity=(opacity>=1?1:opacity);
            this.opacityStyle={opacity};
            this.showHeader=false
          }else {
            this.showHeader=true
          }
        }
      }
    }
</script>

<style lang="stylus"  scoped>

  .header-abs
    position absolute
    left .2rem
    top .2rem
    width .8rem
    height .8rem
    line-height .8rem
    border-radius .4rem
    text-align center
    background rgba(0,0,0,.8)
    .header-abs-back
      color white
      font-size .4rem
  .header-fixed
    position fixed
    left 0
    top 0
    right 0
    background #25a4bb
    height .8rem
    line-height .8rem
    text-align left
    .header-fixed-back

      color white
      width .64rem
      font-size .4rem
</style>
