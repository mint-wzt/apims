import request from '@/utils/request'

export function getLatestPrice(params) {
  return request({
    url: 'api/price-statistics',
    method: 'get',
    params
  })
}
