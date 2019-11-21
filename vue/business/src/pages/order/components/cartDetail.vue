<template>

    <van-card
      :num="cartProductVO.quantity"
      :price="cartProductVO.productPrice"
      :desc="cartProductVO.productSubtitle"
      :title="cartProductVO.productName"
      :thumb="cartProductVO.imageHost+cartProductVO.productMainImage"
    >
      <div slot="footer">
        <p>￥{{cartProductVO.productTotalPrice}}</p>
      </div>
    </van-card>
</template>

<script>
    export default {
      name: "cartDetail",
      props:{
        cartProductVO:Object
      }
    }
</script>

<style scoped>

</style>
