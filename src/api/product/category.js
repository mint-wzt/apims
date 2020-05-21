import request from '@/utils/request'

export function getCategoryTree(params) {
  return request({
    url: 'api/category/tree',
    method: 'get',
    params

  })
}

export function getCategories(params) {
  return request({
    url: 'api/category',
    method: 'get',
    params
  })
}

export function add(data) {
  return request({
    url: 'api/category',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/category',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/category',
    method: 'put',
    data
  })
}

export default { add, edit, del, getCategoryTree, getCategories }
