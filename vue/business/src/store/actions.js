
//给action注册事件处理函数
export function  setUserInfo({commit},user){
  return commit('setUserInfo',user)
}

export function  isShowFooterBar({commit},isShow){
  return commit('isShowFooterBar',isShow)
}
export function setOrderNo({commit},orderNo) {
  return commit('setOrderNo',orderNo)
}
