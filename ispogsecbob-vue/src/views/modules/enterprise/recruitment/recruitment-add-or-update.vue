<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="150px">
      <el-form-item label="工作内容" prop="jobContent">
        <el-input type="textarea" v-model="dataForm.jobContent" placeholder="请输入工位编号"></el-input>
      </el-form-item>
      <el-form-item type="textarea"label="招聘岗位" prop="recruitmentPost">
        <el-input type="textarea" v-model="dataForm.recruitmentPost" placeholder="请输入工位编号"></el-input>
      </el-form-item>
      <el-form-item label="招聘人数" prop="recruitmentPeopleNumber">
        <el-input v-model="dataForm.recruitmentPeopleNumber" placeholder="请输入工位编号"></el-input>
      </el-form-item>
      <el-form-item label="招聘专业" prop="recruitmentSpecialty">
        <el-input type="textarea" v-model="dataForm.recruitmentSpecialty" placeholder="请输入工位编号"></el-input>
      </el-form-item>
      <el-form-item label="岗位要求" prop="postRequire">
        <el-input type="textarea" v-model="dataForm.postRequire" placeholder="请输入工位编号"></el-input>
      </el-form-item>
      <el-form-item label="工作方式" prop="workWay">
        <el-input type="textarea" v-model="dataForm.workWay" placeholder="请输入工位编号"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data () {
    return {
      visible: false,
      entList: [],
      dataForm: {
        recruitmentInfoId: 0,
        entName: '',
        jobContent: '',
        recruitmentPost: '',
        recruitmentPeoplePumber: '',
        recruitmentSpecialty: '',
        postRequire: '',
        workWay: '',
        inApply: ''
      },
      dataRule: {
        jobContent: [
                    {required: true, message: '工作内容不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    init (id) {
      this.visible = true
      this.dataForm.id = id || 0
      this.$http({
        url: this.$http.adornUrl(`/enterprise/info/list`),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.entList = data.page.list
        }
      })
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$http({
            url: this.$http.adornUrl(`/enterprise/recruitment/info/info/${this.dataForm.id}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            console.info(data)
            if (data && data.code === 0) {
              this.dataForm = data.data
            }
          })
        }
      })
    },
        // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/enterprise/recruitment/info/${!this.dataForm.id ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'stationId': this.dataForm.id || undefined,
              'jobContent': this.dataForm.jobContent,
              'recruitmentPost': this.dataForm.recruitmentPost,
              'recruitmentPeopleNumber': this.dataForm.recruitmentPeopleNumber,
              'recruitmentSpecialty': this.dataForm.recruitmentSpecialty,
              'postRequire': this.dataForm.postRequire,
              'workWay': this.dataForm.workWay
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }
      })
    }
  }
}
</script>
