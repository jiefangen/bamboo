/**
 * Created by PanJiaChen on 16/11/18.
 */

/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} name
 * @returns {Boolean}
 */
export function validUsername(name) {
  const valid_map = ['admin', 'system', 'actuator']
  return valid_map.indexOf(name.trim()) >= 0
}
