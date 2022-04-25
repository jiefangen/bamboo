import request from '@/utils/request'

export function add(data) {
  return request({
    url: '/auth/system/user/add',
    method: 'post',
    data
  })
}

export function del(username) {
  return request({
    url: `/auth/system/user/del/${username}`,
    method: 'delete'
  })
}

export function edit(data) {
  return request({
    url: '/auth/system/user/edit',
    method: 'put',
    data
  })
}

export function updatePass(data) {
  return request({
    url: '/auth/system/user/updatePassword',
    method: 'post',
    data
  })
}

export function updateUserRole(data) {
  return request({
    url: '/auth/system/user/updateUserRole',
    method: 'post',
    data
  })
}

// 返回用户的信息以及角色信息
export function getInfo(username) {
  return request({
    url: '/auth/system/user/info',
    method: 'get',
    params: { username }
  })
}

export function getList(data) {
  return request({
    url: '/auth/system/user/page',
    method: 'post',
    data
  })
}

export default { add, edit, del, getInfo, getList, updatePass }
