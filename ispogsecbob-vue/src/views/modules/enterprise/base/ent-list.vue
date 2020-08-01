<template>
  <div class="mod-user">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-date-picker
          v-model="dataForm.matchTime"
          align="right"
          type="year"
          placeholder="请选择年度">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-input v-model="dataForm.projectName" placeholder="项目名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      :default-sort="{prop: 'entInfoId', order: 'ascending'}"
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
        prop="entName"
        header-align="center"
        align="center"
        label="企业名称">
      </el-table-column>
      <el-table-column
        sortable
        prop="entCorporate"
        header-align="center"
        align="center"
        width="110"
        label="法人代表">
      </el-table-column>
      <el-table-column
        sortable
        prop="entCode"
        header-align="center"
        align="center"
        label="统一信用代码">
      </el-table-column>
      <el-table-column
        sortable
        prop="entType"
        header-align="center"
        align="center"
        label="企业类型">
      </el-table-column>
      <el-table-column
        sortable
        prop="newHighZones"
        header-align="center"
        align="center"
        width="120"
        :formatter="formatterZone"
        label="是否高新区">
      </el-table-column>
      <el-table-column
        sortable
        prop="entInTime"
        header-align="center"
        align="center"
        label="申请入驻时间">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="210"
        label="操作">
        <template slot-scope="scope">
          <!-- isAuth('enterprise:info:shenhe') -->
          <el-button v-if="true" type="text" size="small" @click="detailHandle(scope.row.entInfoId, scope.row.inApply)">详情</el-button>
          <el-button v-if="true" type="text" size="small"  @click="deleteHandle(scope.row.entInfoId)">删除</el-button>
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
    <ent-details v-if="shenhe" ref="details" @refreshDataList="getDetailsInfo()"/>

  </div>
</template>

<script>
import EntDetails from '../base/ent-details'

export default {
  data: function () {
    return {
      tempEnt: null, // 企业临时信息
      shenhe: false,
      consentVisible: false,
      retreatVisible: false,
      hasApply: '1',
      dataForm: {
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
    EntDetails
  },
  activated () {
    this.getDataList()
  },
  methods: {
        // 格式化区域显示
    formatterZone: function (row, column, cellValue) {
      return cellValue === '0' ? '是' : '否'
    },
        // 详情
    detailHandle: function (id, hasApply) {
      this.shenhe = true
      this.$nextTick(() => {
        this.$refs.details.init(id, hasApply)
      })
    },
        // 通过
    consentHandle (item) {
      this.consentVisible = true
      this.tempEnt = item
            // this.$nextTick(() => {
            //     this.$refs.retreat.init(item.declareId, 'project_audit_apply_status', item.projectAuditApplyStatus)
            // })
    },
        // 不通过
    retreatHandle (item) {
      this.retreatVisible = true
      this.tempEnt = item
            // this.$nextTick(() => {
            //     this.$refs.retreat.init(item.declareId, 'project_audit_apply_status', item.projectAuditApplyStatus)
            // })
    },
        // 获取详情信息
    getDetailsInfo () {

    },
        // 获取数据列表
    getDataList () {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/enterprise/info/list'),
        method: 'get',
        params: this.$http.adornParams({
          'currPage': this.pageIndex,
          'pageSize': this.pageSize,
          'inApply': this.hasApply
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
        // 审批通过
    applyConsentHandle () {
      this.$http({
        url: this.$http.adornUrl('/enterprise/info/entExamine'),
        method: 'post',
        params: this.$http.adornParams({
          'userId': this.tempEnt.userId,
          'entInfoId': this.tempEnt.entInfoId,
          'inApply': '1'
        }, false)
      }).then(({data}) => {
        this.$message({
          type: 'success',
          message: '提交成功!'
        })
        this.retreatVisible = false
        this.getDataList()
      })
    },
        // 审批不通过
    applyRetreatHandle () {
      this.$http({
        url: this.$http.adornUrl('/enterprise/info/entExamine'),
        method: 'post',
        params: this.$http.adornParams({
          'userId': this.tempEnt.userId,
          'entInfoId': this.tempEnt.entInfoId,
          'inApply': '0'
        }, false)
      }).then(({data}) => {
        this.$message({
          type: 'success',
          message: '提交成功!'
        })
        this.retreatVisible = false
        this.getDataList()
      })
    },
    isDelete (item) {
      if ((item.projectMatchApplyStatus === 0) &&
                this.isAuth('innovate:match:delete')) {
        return true
      }
    },
        // 删除
    deleteHandle (id) {
      var canDelete = true
      var matchIds = id ? [id] : this.dataListSelections.map(item => {
        if ((item.projectInfoEntity.projectMatchApplyStatus > 0) ||
                    !this.isAuth('innovate:match:delete')) {
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
            url: this.$http.adornUrl('/innovate/match/info/delete'),
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
