<template>
  <div class="mod-user">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="项目名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
      </el-form-item>
    </el-form>
    <el-card>
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
        fixed="right"
        header-align="center"
        align="center"
        width="210"
        label="操作">
        <template slot-scope="scope">
          <!-- isAuth('enterprise:info:shenhe') -->
          <el-button v-if="true" type="text" size="small" @click="detailHandle(scope.row.proInfoId)">详情</el-button>
          <el-button v-if="true" type="text" size="small" @click="consentHandle(scope.row)">通过</el-button>
          <el-button v-if="true" type="text" size="small" @click="retreatHandle(scope.row)">不通过</el-button>
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

    <!-- 通过按钮 -->
    <el-dialog
      title="审核提示"
      :visible.sync="consentVisible"
      width="30%"
      :before-close="handleClose">
      <span>{{consenttip}}</span>
      <span slot="footer" class="dialog-footer">
          <el-button @click="consentVisible = false">取 消</el-button>
          <el-button type="primary" @click="applyConsentHandle()">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 不通过按钮 -->
    <el-dialog
      title="审核提示"
      :visible.sync="retreatVisible"
      width="30%"
      :before-close="handleClose">
      <span>{{retreattip}}</span>
      <span slot="footer" class="dialog-footer">
          <el-button @click="retreatVisible = false">取 消</el-button>
          <el-button type="primary" @click="applyRetreatHandle()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import ProjectDetails from '../project/project-details'
export default {
  data () {
    return {
      tempPro: null, // 项目临时信息
      consenttip: '您确认要通过该审核吗？该操作不可撤回！',
      retreattip: '您确认不通过该审核吗？该操作不可撤回！',
      shenhe: false,
      consentVisible: false,
      retreatVisible: false,
      hasApply: '0',
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
    ProjectDetails
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
      console.log(this.hasType, id)
      this.shenhe = true
      this.$nextTick(() => {
        this.$refs.details.init(this.hasType, id)
      })
    },
        // 通过
    consentHandle (item) {
      this.consentVisible = true
      this.tempPro = item
            // this.$nextTick(() => {
            //     this.$refs.retreat.init(item.declareId, 'project_audit_apply_status', item.projectAuditApplyStatus)
            // })
    },
        // 不通过
    retreatHandle (item) {
      this.retreatVisible = true
      this.tempPro = item
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
        url: this.$http.adornUrl('/enterprise/project/info/list'),
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
        url: this.$http.adornUrl('/enterprise/project/info/entExamine'),
        method: 'post',
        params: this.$http.adornParams({
          'proInfoId': this.tempPro.proInfoId,
          'inApply': '1'
        }, false)
      }).then(({data}) => {
        this.$message({
          type: 'success',
          message: '提交成功!'
        })
        this.consentVisible = false
        this.getDataList()
      })
    },
        // 审批不通过
    applyRetreatHandle () {
      this.$http({
        url: this.$http.adornUrl('/enterprise/project/info/entExamine'),
        method: 'post',
        params: this.$http.adornParams({
          'proInfoId': this.tempPro.proInfoId,
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
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.proInfoId
      })
      this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/enterprise/project/info/delete'),
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
<style>
  .el-step__title {
    font-size: 12px;
    line-height: 28px;
  }

  .el-table__expanded-cell[class*=cell] {
    padding: 5px 5px;
  }
</style>
