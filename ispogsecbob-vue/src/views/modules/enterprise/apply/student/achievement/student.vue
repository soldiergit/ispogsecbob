<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList">查询</el-button>
        <el-button v-if="isAuth('enterprise:student:achievement:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('enterprise:student:achievement:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
    </el-form>
    <!--<el-card>-->
      <!--<el-radio-group v-model="hasApply" @change="getDataList()">-->
        <!--<el-radio label="1">未审批</el-radio>-->
        <!--<el-radio label="2">已审批</el-radio>-->
      <!--</el-radio-group>-->
    <!--</el-card>-->
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
        prop="achievementTime"
        header-align="center"
        align="center"
        :formatter="dateFormat"
        label="年度">
      </el-table-column>
      <el-table-column
        prop="stuAchievementContent"
        header-align="center"
        align="center"
        label="所获奖励/证书内容">
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
          <el-button v-if="submitVisible(scope.row)" type="text" size="small" @click="applySubmitHandle(scope.row.stuAchievementId,1)">提交</el-button>
          <el-button v-if="updateVisible(scope.row)" type="text" size="small" @click="addOrUpdateHandle(scope.row.stuAchievementId)">修改</el-button>
          <el-button type="text" size="small" @click="detailHandle(scope.row.stuAchievementId)">详情</el-button>
          <el-button v-if="deleteVisible(scope.row)" type="text" size="small" @click="deleteHandle(scope.row.stuAchievementId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    <detail v-if="detailVisible" ref="detail" @refreshDataList="getDataList"></detail>
  </div>
</template>

<script>
  import AddOrUpdate from './info-add-or-update'
  import Detail from './info-detail'
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
        addOrUpdateVisible: false
      }
    },
    components: {
      AddOrUpdate,
      Detail
    },
    activated () {
      this.getDataList()
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.addOrUpdateVisible = false
        this.detailVisible = false
        this.$http({
          url: this.$http.adornUrl('/enterprise/student/achievement/list'),
          method: 'get',
          params: this.$http.adornParams({
            'userId': this.$store.state.user.id,
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
      passVisible (item) {
        if (item.inApply === 1) { // 已提交
          let roleIdList = this.$store.state.user.roleIdList
          for (let roleIndex = 0; roleIndex < roleIdList.length; roleIndex++) {
            if (roleIdList[roleIndex] === 5) { // 管理员
              return true
            }
          }
        }
        return false
      },
      noPassVisible (item) {
        if (item.inApply === 1) { // 已提交
          let roleIdList = this.$store.state.user.roleIdList
          for (let roleIndex = 0; roleIndex < roleIdList.length; roleIndex++) {
            if (roleIdList[roleIndex] === 5) { // 管理员
              return true
            }
          }
        }
        return false
      },
      submitVisible (item) {
        if (item.inApply === 0) {
          return true
        }
        return false
      },
      updateVisible (item) {
        if (item.inApply === 0 || item.inApply === 3) {
          return true
        }
        return false
      },
      deleteVisible (item) {
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
            url: this.$http.adornUrl('/enterprise/student/achievement/apply'),
            method: 'post',
            params: this.$http.adornParams({
              'stuAchievementId': id,
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
      // 详情
      detailHandle (id) {
        this.detailVisible = true
        this.$nextTick(() => {
          this.$refs.detail.init(id)
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
          return item.stuAchievementId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/enterprise/student/achievement/delete'),
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
      // 时间格式化
      dateFormat (row, column) {
        var t = new Date(row.achievementTime)
        return t.getFullYear() + '年'
      }
    }
  }
</script>

<style scoped>

</style>
