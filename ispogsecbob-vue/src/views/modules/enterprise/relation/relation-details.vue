<template>
  <div class="mod-user">
  <el-dialog
    :title="'申请合作列表'"
    :close-on-click-modal="false"
    width="75%"
    :visible.sync="visible">
    <el-card>
      <el-radio-group v-model="hasType" @change="getDataList(hasType)">
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
        prop="sysUserEntity.name"
        header-align="center"
        align="center"
        v-if="hasType == 'userPerId'"
        label="申请人">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="getStuDetailsInfo(scope.row.userPerId)">{{scope.row.sysUserEntity.name}}</el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="sysUserEntity.name"
        header-align="center"
        align="center"
        v-if="hasType == 'userTeacherId'"
        label="申请人">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="getTeaDetailsInfo(scope.row.userTeacherId)">{{scope.row.sysUserEntity.name}}</el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="entEnterpriseInfo.entName"
        header-align="center"
        align="center"
        v-if="hasType == 'entInfoId'"
        label="申请企业">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="getEntDetailsInfo(scope.row.entInfoId, scope.row.inApply)">{{scope.row.entName}}</el-button>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="400"
        label="操作">
        <template slot-scope="scope">
          <!-- isAuth('enterprise:info:shenhe') -->
          <el-button v-if="inApply == '0'" type="text" size="small" @click="consentHandle(scope.row)">同意申请</el-button>
          <!--          <el-button v-if="inApply == '2'" type="text" size="small" @click="retreatHandle(scope.row)">拒绝</el-button>-->
          <el-button v-if="hasType == 'userPerId'" type="text" size="small" @click="getStuDetailsInfo(scope.row.userPerId)">详情</el-button>
          <el-button v-if="hasType == 'userTeacherId'" type="text" size="small" @click="getTeaDetailsInfo(scope.row.userTeacherId)">详情</el-button>
          <el-button v-if="hasType == 'entInfoId'" type="text" size="small" @click="getEntDetailsInfo(scope.row.entInfoId, scope.row.inApply)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="visible = false">返回</el-button>
    </span>
  </el-dialog>
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
import EntDetails from '../base/ent-details'
import TeaDetails from '../base/tea-details'
import StuDetails from '../base/stu-details'
export default {
  data () {
    return {
      item: null,
      visible: false,
      entDetails: false,
      teaDetails: false,
      stuDetails: false,
      consenttip: '您确认要同意该合作申请吗？',
      retreattip: '您确认要拒绝该合作申请吗？',
      shenhe: false,
      consentVisible: false,
      retreatVisible: false,
      proInfoId: '',
      proCooperationInfoId: '',
      inApply: '1',
      applyName: '',
      dataList: [],
      dataStuList: [],
      dataTeaList: [],
      dataEntList: [],
      dataListSelections: [],
      dataForm: {
        key: '',
        proName: '',
        proCooperationInfoId: 0,
        cooperationContent: '',
        cooperationType: '',
        cooperationRequire: '',
        userPerId: '',
        userTeacherId: '',
        entInfoId: '',
        inApply: ''
      },
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      hasType: 'userPerId'
    }
  },
  components: {
    EntDetails,
    TeaDetails,
    StuDetails
  },
  methods: {
    init (id, hasType, inApply) {
      this.proCooperationInfoId = id || 0
      this.visible = true
      this.hasType = hasType
      this.inApply = inApply
      console.log(this.proCooperationInfoId)
      console.log(this.hasType)
      console.log(this.inApply)
      this.$nextTick(() => {
        this.$http({
          url: this.$http.adornUrl(`/enterprise/person/cooperation/info/${this.proCooperationInfoId}/${this.hasType}/${this.inApply}`),
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.dataForm.key,
            'inType': this.hasType
          })
        }).then(({data}) => {
          console.log(data)
          if (data && data.code === 0) {
            this.dataStuList = data.entProjectCooperationInfoEntity.userPersonInfoEntities
            this.dataTeaList = data.entProjectCooperationInfoEntity.userTeacherInfoEntities
            this.dataEntList = data.entProjectCooperationInfoEntity.entEnterpriseInfoEntities
            this.dataList = this.dataStuList
            this.totalPage = data.totalCount
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
          console.log(this.dataList)
        })
      })
    },
      // 获取数据列表
    getDataList (hasType) {
      this.hasType = hasType
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl(`/enterprise/person/cooperation/info/${this.proCooperationInfoId}/${this.hasType}/${this.inApply}`),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'key': this.dataForm.key,
          'inType': this.hasType
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          console.log(data)
          if (this.hasType === 'userPerId') {
            this.dataList = data.entProjectCooperationInfoEntity.userPersonInfoEntities
          }
          if (this.hasType === 'userTeacherId') {
            this.dataList = data.entProjectCooperationInfoEntity.userTeacherInfoEntities
          }
          if (this.hasType === 'entInfoId') {
            this.dataList = data.entProjectCooperationInfoEntity.entEnterpriseInfoEntities
          }
          this.totalPage = data.totalCount
        } else {
          this.dataList = []
          this.totalPage = 0
        }
        this.dataListLoading = false
      })
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
      // 通过
    consentHandle (item) {
      this.consentVisible = true
      this.item = item
          // this.$nextTick(() => {
          //     this.$refs.retreat.init(item.declareId, 'project_audit_apply_status', item.projectAuditApplyStatus)
          // })
    },
      // 不通过
    retreatHandle (item) {
      this.retreatVisible = true
      this.item = item
          // this.$nextTick(() => {
          //     this.$refs.retreat.init(item.declareId, 'project_audit_apply_status', item.projectAuditApplyStatus)
          // })
    },
      // 审批通过
    applyConsentHandle () {
      this.$http({
        url: this.$http.adornUrl('/enterprise/person/cooperation/update'),
        method: 'post',
        params: this.$http.adornParams({
          'proCooperationId': this.item.entPersonCooperationInfoEntity.proCooperationId,
          'inApply': '1'
        }, false)
      }).then(({data}) => {
        this.$message({
          type: 'success',
          message: '提交成功!'
        })
        this.consentVisible = false
        this.getDataList(this.hasType)
      })
    },
      // 审批不通过
    applyRetreatHandle () {
      this.$http({
        url: this.$http.adornUrl('/enterprise/person/cooperation/update'),
        method: 'post',
        params: this.$http.adornParams({
          'proCooperationId': this.item.entPersonCooperationInfoEntity.proCooperationId,
          'inApply': '2'
        }, false)
      }).then(({data}) => {
        this.$message({
          type: 'success',
          message: '提交成功!'
        })
        this.retreatVisible = false
        this.getDataList(this.hasType)
      })
    },
    // 多选
    selectionChangeHandle (val) {
      this.dataListSelections = val
    },
    details (id) {
      console.log(id)
    }
  },
      // 删除
  deleteHandle (id) {
    var ids = id ? [id] : this.dataListSelections.map(item => {
      return item.proCooperationId
    })
    this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      this.$http({
        url: this.$http.adornUrl('/enterprise/entpersoncooperationinfo/delete'),
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
</script>
