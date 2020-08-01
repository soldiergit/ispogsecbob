<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('enterprise:teacher:experience:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('enterprise:teacher:experience:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
    </el-form>
    <el-card>
      <el-radio-group v-model="hasApply" @change="getDataList">
        <el-radio label="1">未审批</el-radio>
        <el-radio label="2">已审批</el-radio>
        <el-radio label="3">未通过</el-radio>
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
        hidden
        header-align="center"
        align="center"
        width="150"
        label="序号">
        <template slot-scope="props">
          <p v-text="props.$index+1"></p>
        </template>
      </el-table-column>
      <el-table-column
        prop="teaExperienceContent"
        header-align="center"
        align="center"
        label="科研内容">
      </el-table-column>
      <el-table-column
        prop="inApply"
        header-align="center"
        align="center"
        label="审核状态"><template slot-scope="scope">
        <el-tag v-if="scope.row.inApply === 0" size="small">未提交</el-tag>
        <el-tag v-if="scope.row.inApply === 1" size="small">待审核</el-tag>
        <el-tag v-if="scope.row.inApply === 2" size="small">已通过</el-tag>
        <el-tag v-if="scope.row.inApply === 3" size="small">未通过</el-tag>
      </template>

      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="submitVisiableHandle(scope.row)" type="text" size="small" @click="submitHandle(scope.row.teaExperienceId)">通过</el-button>
          <el-button v-if="reteratVisiableHandle(scope.row)" type="text" size="small" @click="reteratHandle(scope.row.teaExperienceId)">不通过</el-button>
          <el-button type="text" size="small" @click="detailHandle(scope.row.teaExperienceId)">详情</el-button>
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
    <Detail v-if="detailVisible" ref="detail" @refreshDataList="getDataList"></Detail>
    <reterat v-if="reteratVisible" ref="reterat" @refreshDataList="getDataList"></reterat>
  </div>
</template>

<script>
  import AddOrUpdate from './teacher-experienceinfo-add-or-update'
  import Detail from './ex-info-detail'
  import Reterat from './ex-retreat-add-or-update'

  export default {
    data () {
      return {
        hasApply: '1',
        dataForm: {
          key: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        detailVisible: false,
        reteratVisible: false
      }
    },
    components: {
      AddOrUpdate,
      Detail,
      Reterat
    },
    activated () {
      this.getDataList()
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/enterprise/teacher/experience/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.dataForm.key,
            'hasApply': this.hasApply
          })
        }).then(({data}) => {
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
      reteratHandle (id) {
        this.reteratVisible = true
        this.$nextTick(() => {
          this.$refs.reterat.init(id)
        })
      },
      // 提交
      submitHandle (id) {
        this.$confirm('此操作将使该项目进入下一个审批流程，是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/enterprise/teacher/experience/apply'),
            method: 'post',
            params: this.$http.adornParams({
              'teaExperienceId': id,
              'status': 2
            }, false)
          }).then(({data}) => {
            this.$message({
              type: 'success',
              message: '提交成功!'
            })
            this.getDataList()
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消提交'
          })
        })
      },
      // 详情
      detailHandle (id) {
        this.detailVisible = true
        this.$nextTick(() => {
          this.$refs.detail.init(id)
        })
      },
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.teaExperienceId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/enterprise/teacher/experience/delete'),
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
      },
      //
      submitVisiableHandle (item) {
        if (item.inApply === 1) {
          return true
        }
        return false
      },
      reteratVisiableHandle (item) {
        if (item.inApply === 1) {
          return true
        }
        return false
      },
      deleteVisiableHandle (item) {
        if (item.inApply === 0) {
          return true
        }
        return false
      }
    }
  }
</script>
