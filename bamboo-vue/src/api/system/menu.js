import request from '@/utils/request'

export function getMenus() {
  return request({
    url: '/auth/system/menu/getMenus',
    method: 'get'
  })
}

export default { getMenus }