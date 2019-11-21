<template>
  <div>
    <van-nav-bar
      title="地址列表"
      left-text="返回"
      right-text="确定"
      left-arrow
      @click-left="onClickLeft"
      @click-right="selectShipping"

    />
  <div>
    <!--:disabled-list="disabledList"-->
    <!--disabled-text="以下地址超出配送范围"-->
  <van-address-list
    v-model="chosenAddressId"
    :list="list"
    @add="onAdd"
    @edit="onEdit"
  />
  </div>
</div>
</template>

<script>

  import {mapActions} from 'vuex'
    export default {
        name: "AddressList_cart",
      data() {
        return {
          chosenAddressId: "",
          list: [],
          areaList:[],

        }
      },

      mounted(){
        this.getAreaList()
      },
      methods: {

        selectShipping(){

          if (this.chosenAddressId==""||this.chosenAddressId==null)
            alert("请选择地址")
          else{

            this.$router.push({name:"OrderDetail2",query:{shippingId:Number(this.chosenAddressId)}})
          }

        },
          getAreaList(){
            var _vm=this
            this.service.post("/shipping/selectShippingByUserId")
              .then(function (val) {
                _vm.areaList=val.data.data;
                for (var i=0;i<_vm.areaList.length;i++){
                  var area=new Object();
                  area.id=_vm.areaList[i].id
                  area.name=_vm.areaList[i].receiverName
                  area.tel=_vm.areaList[i].receiverMobile
                  area.address=_vm.areaList[i].receiverProvince+_vm.areaList[i].receiverCity+_vm.areaList[i].receiverDistrict+
                  _vm.areaList[i].receiverAddress
                  _vm.list.push(area)
                }
              })
          },
        onClickLeft(){
          this.$router.go(-1)
        },

        onAdd() {
          // Toast('新增地址');
          this.$router.push("/AddressEdit")
        },

        onEdit(item, index) {
          Toast('编辑地址:' + index);
        }
      }
    }
</script>

<style scoped>

</style>
