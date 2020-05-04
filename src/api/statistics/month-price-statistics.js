import request from '@/utils/request'

export function getPriceAnalysisOneToOne(params) {
  return request({
    url: 'api/month-price/one-market-product',
    method: 'get',
    params
  })
}

export function getMonthPriceOneToMoreMarket(params) {
  return request({
    url: 'api/month-price/more-market-product',
    method: 'get',
    params
  })
}
