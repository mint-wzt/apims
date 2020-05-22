import request from '@/utils/request'

export function get(params) {
  return request({
    url: 'api/industry-statistics/info',
    method: 'get',
    params
  })
}
