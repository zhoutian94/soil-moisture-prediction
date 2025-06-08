import request from '@/utils/request'

export function fetchDelayList(query) {
  return request({
    url: '/vue-element-admin/delay/list',
    method: 'get',
    params: query
  })
}
