<template>
  <div>
    <div class="banner" @click="handlerClickBanner">
      <img class="banner-img" :src="productUrl"/>
      <div class="banner-info">
        <div class="banner-number">
          <span class="iconfont banner-icon">&#xe625;</span>
          {{imgList.length}}
        </div>
      </div>
    </div>
    <Gallery :imgList="imgList" v-show="showGallery" @close="handlerClose"></Gallery>
  </div>
</template>

<script>

  import Gallery from '@/common/gallery/Gallery'

    export default {
        name: "Banner",
      components: {
        Gallery
      },
      props:{
        detailInfo:Object
      },
      data(){
          return{
            showGallery:false
          }
      },
      methods:{
        handlerClickBanner:function () {
          this.showGallery=true
        },
        handlerClose:function () {
          this.showGallery=false
        }
      },
      computed:{
        productUrl:function () {
          return this.detailInfo.imageHost+this.detailInfo.mainImage
        },
        imgList:function () {
          const imgList=(this.detailInfo.subImages||"").split(",");
          for (var i=0;i<imgList.length;i++){
            imgList[i]=this.detailInfo.imageHost+imgList[i];
          }
          return imgList
        }
      }
  }
</script>

<style lang="stylus" scoped>

  .banner
    overflow hidden
    height 0
    padding-bottom 70%
    position relative
    .banner-img
      width 100%
    .banner-info
      position absolute
      left 0
      right 0
      bottom 0
      .banner-number
        float right
        margin-right .1rem
        background rgba(0,0,0,.8)
        color white
        border-radius .1rem
        padding .05rem
        font-size .24rem
        .banner-icon
          font-size .24rem
</style>

