import request from '@/utils/request'

export function getProductStatistics(params) {
  return request({
    url: 'api/product-statistics/info',
    method: 'get',
    params
  })
}

export function getProductsByCatagory(params) {
  return request({
    url: 'api/product-statistics/products',
    method: 'get',
    params
  })
}
