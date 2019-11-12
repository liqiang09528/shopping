<template>

     <div class="icons" >
       <swiper :options="swiperOption" ref="mySwiper" >
         <swiper-slide v-for="(pageArray,index) of pages"  :key="index">
       <div class="icon" v-for="(item,index) of pageArray">
         <div class="icon-img">
           <img class="icon-img-content" :src="item.categoryUrl"/>
        </div>
        <p class="icon-desc">{{item.categoryName}}</p>
      </div>
         </swiper-slide>

       </swiper>
    </div>









</template>

<script>
    export default {
        name: "Category",
        data(){
          return{
            swiperOption:{},
            categoryList:[
              {
                "id":1,
                "categoryUrl":"http://img1.qunarzz.com/piao/fusion/1611/54/ace00878a52d9702.png",
                "categoryName":"手机"
              },
              {
                "id":1,
                "categoryUrl":"http://img1.qunarzz.com/piao/fusion/1611/54/ace00878a52d9702.png",
                "categoryName":"手机"
              },
              {
                "id":1,
                "categoryUrl":"http://img1.qunarzz.com/piao/fusion/1611/54/ace00878a52d9702.png",
                "categoryName":"手机"
              },
              {
                "id":1,
                "categoryUrl":"http://img1.qunarzz.com/piao/fusion/1611/54/ace00878a52d9702.png",
                "categoryName":"手机"
              },
              {
                "id":1,
                "categoryUrl":"http://img1.qunarzz.com/piao/fusion/1611/54/ace00878a52d9702.png",
                "categoryName":"手机"
              },
              {
                "id":1,
                "categoryUrl":"http://img1.qunarzz.com/piao/fusion/1611/54/ace00878a52d9702.png",
                "categoryName":"手机"
              },
              {
                "id":1,
                "categoryUrl":"http://img1.qunarzz.com/piao/fusion/1611/54/ace00878a52d9702.png",
                "categoryName":"手机"
              },
              {
                "id":1,
                "categoryUrl":"http://img1.qunarzz.com/piao/fusion/1611/54/ace00878a52d9702.png",
                "categoryName":"手机"
              },
              {
                "id":1,
                "categoryUrl":"http://img1.qunarzz.com/piao/fusion/1611/54/ace00878a52d9702.png",
                "categoryName":"手机"
              }
            ]
          }
        },
        computed:{
          pages(){
            const pages=[]
            this.categoryList.forEach((item,index)=>{
              const page=Math.floor(index/8)
              if (!pages[page]){
                pages[page]=[]
              }
              pages[page].push(item)

            })
            return pages;
          }
        }

    }
</script>

<style lang="stylus" scoped>
  @import "~styles/mixins.styl"

  .icons>>>.swiper-container
    width:100%
    height 0
    padding-bottom 50%
    overflow hidden
  .icon
    width 25%
    height 0
    padding-bottom 25%
    overflow hidden
    float left
    position relative
    .icon-img
      position absolute
      left 0
      right 0
      top 0
      bottom .44rem
      box-sizing border-box
      padding .1rem
      .icon-img-content
        height 100%
        display block
        margin 0 auto
    .icon-desc
      position absolute
      left 0
      right 0
      bottom 0
      height .44rem
      color #333
      text-align center
      ellipsis()








</style>
