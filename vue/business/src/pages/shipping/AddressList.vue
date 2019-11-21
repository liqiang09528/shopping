<template>
  <div>
    <van-nav-bar
      title="地址列表"
      left-text="返回"

      left-arrow
      @click-left="onClickLeft"

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
    export default {
        name: "AddressList",
      data() {
        return {
          chosenAddressId: '',
          list: [],
          areaList:[],

        }
      },

      mounted(){
        this.getAreaList()
      },
      methods: {


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
