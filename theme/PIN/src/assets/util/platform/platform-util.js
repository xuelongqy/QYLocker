/**
 * 验证运行终端的平台
 * @param platform 平台验证信息
 * @return {boolean} 是否正确
 */
export let isPlatform = function (platform) {
  var userAgentInfo = navigator.userAgent;
  return userAgentInfo.toLowerCase().indexOf(platform.toLowerCase()) > -1
}

export default {
  isPlatform
}
