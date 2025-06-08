<template>
  <div class="app-container">

    <div>
      <el-row :gutter="20" class="toolbar">
        <el-input v-model="searchKeyword" placeholder="站点名关键字" class="mr-10 input-width" />
        <el-date-picker
          v-model="startDate"
          type="date"
          placeholder="开始时间"
          format="yyyy-MM-DD"
          class="mr-10"
        />
        <el-date-picker
          v-model="endDate"
          type="date"
          placeholder="结束时间"
          format="yyyy-MM-DD"
          class="mr-10"
        />
        <el-button type="primary" @click="handleSearch">查询</el-button>
      </el-row>
    </div>
    <div>
      <FilenameOption v-model="filename" title="请输入文件名" placeholder="请输入导出的文件名" />
      <AutoWidthOption v-model="autoWidth" />
      <BookTypeOption v-model="bookType" />
      <el-button :loading="downloadLoading" style="margin:0 0 20px 20px;" type="primary" icon="el-icon-document" @click="handleDownload">
        导出文件
      </el-button>
    </div>

    <el-table v-loading="listLoading" :data="list" element-loading-text="Loading..." border fit highlight-current-row>
      <el-table-column align="center" label="站点编号" width="95">
        <template slot-scope="scope">
          {{ scope.row.station_id }}
        </template>
      </el-table-column>
      <el-table-column label="省份">
        <template slot-scope="scope">
          <el-tag>{{ scope.row.province }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="站点名称">
        <template slot-scope="scope">
          <el-tag>{{ scope.row.station_name }}</el-tag>
        </template>
      </el-table-column>
      <!-- <el-table-column label="发射站名称">
        <template slot-scope="scope">
          <el-tag>{{ scope.row.transmitting_station_name }}</el-tag>
        </template>
      </el-table-column> -->
      <el-table-column label="时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.time | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="时延">
        <template slot-scope="scope">
          <el-tag>{{ scope.row.differential_delay }}</el-tag>
        </template>
      </el-table-column>
      <!--       <el-table-column label="发射站编号">
        <template slot-scope="scope">
          {{ scope.row.transmitting_station_id }}
        </template>
      </el-table-column> -->
      <!--       <el-table-column label="接收站编号">
        <template slot-scope="scope">
          {{ scope.row.receiver_id }}
        </template>
      </el-table-column> -->
      <el-table-column label="频道编号">
        <template slot-scope="scope">
          {{ scope.row.channel_id }}
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { fetchDelayList } from '@/api/delay'
import { parseTime } from '@/utils'
// options components
import FilenameOption from './components/FilenameOption'
import AutoWidthOption from './components/AutoWidthOption'
import BookTypeOption from './components/BookTypeOption'
import { Message } from 'element-ui'

export default {
  name: 'ExportExcel',
  components: { FilenameOption, AutoWidthOption, BookTypeOption },
  data() {
    return {
      searchKeyword: '',
      startDate: '',
      endDate: '',
      list: null,
      listLoading: true,
      downloadLoading: false,
      filename: '',
      autoWidth: true,
      bookType: 'xlsx'
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    handleSearch() {
      // 在这里添加查询逻辑
      Message.success('查询按钮点击')
    },
    fetchData() {
      this.listLoading = true
      fetchDelayList().then(response => {
        this.list = response.data.items
        this.listLoading = false
      })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['站点编号', '发射站名称', '频道编号', '时间', '时延', '站点名称', '省份']
        const filterVal = ['station_id', 'transmitting_station_name', 'channel_id', 'time', 'differential_delay', 'station_name', 'province']
        // const tHeader = ['站点编号', '发射站编号', '发射站名称', '接收站编号', '频道编号', '时间', '时延', '站点名称', '省份']
        // const filterVal = ['station_id', 'transmitting_station_id','transmitting_station_name', 'receiver_id', 'channel_id', 'time', 'differential_delay', 'station_name', 'province']
        const list = this.list
        const data = this.formatJson(filterVal, list)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: this.filename,
          autoWidth: this.autoWidth,
          bookType: this.bookType
        })
        this.downloadLoading = false
      })
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'time') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    }
  }
}
</script>

<style>
.radio-label {
  font-size: 14px;
  color: #606266;
  line-height: 40px;
  padding: 0 12px 0 30px;
}
.toolbar {
  margin-bottom: 20px;
  margin-right: 0 !important;
  margin-left: 0 !important;
}
.mr-10 {
  margin-right: 10px;
}
.input-width {
  width: 200px;
}
</style>
