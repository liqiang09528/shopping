<template>
    <swiper :options="swiperOption" ref="mySwiper" >
      <!-- slides -->
      <swiper-slide><img class="swiper-img" src="http://img.cdn.imbession.top/combanner.png"/></swiper-slide>
      <swiper-slide><img class="swiper-img" src="http://img.cdn.imbession.top/topbanner.png"/></swiper-slide>
      <swiper-slide>I8'm Slide 3</swiper-slide>
      <swiper-slide>I'm Slide 4</swiper-slide>
      <swiper-slide>I'm Slide 5</swiper-slide>
      <swiper-slide>I'm Slide 6</swiper-slide>
      <swiper-slide>I'm Slide 7</swiper-slide>
      <!-- Optional controls -->
      <div class="swiper-pagination"  slot="pagination"></div>
      <!--<div class="swiper-button-prev" slot="button-prev"></div>-->
      <!--<div class="swiper-button-next" slot="button-next"></div>-->
      <!--<div class="swiper-scrollbar"   slot="scrollbar"></div>-->
    </swiper>


</template>

<script>
    export default {
        name: "Carousel",
      data(){
          return{
            swiperOption:{
              pagination:'.swiper-pagination',
              loop:true
            }
          }
      }
    }
</script>

<style lang="stylus" scoped>
  .swiper-img
    width 100%
  .swiper-pagination>>>.swiper-pagination-bullet-active
    background-color white
</style>
