
<template>
  <div class="shopcar">
    <Header></Header>
    <div class="car-list">
      <ul>
        <li class="car-item" v-for="(item,index) of good_list" :key="index">
          <div class="input-block">
            <label class="input-label" :class="{active: item.productChecked==1?true:false}" :for="'car-checkbox-'+index" @click="select_one(item.id,item.productChecked)"></label>
          </div>
          <div class="car-item-content">
            <div class="car-img">
              <img :src="item.imageHost+item.productMainImage" />
            </div>
            <div class="car-cont" >
              <h3>{{item.productName}}</h3>
            </div>
            <div class="num">
              <span @click="reduce(item.id,item.quantity)">-</span>
              <span>{{item.quantity}}</span>
              <span @click="add(item.id,item.quantity,item.c)">+</span>
            </div>
            <div class="car-price">
              <span class="car-price">￥{{item.productTotalPrice}}</span>
              <span class="car-num">X{{item.quantity}}</span>
            </div>
          </div>
        </li>
      </ul>
    </div>

    <div class="car-footer">
      <div class="foot-car">
        <label  class="input-label" :class="{active: selected_all}" @click="slect_all"></label>
      </div>
      <div class="total-cont">
        <span>总价：{{totalPrice}}</span>
        <span>共{{totalNum}}件</span>
      </div>
      <div class="btn-box">
        <button @click="selectShipping">立即下单</button>
      </div>
    </div>
  </div>
</template>

<script>
  import {store} from 'vuex'
  import Header from './components/Header'
  import axios from 'axios'

  import {mapActions}from 'vuex'

  export default {
    name: "Cart",
    data () {
      return {

        good_list:[],
        totalPrice: 0,
        totalNum: 0,
        selected_all: false
      }
    },
    components:{
      Header
    },

    mounted () {
      this.getGood_list();
      this.setActions()

    },
    watch: {
      'good_list': {
        handler: function (val, oldVal) {
          // console.log(val)
          return val;
        },
        deep: true
      }
    },
    methods: {
      ...mapActions(['isShowFooterBar']),
      setActions(){
        this.isShowFooterBar(true)
      },

      selectShipping:function(){
        var a=0
        for (var i=0;i<this.good_list.length;i++){

          if (this.good_list[i].productChecked==1)
            a=a+1;
        }
        if (a>0){
          this.$router.push("AddressList_cart")
        }

      },
      // createOrder:function(){
      //
      //   axios.get("http://localhost:8888/order/"+32)
      //     .then(this.getGood_listInfo)
      // },
      getGood_list(){
        axios.get("http://localhost:8888/cart/select")
          .then(this.getGood_listInfo)
      },
      getGood_listInfo(res){
        // console.log(res.data.status);
        // console.log(res.data.data);
        if (res.data.status==0){
          this.good_list=res.data.data.cartProductVOList;
          this.totalPrice=res.data.data.carttotalprice;
          this.selected_all=res.data.data.isallchecked
          this.getTotal();
          // console.log(this.good_list)
        }
      },
    selected(id,checked){
      axios.get("http://localhost:8888/cart/select_one?id="+id+"&checked="+checked)
        .then(this.getGood_listInfo)
    }
    ,
      getTotal () {
        this.totalNum = 0;
        if(this.good_list!=null){
        for (var i = 0; i < this.good_list.length; i++) {
          var _d = this.good_list[i]
          console.log(this.good_list[i])

            if(_d.productChecked==1?true:false){

              this.totalNum +=_d.quantity
            }
        }
        }
      },
      select_one (id,checked) {
        if (checked==0)
          checked=1;
        else checked=0;
        this.selected(id,checked)
        // if(this.good_list[index].productChecked==1?true:false){
        //   this.good_list[index].is_selected = false
        // }else{
        //   this.good_list[index].is_selected = true
        // }
      },
      slect_all () {
        if(this.selected_all){
          for (var i = 0; i < this.good_list.length; i++) {
            const id=this.good_list[i].id;

            this.selected(id,0)
            // this.good_list[i].is_selected = false;
          }

        }else{
          for (var i = 0; i < this.good_list.length; i++) {
            const id=this.good_list[i].id;
            this.selected(id,1)
            // this.good_list[i].is_selected = true;
          }

        }

      },
      reduce (index,quantity) {

        if(quantity <= 1) return;
        axios.get("http://localhost:8888/cart/rule?id="+index+"&symbol=-1")
          .then(this.getGood_listInfo)

      },
      add (index,quantity,productStock) {
        if(quantity >= productStock) return;
        axios.get("http://localhost:8888/cart/rule?id="+index+"&symbol=1")
          .then(this.getGood_listInfo)
      }
    }
  }
</script>

<style lang="stylus" scoped>
  @import "~styles/mixins.styl"
  .car-list
    background #f2f2f2
    margin-top 12px
    padding 8px
  .car-item
    border-bottom 1px solid #ddd
    position relative
    height 76px
    overflow hidden
  .car-checkbox
    display none
  .input-block
    width 30px
    float left
    height 55px
    line-height 55px
  .input-label
    cursor pointer
    position absolute
    width 18px
    height 18px
    top 18px
    left 0
    background #fff
    border:2px solid #ccc
    border-radius 50%
  .input-label:after
    opacity 0
    content ''
    position absolute
    width 9px
    height 5px
    background transparent
    top 6px
    left 6px
    border 2px solid #333
    border-top none
    border-right none
    -webkit-transform rotate(-45deg)
    -moz-transform rotate(-45deg)
    -o-transform rotate(-45deg)
    -ms-transform rotate(-45deg)
    transform rotate(-45deg)
  .car-img
    width 64px
    height 64px
    float left
    overflow hidden
  .car-img img
    width 100%
  .input-label.active
    background #F15A24
  .car-cont
    margin-left 100px

  .car-cont h3
    font-weight normal
    line-height 24px
    font-size 14px
    ellipse()
  .car-price
    position absolute
    right 12px
    bottom 0px
    width 40px
    height 40px
    text-align right
  .car-price span
    display block
    height 24px
    line-height 24px
    width 100%
  .car-footer
    height 60px
    background #f5f5f5
    position fixed
    bottom .8rem
    left 0
    right 0
  .foot-car
    width 42px
    height 60px
    float left
    margin-left 12px
    position relative
  .total-cont
    height 60px
    line-height 60px
    font-size 16px
    float left
  .total-cont span
    display inline-block
    margin-left 12px
  .btn-box
    float right
    height 60px
    line-height 60px
  .btn-box button
    height 100%
    width: 100px
    border none
    background #F15A24
    color #fff
    font-size 16px
  .num
    position absolute
    top 28px
    right 25px
    width 120px
  .num span
    display inline-block
    width 28px
    height 28px
    float left
    text-align center
    line-height 28px
    border 1px solid #ddd
    font-size 14px

</style>
