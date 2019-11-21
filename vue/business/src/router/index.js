import Vue from 'vue'
import Router from 'vue-router'

import HelloWorld from '@/components/HelloWorld'
import Home from '@/pages/home/Home'
import Cart from '@/pages/cart/Cart'
import My from '@/pages/my/My'
import Detail from '@/pages/detail/Detail'
import UserCenter from '@/pages/my/UserCenter'
import UserLogin from '@/pages/my/UserLogin'
import MyOrder from '@/pages/order/MyOrder'
import AddressEdit from '@/pages/shipping/AddressEdit'
import AddressList from '@/pages/shipping/AddressList'
import AddressList_cart from '@/pages/shipping/AddressList_cart'
import OrderDetail2 from '@/pages/order/OrderDetail2'
import pay from '@/pages/pay/Pay'
import App from "../App";

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'App',
      component: App
    },
    {
      path: '/cart',
      name: 'Cart',
      component: Cart
    },
    {
      path: '/my',
      name: 'My',
      component: My
    },
    {
      path: '/Home',
      name: 'Home',
      component: Home
    },
    {
      path:"/detail/:id",
      name:"Detail",
      component: Detail
    },
    {
      path:"/UserCenter",
      name:"UserCenter",
      component: UserCenter
    },
    {
      path: "/UserLogin",
      name: "UserLogin",
      component: UserLogin
    }
    ,
    {
      path: "/MyOrder",
      name: "MyOrder",
      component: MyOrder
    }
    ,
    {
      path: "/AddressEdit",
      name: "AddressEdit",
      component: AddressEdit
    }
    ,
    {
      path: "/AddressList",
      name: "AddressList",
      component: AddressList
    },
    {
      path: "/AddressList_cart",
      name: "AddressList_cart",
      component: AddressList_cart
    },

    {
      path: "/OrderDetail2",
      name: "OrderDetail2",
      component: OrderDetail2
    },
    {
      path: "/pay",
      name: "pay",
      component: pay
    }

  ]
})
