<template>
  <el-dialog
    :title="'企业审核'"
    :close-on-click-modal="false"
    width="60%"
    :visible.sync="visible">
    <el-form :model="dataForm" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="150px">
      <el-form-item label="企业名称" prop="entEnterpriseInfo.entName">
        <el-input v-model="dataForm.entEnterpriseInfo.entName" :readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="工作内容" prop="jobContent">
        <el-input type="textarea" v-model="dataForm.jobContent" :readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="招聘岗位" prop="recruitmentPost">
        <el-input type="textarea" v-model="dataForm.recruitmentPost" :readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="招聘人数" prop="recruitmentPeopleNumber">
        <el-input v-model="dataForm.recruitmentPeopleNumber" :readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="招聘专业" prop="recruitmentSpecialty">
        <el-input type="textarea" v-model="dataForm.recruitmentSpecialty" :readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="岗位要求" prop="postRequire">
        <el-input type="textarea" v-model="dataForm.postRequire" :readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="工作方式" prop="workWay">
        <el-input type="textarea" v-model="dataForm.workWay" :readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="是否高新区" prop="newHighZones">
        <el-radio-group v-model="dataForm.entEnterpriseInfo.newHighZones" size="small" :readonly="true">
          <el-radio border label="0" :readonly="true">是</el-radio>
          <el-radio border label="1" :readonly="true">否</el-radio>
        </el-radio-group>
      </el-form-item>

    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="visible = false">返回</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data () {
    return {
      visible: false,
      dataList: [],
      dataForm: {
        recruitmentInfoId: 0,
        entName: '',
        jobContent: '',
        recruitmentPost: '',
        recruitmentPeoplePumber: '',
        recruitmentSpecialty: '',
        postRequire: '',
        workWay: '',
        newHighZones: '',
        inApply: ''
      }
    }
  },
  methods: {
    init (id) {
      this.visible = true
      this.dataForm.id = id || 0
      if (this.dataForm.id) {
        this.$http({
          url: this.$http.adornUrl(`/enterprise/recruitment/info/info/${this.dataForm.id}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          console.log(this.dataForm.id)
          if (data && data.code === 0) {
            this.dataForm = data.data
          }
        })
      }
    }
  }
}
</script>
