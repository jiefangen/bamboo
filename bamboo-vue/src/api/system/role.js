import request from '@/utils/request'

export function getRoutes() {
  return request({
    url: '/auth/system/role/getRoutes',
    method: 'get'
  })
}

export function getRoles() {
  return request({
    url: '/auth/system/role/getRoles',
    method: 'get'
  })
}

export function addRole(data) {
  return request({
    url: '/auth/system/role/add',
    method: 'post',
    data
  })
}

export function updateRole(roleId, data) {
  return request({
    url: `/auth/system/role/update/${roleId}`,
    method: 'put',
    data
  })
}

export function deleteRole(roleId) {
  return request({
    url: `/auth/system/role/del/${roleId}`,
    method: 'delete'
  })
}

export default { getRoutes, getRoles, addRole, updateRole, deleteRole }
