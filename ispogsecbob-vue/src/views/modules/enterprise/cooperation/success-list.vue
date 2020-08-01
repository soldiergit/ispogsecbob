<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button type="primary" v-if="isAuth('enterprise:project:cooperation:save')" @click="addOrUpdateHandle(null, proInfoId)">新增</el-button>
        <el-button type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
    </el-form>
    <el-card v-if="!isAuth('enterprise:project:info:save')">
      <el-radio-group v-model="hasType" @change="getDataList">
        <el-radio label="userPerId">学生</el-radio>
        <el-radio label="userTeacherId">教师</el-radio>
        <el-radio label="entInfoId">企业</el-radio>
      </el-radio-group>
    </el-card>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
      <el-table-column
        sortable
        prop="projectInfo.proName"
        header-align="center"
        align="center"
        label="项目名称">
      </el-table-column>
      <el-table-column
        prop="cooperationContent"
        header-align="center"
        align="center"
        label="合作内容">
      </el-table-column>
      <el-table-column
        prop="cooperationType"
        header-align="center"
        align="center"
        label="合作方式">
      </el-table-column>
      <el-table-column
        prop="cooperationRequire"
        header-align="center"
        align="center"
        label="合作要求">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <!-- isAuth('enterprise:info:shenhe') -->
          <el-button v-if="true" type="text" size="small" @click="detailHandle(scope.row.proCooperationInfoId)">详情</el-button>
          <el-button v-if="true" type="text" size="small" @click="successHandle(scope.row.proCooperationInfoId)">合作达成列表</el-button>
          <el-button v-else type="text" size="small">无操作</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <cooperation-details v-if="shenhe" ref="details" @refreshDataList="getDetailsInfo()"/>
    <relation-details v-if="shenhe" ref="relationDetails" @refreshDataList="getDetailsInfo()"/>
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
import CooperationDetails from '../cooperation/cooperation-details'
import RelationDetails from '../relation/relation-details'
import AddOrUpdate from './cooperation-add-or-update'
export default {
  data () {
    return {
      dataForm: {
        key: ''
      },
      dataList: [],
      proInfoId: '',
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      shenhe: false,
      hasType: 'userPerId',
      hasApply: '3'
    }
  },
  components: {
    CooperationDetails,
    AddOrUpdate,
    RelationDetails
  },
  activated () {
    this.getDataList()
  },
  methods: {
        // 获取数据列表
    getDataList () {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/enterprise/project/cooperation/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'key': this.dataForm.key,
          'inApply': this.hasApply,
          'inType': this.hasType
        })
      }).then(({data}) => {
        console.log(data)
        if (data && data.code === 0) {
          this.dataList = data.page.list
          this.totalPage = data.page.totalCount
        } else {
          this.dataList = []
          this.totalPage = 0
        }
        this.dataListLoading = false
      })
    },
        // 每页数
    sizeChangeHandle (val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList()
    },
        // 当前页
    currentChangeHandle (val) {
      this.pageIndex = val
      this.getDataList()
    },
        // 多选
    selectionChangeHandle (val) {
      this.dataListSelections = val
    },
        // 新增 / 修改
    addOrUpdateHandle (id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
        this.$refs.addOrUpdate.selectProject()
      })
    },
        // 详情
    detailHandle: function (id) {
      this.shenhe = true
      this.$nextTick(() => {
        this.$refs.details.init(id, this.hasType)
      })
    },
    // 合作达成列表
    successHandle (id) {
      this.shenhe = true
      this.$nextTick(() => {
        this.$refs.relationDetails.init(id, this.hasType, '1')
      })
    },
        // 删除
    deleteHandle (id) {
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.proCooperationInfoId
      })
      this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/enterprise/project/cooperation/delete'),
          method: 'post',
          data: this.$http.adornData(ids, false)
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.getDataList()
              }
            })
          } else {
            this.$message.error(data.msg)
          }
        })
      })
    }
  }
}
</script>
