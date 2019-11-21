<template>
  <div>
    <Header></Header>
    <Carousel></Carousel>
    <Category></Category>
    <Hot :hotList="hotList"></Hot>

  </div>
</template>

<script>
  import Header from './components/Header'
  import Carousel from './components/Carousel'
  import Category from './components/Category'
  import Hot from './components/Hot'
  import axios from 'axios'
  import {mapActions}from 'vuex'
    export default {
      name: "Home",
      components:{
          Header,
          Carousel,
          Category,
          Hot
      },
      data(){
        return{
          hotList:[]
        }
      },
      mounted() {
        //热门商品
        this.getHotList()
        //轮播图
        //类别
        this.setActions()
      },
      methods:{
        ...mapActions(['isShowFooterBar']),
        setActions(){
          this.isShowFooterBar(true)
        },
        getHotList(){
          axios.get("http://localhost:8888/product/isHot")
            .then(this.getHotListInfo)
        },

        getHotListInfo(res){
          console.log(res.data.data);
          console.log(res.data.status);

          if (res.data.status==0){
            this.hotList=res.data.data;
            console.log(this.hotList)
          }
        }
      }
    }
</script>

<style scoped>

</style>
