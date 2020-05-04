import request from '@/utils/request'

export function getProductSalesStatistics(params) {
  return request({
    url: 'api/sales-statistics',
    method: 'get',
    params
  })
}

export function getProductSalesRank(params) {
  return request({
    url: 'api/sales-statistics/sales-rank',
    method: 'get',
    params
  })
}
