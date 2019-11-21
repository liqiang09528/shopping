<template>
  <div>
    <Banner :detailInfo="detailInfo"></Banner>
    <Header></Header>
    <Foot v-bind:id="id"></Foot>
    <!--<FootBor></FootBor>-->
  </div>

</template>

<script>

  import Banner from './components/Banner'
  import Header from './components/Header'
  import Foot from './components/Foot'
  import FootBor from './components/FootBor'
  import {mapActions} from 'vuex'

  import axios from 'axios'
    export default {
      name: "Detail",
      components:{
        Banner,
        Header,
        Foot,
        FootBor
      },
      data(){
        return{
          detailInfo:{},
          id:""
        }
      },
      mounted(){
        this.getDetailInfo();
        this.setActions()
      },
      methods:{
        ...mapActions(['isShowFooterBar']),
        setActions(){
          this.isShowFooterBar(false)
        },
        getDetailInfo(){
          const id=this.$route.params.id;
          this.id=id;
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
