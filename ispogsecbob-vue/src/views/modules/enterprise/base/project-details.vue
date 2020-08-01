<template>
  <div class="mod-user">
    <el-dialog
      width="60%"
      :modal-append-to-body='false'
      :visible.sync="visible">
      <div id="project">
        <div class="menuList">
          <ul class="project-ul">
            <li v-for="(item,index) in list" :key="item.id" :class="{active:num==index}" @click="getNum(index)">
              {{item}}
            </li>
          </ul>
        </div>
        <div class="tabCon">
          <div v-if="viewType === 0">
            <el-form :model="dataForm" ref="dataForm" label-width="150px">
              <el-form-item label="项目名称" prop="proName">
                <el-input v-model="dataForm.proName" :readonly="true"></el-input>
              </el-form-item>
              <el-form-item label="项目登记" prop="proRegister">
                <el-input v-model="dataForm.proRegister" :readonly="true"></el-input>
              </el-form-item>
              <el-form-item label="项目来源" prop="proOrigin">
                <el-input type="textarea" v-model="dataForm.proOrigin" :readonly="true"></el-input>
              </el-form-item>
              <el-form-item label="项目经费" prop="proOutlay">
                <el-input v-model="dataForm.proOutlay" :readonly="true"></el-input>
              </el-form-item>
              <el-form-item label="项目类型" prop="proType">
                <el-input v-model="dataForm.proType" :readonly="true"></el-input>
              </el-form-item>
              <el-form-item label="项目介绍" prop="proIntroduce">
                <el-input type="textarea" v-model="dataForm.proIntroduce" :readonly="true"></el-input>
              </el-form-item>
            </el-form>
          </div>
          <div v-if="viewType === 1">
            <div v-if="hasType === 'userPerId'">
              {{sysUser.name}}
            </div>
            <div v-if="hasType === 'userTeacherId'">
              {{sysUser.name}}
            </div>
            <div v-if="hasType === 'entInfoId'">
              {{sysUser.name}}
            </div>
          </div>
          <div v-if="viewType === 2">
            {{projectCooperationInfo.cooperationContent}}
          </div>
          <div v-if="viewType === 3">
              <el-table
                :data="personCooperationPer"
                :default-sort="{prop: 'userId', order: 'ascending'}"
                style="width: 100%">
                <el-table-column
                  prop="userPersonInfo.sysUserEntity.name"
                  width="180">
                </el-table-column>
              </el-table>
            <div v-if="hasType === 'userTeacherId'"></div>
            <div v-if="hasType === 'entInfoId'"></div>
          </div>
          <div v-if="viewType === 4">
            <el-table
              :data="entProjectAttachEntities"
              :default-sort="{prop: 'projectAttachId', order: 'ascending'}"
              style="width: 100%">
              <el-table-column
                prop="url"
                width="180">
              </el-table-column>
            </el-table>
          </div>
<!--          <div v-for='(itemCon,index) in tabContents' v-show="index == num">-->
<!--            {{itemCon}}-->
<!--          </div>-->
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="visible = false">返回</el-button>
      </span>
      <!-- 弹窗, 学生 / 教师 / 企业详情 -->
      <ent-details v-if="entDetails" ref="entDetails" @refreshDataList="getEntDetailsInfo()"/>
      <tea-details v-if="teaDetails" ref="teaDetails"/>
      <stu-details v-if="stuDetails" ref="stuDetails"/>
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
      visible: false,
      entDetails: false,
      teaDetails: false,
      stuDetails: false,
      dataList: [],
      viewType: 0,
      activeNames: ['1'],
      hasType: 'userPerId',
      num: 0,
      list: ['项目信息', '负责人', '合作信息', '成员', '附件、专利'],
      tabContents: [
        // this.dataForm, this.dataForm.sysUser, this.dataForm.projectCooperationInfo, this.dataForm.projectCooperationInfo, this.dataForm.entProjectAttachEntities
      ],
      dataForm: {
        proInfoId: 0,
        proName: '',
        proRegister: '',
        proOrigin: '',
        proOutlay: '',
        proType: '',
        proIntroduce: '',
        inApply: ''
      },
      sysUser: '',
      entProjectAttachEntities: [],
      projectCooperationInfo: '',
      personCooperationPer: [],
      personCooperationTeacher: [],
      personCooperationEnt: []
    }
  },
  components: {
    EntDetails,
    TeaDetails,
    StuDetails
  },
  methods: {
    init (hasType, id) {
      this.visible = true
      this.dataForm.proInfoId = id
      this.hasType = hasType
      if (hasType === 'entInfoId') {
        this.list.splice(1, 5, '企业详情', '合作信息', '成员', '附件、专利')
      }
      console.log(`${this.hasType}/${this.dataForm.proInfoId}`)
      if (this.dataForm.proInfoId) {
        this.$http({
          url: this.$http.adornUrl(`/enterprise/project/info/projectInfo/${this.hasType}/${this.dataForm.proInfoId}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            console.log(data)
            this.dataForm = data.data
            this.sysUser = data.data.sysUser
            this.entProjectAttachEntities = data.data.entProjectAttachEntities
            this.projectCooperationInfo = data.data.projectCooperationInfo
            this.personCooperationPer = data.data.projectCooperationInfo.personCooperationPer
            this.personCooperationTeacher = data.data.projectCooperationInfo.personCooperationTeacher
            this.personCooperationEnt = data.data.projectCooperationInfo.personCooperationEnt
          }
        })
      }
    },
    getNum (index) {
      this.num = index
      this.viewType = index
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
    handleChange (val) {
      console.log(val)
    }
  }
}
</script>

<style>
  .d1{
    padding-bottom: 15px;
  }
  html,
  body {
    height: 100%;
    margin: 0;
    padding: 0;
    background-color: #58596b;
  }

  .active {
    color: #fff;
    background: #e74c3c;
  }

  #project {
    width: 100%;
    height: 90%;
    margin-bottom: 100px;
    background-color: #fff;
    box-shadow: 0 1px 3px rgba(0, 0, 0, .1);

  }

  .menuList {
    width: 100%;
    height: 60px;
    background-color: #33344a;
  }

  .project-ul {
    width: 100%;
    display: flex;
    list-style: none;
    padding: 0;
    margin: 0;
    color: #717181;
    font-size: 16px;
    line-height: 60px;

  }

  .project-ul li {
    flex: 1;
    text-align: center;
    cursor: pointer;
  }

  .tabCon {
    width: 700px;
    margin: 0 auto;
    padding: 40px 20px;
    color: #999;
    font-size: 14px;
    background-color: #fff;
  }
</style>
