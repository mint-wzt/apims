import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/inspection-item',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/inspection-item',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/inspection-item',
    method: 'put',
    data
  })
}

/**
 * 获取所有的检测项
 */
export function getAllInspections() {
  return request({
    url: 'api/inspection-item/all',
    method: 'get'
  })
}

export default { add, edit, del }
