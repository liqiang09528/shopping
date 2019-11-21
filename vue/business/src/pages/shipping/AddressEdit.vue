<template>
  <!--show-set-default-->
  <!--show-postal-->
  <van-address-edit
    :area-list="areaList"


    show-delete
    show-search-result
    :search-result="searchResult"
    :area-columns-placeholder="['请选择', '请选择', '请选择']"
    @focus="onFocus"
    @change-area="onArea"
    @save="onSave"
    @delete="onDelete"
    @change-detail="onChangeDetail"
  />
</template>

<script>
    import areaList from 'styles/area.js'
    import axios from 'axios'
    export default {
        name: "AddressEdit",
      data() {
        return {
          postCode:"",
          detail:"",
          province:"",
          city:"",
          county:"",
          areaList,
          searchResult: []
        }
      },

      methods: {

        onFocus(a){
          console.log(a)

          if (a=="addressDetail") {
            var _vm = this
            axios.get("http://localhost:8888/shipping/selectPostByAddress/?receiverProvince=" + this.province + "&" +
              "receiverCity=" + this.city + "&receiverDistrict=" + this.county)
              .then(function (data) {
                const rs = data.data.rs;
                for (var i = 0; i < rs.length; i++) {
                  console.log(rs[i])
                  if (rs[i].POSTCODE != -1) {

                    _vm.postCode = rs[i].POSTCODE

                    break;
                  }
                }
                console.log(_vm.postCode)
              })
          }
        },
        onArea(area){
          if (area[0].name==area[1].name){
            this.city=area[1].name;
            this.county=area[2].name;
          }else{
            this.province=area[0].name;
            this.city=area[1].name;
            this.county=area[2].name;
          }

          console.log(this.area)
        },
        onSave(content) {
         console.log(content)



             },
        onDelete() {
          Toast('delete');
        },
        onChangeDetail(detail) {
          console.log(detail)
          this.detail=detail;
          console.log(this.detail)

          // if (val) {
          //   this.searchResult = [{
          //     name: '黄龙万科中心',
          //     address: '杭州市西湖区'
          //   }];
          // } else {
          //   this.searchResult = [];
          // }
        }
      }
    }
</script>

<style scoped>

</style>
