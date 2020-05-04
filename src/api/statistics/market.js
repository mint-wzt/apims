import request from '@/utils/request'

export function getMarkets(params) {
  return request({
    url: 'api/market',
    method: 'get',
    params
  })
}
