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

export function addMenu(data) {
  return request({
    url: '/auth/system/menu/add',
    method: 'post',
    data
  })
}

export function deleteMenu(menuId) {
  return request({
    url: `/auth/system/menu/del/${menuId}`,
    method: 'delete'
  })
}

export function updateMenu(data) {
  return request({
    url: `/auth/system/menu/update`,
    method: 'put',
    data
  })
}

export default { getMenus, getChildKeys, addMenu, deleteMenu, updateMenu }
