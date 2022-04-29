import request from '@/utils/request'

export function getMenus() {
  return request({
    url: '/auth/system/menu/getMenus',
    method: 'get'
  })
}

export function getChildKeys(menuId) {
  return request({
    url: '/auth/system/menu/getChildKeys',
    method: 'get',
    params: { menuId }
  })
}

export default { getMenus, getChildKeys }
