import { constantRoutes } from '@/router'

import Layout from '@/layout'

/**
 * 组件映射容器
 */
const asyncRouteMap = {
  layout: Layout,
  dashboard: () => import('@/views/dashboard/index'),
  sys_user: () => import('@/views/system/user/index'),
  sys_role: () => import('@/views/system/role/index'),
  sys_menu: () => import('@/views/system/menu/index')
 }

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return false
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    tmp.component = asyncRouteMap[tmp.component]
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  generateRoutes({ commit }, routeParam) {
    return new Promise(resolve => {
      const accessedRoutes = filterAsyncRoutes(routeParam.asyncRoutes, routeParam.roles)
      commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
