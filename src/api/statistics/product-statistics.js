import request from '@/utils/request'

export function getProductStatistics(params) {
  return request({
    url: 'api/product-statistics',
    method: 'get',
    params
  })
}
