<template>
  <div>
    <van-nav-bar
      title="支付"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
    />
    <div>
     <p>{{this.orderNo}}</p>
    <img :src="this.imageURL"/>
  </div>
  </div>
</template>

<script>
    export default {
      name: "Pay",
      data(){
        return{
          orderNo:0,

          imageURL:"",

          failed:0


        }
      },
      methods:{

        onClickLeft(){

          this.$router.go(-1)
        },
        getOrderNo(){

          this.orderNo=this.$route.query.orderNo

          // this.pay()
        }
        ,
        pay(orderNo){
          var _vm=this
          this.service.post("order/pay?orderNo="+this.orderNo)
            .then(function (res) {

              _vm.imageURL=res.data.data.qrcode
              console.log(_vm.imageURL)
              _vm.isPay()
            })
        },

        //轮询
        isPay(interval){
          interval=interval || 5000;
          console.log(interval)
          meter1=setTimeout(this.isPayInfo(),interval)
        },
        isPayInfo(){
          var _vm=this
            this.service.post("order/isPay?orderNo="+this.orderNo)
              .then(function (res) {
                console.log(res.data.data)
                if (res.data.data=="success"){

                  _vm.$router.push({name:"App",query:{selected:1}});
                  clearTimeout(meter1)
                }

                _vm.isPay()
              })
              .catch(function (err) {
                console.log(err)
                // if (_vm.failed<=10)
                //   _vm.isPay(interval+1000)
              })

        }

      },
      mounted(){
        this.getOrderNo()
        this.pay()

      }
    }
</script>

<style scoped>

</style>
