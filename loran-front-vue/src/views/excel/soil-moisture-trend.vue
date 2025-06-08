<template>
  <div class="app-container">
    <el-row :gutter="20" class="toolbar">
      <el-button type="primary" @click="search">搜索</el-button>
      <el-button class="mr-10" type="success" @click="predict">预测</el-button>
      <el-select v-model="selectedStation" class="mr-10" placeholder="选择站点">
        <el-option label="延安站" value="yanan" />
        <!-- 可以添加更多站点选项 -->
      </el-select>
      <el-date-picker
        v-model="startDate"
        type="date"
        placeholder="开始时间"
        format="yyyy-MM-DD"
      />
      <el-date-picker
        v-model="endDate"
        type="date"
        placeholder="结束时间"
        format="yyyy-MM-DD"
      />
    </el-row>
    <el-row>
      <div ref="chart" class="chart" />
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { Message } from 'element-ui'

export default {
  name: 'SoilMoistureTrend',
  data() {
    return {
      selectedStation: '',
      startDate: '',
      endDate: '',
      chart: null
    }
  },
  mounted() {
    this.initChart()
    this.selectedStation = 'yanan'
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    search() {
      Message.success('搜索按钮点击')
      // 在这里添加搜索逻辑
    },
    predict() {
      Message.success('预测按钮点击')
      // 在这里添加预测逻辑
    },
    initChart() {
      this.chart = echarts.init(this.$refs.chart)
      const option = {
        tooltip: {
          trigger: 'axis', // 触发类型，'axis' 表示坐标轴触发
          axisPointer: {
            type: 'line' // 指示器类型，'line' 表示直线指示器
          }
        },
        legend: {
          data: ['历史', '预测'], // 图例项
          textStyle: {
            fontSize: 14, // 图例字体大小
            color: '#333' // 图例字体颜色
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: [
            '2024-09-01', '2024-09-02', '2024-09-03', '2024-09-04', '2024-09-05',
            '2024-09-06', '2024-09-07', '2024-09-08', '2024-09-09', '2024-09-10',
            '2024-09-11', '2024-09-12', '2024-09-13', '2024-09-14', '2024-09-15',
            '2024-09-16', '2024-09-17', '2024-09-18', '2024-09-19'
          ]
        },
        yAxis: {
          type: 'value',
          name: '土壤含水量（m3/m3）',
          nameTextStyle: {
            fontSize: 14,
            fontWeight: 'bold',
            color: '#333'
          },
          nameGap: 20
        },
        series: [
          {
            name: '历史', // 图例项名称
            type: 'line',
            data: [
              0.2587, 0.2599, 0.2566, 0.2537, 0.2504,
              0.2537, 0.2574, 0.2577, 0.2718, 0.2697,
              0.2661, 0.2648, null, null, null,
              null, null, null, null
            ],
            areaStyle: { color: 'rgba(0,51,102,0.3)' }, // 深蓝色覆盖区域
            lineStyle: { color: '#003366' } // 深蓝色
          },
          {
            name: '预测', // 图例项名称
            type: 'line',
            data: [
              null, null, null, null, null,
              null, null, null, null, null,
              null, 0.2648, 0.2648, 0.2633, 0.2606,
              0.2589, 0.2564, 0.2536, 0.2501, 0.2491
            ], // 填充最后两个点
            areaStyle: { color: 'rgba(0,100,0,0.3)' }, // 深绿色覆盖区域
            lineStyle: { color: '#004d00' } // 深绿色
          }
        ]
      }
      this.chart.setOption(option)
    },
    handleResize() {
      if (this.chart) {
        this.chart.resize()
      }
    }
  }
}
</script>

<style>
.app-container {
  padding: 20px;
}

.toolbar {
  margin-bottom: 20px;
}

.chart {
  width: 100%;
  height: 400px;
}
.mr-10 {
  margin-right: 10px;
}
</style>
