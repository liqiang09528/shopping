<template>
    <div class="container" @click="handlerClickGallery">
      <div class="wrapper">
        <swiper :options="swiperOption" ref="mySwiper">
          <swiper-slide v-for="(item,index) of imgList" :key="index">
            <img class="swiper-img" :src="item"/>
          </swiper-slide>
      <div class="swiper-pagination" slot="pagination"></div>
        </swiper>
      </div>

    </div>
</template>

<script>
    export default {
        name: "Gellery",
        props:{
          imgList:Array
        },
        data(){
          return{
            swiperOption:{
              pagination: '.swiper-pagination',
              paginationType:'fraction',
              loop:true,
              observer:true,
              observeParents:true
            }
          }
        },
        methods:{
          handlerClickGallery:function () {
            this.$emit("close")
          }
        }
    }
</script>

<style lang="stylus" scoped>
  .container>>>.swiper-container
    overflow inherit
  .container
    position fixed
    left 0
    right 0
    bottom 0
    top 0
    background black
    z-index 99
    display flex
    flex-direction column
    justify-content center
    .wrapper

      height 0
      width 100%
      padding-bottom 100%
      background  white
      .swiper-img
        width 100%
      .swiper-pagination
        bottom -1rem
        color white
</style>
