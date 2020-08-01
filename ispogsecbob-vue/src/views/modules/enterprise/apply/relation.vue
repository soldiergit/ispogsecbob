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
        prop="projectInfo.proName"
        header-align="center"
        align="center"
        label="项目名称">
      </el-table-column>
      <el-table-column
        v-if="hasType == 'userPerId'"
        prop="userPersonInfo.sysUser.name"
        header-align="center"
        align="center"
        label="项目负责人">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="getStuDetailsInfo(scope.row.userPerId)">{{scope.row.userPersonInfo.sysUserEntity.name}}</el-button>
        </template>
      </el-table-column>
      <el-table-column
        v-if="hasType == 'userTeacherId'"
        prop="userTeacherInfo.sysUserEntity.name"
        header-align="center"
        align="center"
        label="项目负责人">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="getTeaDetailsInfo(scope.row.userTeacherId)">{{scope.row.userTeacherInfo.sysUserEntity.name}}</el-button>
        </template>
      </el-table-column>
      <el-table-column
        v-if="hasType == 'entInfoId'"
        prop="entEnterpriseInfo.entName"
        header-align="center"
        align="center"
        label="申请企业">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="getEntDetailsInfo(scope.row.entEnterpriseInfo.entInfoId, scope.row.entEnterpriseInfo.inApply)">{{scope.row.entEnterpriseInfo.entName}}</el-button>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="300"
        label="操作">
        <template slot-scope="scope">
          <!-- isAuth('enterprise:info:shenhe') -->
          <el-button v-if="true" type="text" size="small" @click="detailHandle(scope.row.proCooperationInfoId)">申请合作列表</el-button>
          <el-button v-if="true" type="text" size="small" @click="consentHandle(scope.row.proCooperationInfoId)">通过</el-button>
          <el-button v-if="true" type="text" size="small" @click="retreatHandle(scope.row.proCooperationInfoId)">不通过</el-button>
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
    <relation-details v-if="shenhe" ref="details" @refreshDataList="getDetailsInfo()"/>
    <!-- 弹窗, 学生 / 教师 / 企业详情 -->
    <ent-details v-if="entDetails" ref="entDetails" @refreshDataList="getEntDetailsInfo()"/>
    <tea-details v-if="teaDetails" ref="teaDetails"/>
    <stu-details v-if="stuDetails" ref="stuDetails"/>

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
import RelationDetails from '../relation/relation-details'
import EntDetails from '../base/ent-details'
import TeaDetails from '../base/tea-details'
import StuDetails from '../base/stu-details'
export default {
  data () {
    return {
      tempPro: null, // 项目临时信息
      consenttip: '您确认要通过该审核吗？合作双方将信息可见，该操作不可撤回！',
      retreattip: '您确认不通过该审核吗？该操作不可撤回！',
      shenhe: false,
      consentVisible: false,
      retreatVisible: false,
      entDetails: false,
      teaDetails: false,
      stuDetails: false,
      proCooperationInfoId: 0,
      hasApply: '1',
      hasType: 'userPerId',
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
      dataListSelections: [],
      proCooperationIds: []
    }
  },
  components: {
    ProjectDetails,
    RelationDetails,
    EntDetails,
    TeaDetails,
    StuDetails
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
        this.$refs.details.init(id, this.hasType, '0')
      })
    },
        // 通过
    consentHandle (proCooperationInfoId) {
      this.consentVisible = true
      this.proCooperationInfoId = proCooperationInfoId
            // this.$nextTick(() => {
            //     this.$refs.retreat.init(item.declareId, 'project_audit_apply_status', item.projectAuditApplyStatus)
            // })
    },
        // 不通过
    retreatHandle (proCooperationInfoId) {
      this.retreatVisible = true
      this.proCooperationInfoId = proCooperationInfoId
            // this.$nextTick(() => {
            //     this.$refs.retreat.init(item.declareId, 'project_audit_apply_status', item.projectAuditApplyStatus)
            // })
    },
        // 获取详情信息
    getDetailsInfo () {

    },
    changeType (hasType) {
      if (hasType === 'userPerId') {
        this.dataList = this.dataStuList
      }
      if (hasType === 'userTeacherId') {
        this.dataList = this.dataTeaList
      }
      if (hasType === 'entInfoId') {
        this.dataList = this.dataEntList
      }
    },
      // 企业详情弹窗
    getEntDetailsInfo (id, hasApply) {
      console.log(id + hasApply)
      this.entDetails = true
      this.$nextTick(() => {
        this.$refs.entDetails.init(id, hasApply)
      })
    },
      // 教师详情弹窗
    getTeaDetailsInfo (id) {
      console.log(id)
      this.teaDetails = true
      this.$nextTick(() => {
        this.$refs.teaDetails.init(id)
      })
    },
      // 学生详情弹窗
    getStuDetailsInfo (id) {
      console.log(id)
      this.stuDetails = true
      this.$nextTick(() => {
        this.$refs.stuDetails.init(id)
      })
    },
        // 获取数据列表
    getDataList () {
      this.dataListLoading = true
      this.$http({
        // url: this.$http.adornUrl('/enterprise/person/cooperation/list'),
        url: this.$http.adornUrl('/enterprise/person/cooperation/proList'),
        method: 'get',
        params: this.$http.adornParams({
          'currPage': this.pageIndex,
          'pageSize': this.pageSize,
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
        url: this.$http.adornUrl('/enterprise/project/cooperation/entExamine'),
        method: 'post',
        params: this.$http.adornParams({
          'proCooperationInfoId': this.proCooperationInfoId,
          'inApply': '2'
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
        url: this.$http.adornUrl('/enterprise/project/cooperation/entExamine'),
        method: 'post',
        params: this.$http.adornParams({
          'proCooperationInfoId': this.proCooperationInfoId
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
