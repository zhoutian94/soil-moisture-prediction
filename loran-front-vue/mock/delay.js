const Mock = require('mockjs')

const List = []
const count = 100

const baseContent = '<p>I am testing data, I am testing data.</p><p><img src="https://wpimg.wallstcn.com/4c69009c-0fd4-4153-b112-6cb53d1cf943"></p>'
const image_uri = 'https://wpimg.wallstcn.com/e4558086-631c-425c-9430-56ffb46e70b3'

const now = +Mock.Random.now('T'); // 当前时间戳

for (let i = 0; i < count; i++) {
  const city = Mock.Random.city(true); // 生成城市，包含省份信息

  List.push(Mock.mock({
    station_id: '@increment', // 自增 station_id
    // transmitting_station_id: '@integer(1, 10)', // 随机生成 transmitting_station_id，范围在 1 到 10 之间
    transmitting_station_name: '延安站', // 随机生成 transmitting_station_id，范围在 1 到 10 之间
    // receiver_id: '@integer(1, 10)', // 随机生成 receiver_id，范围在 1 到 10 之间
    channel_id: '@integer(1, 10)', // 随机生成 channel_id，范围在 1 到 10 之间
    time: now - i * 1000, // 生成最近1分钟内的时间戳，时间倒序
    differential_delay: '@float(400, 500, 2, 2)', // 随机生成 differential_delay，范围在 0 到 2000 之间，保留两位小数
    station_name: '延安站', // 提取城市名
    // station_name: city.split(' ')[1], // 提取城市名
    province: '陕西省' // 使用生成的省份
    // province: city.split(' ')[0] // 使用生成的省份
  }))
}

module.exports = [
  {
    url: '/vue-element-admin/delay/list',
    type: 'get',
    response: config => {
      const { importance, type, title, page = 1, limit = 20, sort } = config.query

      let mockList = List.filter(item => {
        if (importance && item.importance !== +importance) return false
        if (type && item.type !== type) return false
        if (title && item.title.indexOf(title) < 0) return false
        return true
      })

      if (sort === '-id') {
        mockList = mockList.reverse()
      }

      const pageList = mockList.filter((item, index) => index < limit * page && index >= limit * (page - 1))

      return {
        code: 20000,
        data: {
          total: mockList.length,
          items: pageList
        }
      }
    }
  }
]

