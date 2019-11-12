<template>
  <div>
    <Banner :detailInfo="detailInfo"></Banner>
    <Header></Header>
  </div>

</template>

<script>

  import Banner from './components/Banner'
  import Header from './components/Header'

  import axios from 'axios'
    export default {
      name: "Detail",
      components:{
        Banner,
        Header
      },
      data(){
        return{
          detailInfo:{}
        }
      },
      mounted(){
        this.getDetailInfo();
      },
      methods:{
        getDetailInfo(){
          const id=this.$route.params.id;

          axios.get("http://localhost:8888/product/"+id)
            .then(this.getDetailInfoRes)
        },
        getDetailInfoRes(res){
          if (res.data.status==0&&res.data.data){
            this.detailInfo=res.data.data
          }
        }
      }

    }
</script>

<style scoped>

</style>
