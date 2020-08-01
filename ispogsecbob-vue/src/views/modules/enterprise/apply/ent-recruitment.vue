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
        <el-input v-model="dataForm.recruitmentPost" placeholder="招聘岗位" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>

      </el-form-item>
    </el-form>
    <el-card>
      <el-radio-group v-model="hasApply" @change="getDataList">
        <el-radio label="0">未审批</el-radio>
        <el-radio label="1">已审批</el-radio>
      </el-radio-group>
    </el-card>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      :default-sort="{prop: 'recruitmentInfoId', order: 'ascending'}"
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
        prop="entEnterpriseInfo.entName"
        header-align="center"
        align="center"
        label="企业名称">
      </el-table-column>
      <el-table-column
        sortable
        prop="recruitmentPost"
        header-align="center"
        align="center"
        label="招聘岗位">
      </el-table-column>
      <el-table-column
        sortable
        prop="recruitmentPeopleNumber"
        header-align="center"
        align="center"
        width="180"
        label="招聘人数">
      </el-table-column>
      <el-table-column
        sortable
        prop="recruitmentSpecialty"
        header-align="center"
        align="center"
        label="招聘专业">
      </el-table-column>
      <el-table-column
        sortable
        prop="entEnterpriseInfo.newHighZones"
        header-align="center"
        align="center"
        width="180"
        :formatter="formatterZone"
        label="是否高新区">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="210"
        label="操作">
        <template slot-scope="scope">
          <!-- isAuth('enterprise:info:shenhe') -->
          <el-button v-if="true" type="text" size="small" @click="detailHandle(scope.row.recruitmentInfoId)">详情</el-button>
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
    <ent-recuitment-details v-if="shenhe" ref="details" @refreshDataList="getDetailsInfo()"/>
    <!-- 通过按钮 -->
    <el-dialog
      title="招聘审核提示"
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
      title="招聘企业审核提示"
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
import EntRecuitmentDetails from '../recruitment/recruitment-details'

export default {
  data () {
    return {
      tempRec: null, // 招聘临时信息
      consenttip: '您确认要通过该审核吗？该操作不可撤回！',
      retreattip: '您确认不通过该审核吗？该操作不可撤回！',
      shenhe: false,
      consentVisible: false,
      retreatVisible: false,
      hasApply: '0',
      dataForm: {
        recruitmentInfoId: '',
        recruitmentPost: '',
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
    EntRecuitmentDetails
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
      console.log(id)
      this.shenhe = true
      this.$nextTick(() => {
        this.$refs.details.init(id)
      })
    },
        // 通过
    consentHandle (item) {
      this.consentVisible = true
      this.tempRec = item
    },
        // 不通过
    retreatHandle (item) {
      this.retreatVisible = true
      this.tempRec = item
    },
        // 获取数据列表
    getDataList () {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/enterprise/recruitment/info/list'),
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
        // 审批
    applyMatchHandle (id) {
      this.$nextTick(() => {
                // this.$refs.award.init(id)
      })
    },
        // 审批通过
    applyConsentHandle () {
      this.$http({
        url: this.$http.adornUrl('/enterprise/recruitment/info/recExamine'),
        method: 'post',
        params: this.$http.adornParams({
          'recruitmentInfoId': this.tempRec.recruitmentInfoId,
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
    applyRetreatHandle: function () {
      this.$http({
        url: this.$http.adornUrl('/enterprise/recruitment/info/recExamine'),
        method: 'post',
        params: this.$http.adornParams({
          'recruitmentInfoId': this.tempRec.recruitmentInfoId,
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
    isDelete: function (item) {
      if ((item.projectMatchApplyStatus === 0) &&
          this.isAuth('innovate:match:delete')) {
        return true
      }
    },
      // 删除
    deleteHandle: function (id) {
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
