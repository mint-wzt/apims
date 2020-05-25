import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/sales-release',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/sales-release',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/sales-release',
    method: 'put',
    data
  })
}

export function addViews(id) {
  return request({
    url: 'api/sales-release/views',
    method: 'get',
    params: id
  })
}

export default { add, edit, del }
