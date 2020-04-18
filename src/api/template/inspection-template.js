import request from '@/utils/request'

export function getAllTemplates(params) {
  return request({
    url: 'api/inspection-template',
    method: 'get',
    params
  })
}

export function add(data) {
  return request({
    url: 'api/inspection-template',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/inspection-template',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/inspection-template',
    method: 'put',
    data
  })
}

export default { add, edit, del }

