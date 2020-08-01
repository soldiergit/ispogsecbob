<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('enterprise:teacher:achievement:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('enterprise:teacher:achievement:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
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
        prop="teaAchievementContent"
        header-align="center"
        align="center"
        label="教师所获奖励内容">
      </el-table-column>
      <el-table-column
        prop="inApply"
        header-align="center"
        align="center"
        label="审核状态">
        <template slot-scope="scope">
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
          <el-button v-if="passVisiable(scope.row)" type="text" size="small" @click="applySubmitHandle(scope.row.teaAchievementId,2)">通过</el-button>
          <el-button v-if="noPassVisiable(scope.row)" type="text" size="small" @click="applyNoPassHandle(scope.row.teaAchievementId)">不通过</el-button>
          <!--<el-button v-if="submitVisiable(scope.row)" type="text" size="small" @click="applySubmitHandle(scope.row.teaAchievementId,1)">提交</el-button>-->
          <!--<el-button v-if="updateVisiable(scope.row)" type="text" size="small" @click="addOrUpdateHandle(scope.row.teaAchievementId)">修改</el-button>-->
          <el-button type="text" size="small" @click="detailHandle(scope.row.teaAchievementId)">详情</el-button>
          <!--<el-button v-if="deleteVisiable(scope.row)" type="text" size="small" @click="deleteHandle(scope.row.teaAchievementId)">删除</el-button>-->
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
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    <info-detail v-if="infoDetailVisiable" ref="infoDetail" @refreshDataList="getDataList"></info-detail>
    <retreat v-if="retreatlVisiable" ref="retreat" @refreshDataList="getDataList"></retreat>

  </div>
</template>

<script>
  import AddOrUpdate from './teacher-achievement-info-add-or-update'
  import infoDetail from './ac-info-detail'
  import Retreat from './ac-retreat-add-or-update'

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
        infoDetailVisiable: false,
        retreatlVisiable: false,
        dataListSelections: [],
        addOrUpdateVisible: false
      }
    },
    components: {
      AddOrUpdate,
      infoDetail,
      Retreat
    },
    activated () {
      this.getDataList()
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/enterprise/teacher/achievement/list'),
          method: 'get',
          params: this.$http.adornParams({
            'hasApply': this.hasApply,
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.dataForm.key
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
      passVisiable (item) {
        if (item.inApply === 1) { // 已提交
          let roleIdList = this.$store.state.user.roleIdList
          for (let roleIndex = 0; roleIndex < roleIdList.length; roleIndex++) {
            if (roleIdList[roleIndex] === 5 || roleIdList[roleIndex] === 1) { // 管理员
              return true
            }
          }
        }
        return false
      },
      noPassVisiable (item) {
        if (item.inApply === 1) { // 已提交
          let roleIdList = this.$store.state.user.roleIdList
          for (let roleIndex = 0; roleIndex < roleIdList.length; roleIndex++) {
            if (roleIdList[roleIndex] === 5 || roleIdList[roleIndex] === 1) { // 管理员
              return true
            }
          }
        }
        return false
      },
      submitVisiable (item) {
        if (item.inApply === 0) {
          return true
        }
        return false
      },
      updateVisiable (item) {
        if (item.inApply === 0 || item.inApply === 3) {
          return true
        }
        return false
      },
      deleteVisiable (item) {
        if (item.inApply === 0) {
          return true
        }
        return false
      },
      // 通过
      applySubmitHandle (id, status) {
        this.$confirm('此操作将使该项目进入下一个审批流程，是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/enterprise/teacher/achievement/apply'),
            method: 'post',
            params: this.$http.adornParams({
              'teaAchievementId': id,
              'status': status
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
      applyNoPassHandle (id) {
        this.retreatlVisiable = true
        this.$nextTick(() => {
          this.$refs.retreat.init(id)
        })
      },
      applyPassHandle (id) {

      },
      // 详情
      detailHandle (id) {
        this.infoDetailVisiable = true
        this.$nextTick(() => {
          this.$refs.infoDetail.init(id)
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
        })
      },
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.teaAchievementId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/enterprise/teacher/achievement/delete'),
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
