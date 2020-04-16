import request from '@/utils/request'

export function getAllProducts(deptId) {
  const params = {
    deptId,
    page: 0,
    size: 9999
  }
  return request({
    url: 'api/product',
    method: 'get',
    params
  })
}

export function add(data) {
  return request({
    url: 'api/product',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/product',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/product',
    method: 'put',
    data
  })
}

export default { add, edit, del }

