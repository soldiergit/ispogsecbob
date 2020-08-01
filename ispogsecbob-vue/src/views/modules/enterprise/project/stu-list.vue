<template>
  <div class="mod-user">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="项目名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button type="primary" v-if="isAuth('enterprise:project:info:save')" @click="addOrUpdateHandle()">新增项目</el-button>
      </el-form-item>
    </el-form>
    <el-card>
      <el-radio-group v-model="hasApply" @change="getDataList">
        <el-radio label="1">已通过</el-radio>
        <el-radio label="0">未通过</el-radio>
      </el-radio-group>
    </el-card>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      :default-sort="{prop: 'proInfoId', order: 'ascending'}"
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
        prop="proName"
        header-align="center"
        align="center"
        label="项目名称">
      </el-table-column>
      <el-table-column
        sortable
        prop="proRegister"
        header-align="center"
        align="center"
        width="110"
        label="项目登记">
      </el-table-column>
      <el-table-column
        sortable
        prop="proOrigin"
        header-align="center"
        align="center"
        label="项目来源">
      </el-table-column>
      <el-table-column
        sortable
        prop="proOutlay"
        header-align="center"
        align="center"
        label="项目经费">
      </el-table-column>
      <el-table-column
        sortable
        prop="proType"
        header-align="center"
        align="center"
        width="120"
        :formatter="formatterZone"
        label="项目类型">
      </el-table-column>
      <el-table-column
        sortable
        prop="proIntroduce"
        header-align="center"
        align="center"
        label="项目介绍">
      </el-table-column>
      <el-table-column
        sortable
        prop="inApply"
        header-align="center"
        align="center"
        label="状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.inApply === '0'" size="small">审核中</el-tag>
          <el-tag v-if="scope.row.inApply === '1'" size="small">已审核</el-tag>
          <el-tag v-if="scope.row.inApply === '2'" size="small">已提交</el-tag>
          <el-tag v-if="scope.row.inApply === '3'" size="small">已提交</el-tag>
          <el-tag v-if="scope.row.inApply === '4'" size="small">已提交</el-tag>
          <el-tag v-if="scope.row.inApply === '5'" size="small">已提交</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="210"
        label="操作">
        <template slot-scope="scope">
          <!-- isAuth('enterprise:info:shenhe') -->
          <el-button v-if="true" type="text" size="small" @click="detailHandle(scope.row.proInfoId)">详情</el-button>
          <el-button v-if="true" type="text" size="small" @click="deleteHandle(scope.row.proInfoId)">删除</el-button>
          <el-button v-else type="text" size="small">无操作</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[5, 10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <project-details v-if="shenhe" ref="details" @refreshDataList="getDetailsInfo()"/>
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
import ProjectDetails from '../project/project-details'
import AddOrUpdate from './project-add-or-update'
export default {
  data () {
    return {
      tempPro: null, // 项目临时信息
      shenhe: false,
      addOrUpdateVisible: false,
      hasApply: '1',
      hasType: 'userPerId',
      dataForm: {
        key: '',
        baseId: '',
        projectName: '',
        matchTime: new Date(),
        idDel: 0
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: []
    }
  },
  components: {
    ProjectDetails,
    AddOrUpdate
  },
  activated () {
    this.getDataList()
  },
  methods: {
        // 格式化区域显示
    formatterZone (row, column, cellValue) {
      return cellValue === '0' ? '是' : '否'
    },
        // 详情
    detailHandle (id) {
      this.shenhe = true
      this.$nextTick(() => {
        this.$refs.details.init(this.hasType, id)
      })
    },
        // 获取详情信息
    getDetailsInfo () {

    },
        // 获取数据列表
    getDataList () {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/enterprise/project/info/queryListByUserId'),
        method: 'get',
        params: this.$http.adornParams({
          'currPage': this.pageIndex,
          'pageSize': this.pageSize,
          'key': this.dataForm.key,
          'inApply': this.hasApply,
          'inType': this.hasType
        })
      }).then(({data}) => {
        console.log(data)
        if (data.code === 500) {
          this.$message.error(data.msg)
        }
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
        //  每页数
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
    isDelete (item) {
      if ((item.projectMatchApplyStatus === 0) &&
                this.isAuth('innovate:match:delete')) {
        return true
      }
    },
      // 新增 / 修改
    addOrUpdateHandle (id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },
    deleteHandle (id) {
      var canDelete = true
      var matchIds = id ? [id] : this.dataListSelections.map(item => {
        if ((item.projectInfoEntity.projectMatchApplyStatus > 0) ||
          !this.isAuth('enterprise:project:info:delete')) {
          canDelete = false
        } else {
          canDelete = false
        }
        return item.projectInfoEntity.matchId
      })
      this.$confirm(`确定要进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (canDelete) {
          this.$http({
            url: this.$http.adornUrl('/enterprise/project/info/delete'),
            method: 'post',
            data: this.$http.adornData(matchIds, false)
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
        } else {
          this.$message({
            message: '包含不可删除项目',
            type: 'error',
            duration: 1500,
            onClose: () => {
              this.getDataList()
            }
          })
        }
      }).catch(() => {
      })
    }
  }
}
</script>
<style>
  .el-step__title {
    font-size: 12px;
    line-height: 28px;
  }

  .el-table__expanded-cell[class*=cell] {
    padding: 5px 5px;
  }
</style>
