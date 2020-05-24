import request from '@/utils/request'

export function getInfo() {
  return request({
    url: 'api/system',
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: 'api/system',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: 'api/system',
    method: 'put',
    data
  })
}

export default { add, edit }

