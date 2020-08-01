<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-col :span="24">
        <el-form-item label="经历内容" prop="stuExperienceContent">
          <el-input type="textarea" maxlength="400"
                    :rows="5" v-model="dataForm.stuExperienceContent" placeholder="工作/项目经历内容" ></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="经历时间" prop="stuExperienceTime">
          <el-date-picker
            v-model="dataForm.stuExperienceTime"
            align="right"
            type="year"
            placeholder="请选择年度">
          </el-date-picker>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="经历地点" prop="stuExperiencePlace">
          <el-input v-model="dataForm.stuExperiencePlace" placeholder="请输入工作/项目经历地点"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="经历类型" prop="stuExperienceWorkType">
          <el-select v-model="dataForm.stuExperienceWorkType"  placeholder="请选择">
            <el-option v-for="item in workTypeList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="附件" prop="entStudentAttachmentEntities">
          <el-upload
            ref="upLoadFiles"
            class="upload-demo"
            :file-list="dataForm.entStudentAttachmentList"
            :action="url"
            :data="{userPerId: dataForm.userPerId}"
            :on-success="successHandle"
            :on-remove="removeHandle">
            <el-button size="small" icon="el-icon-upload" type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
      </el-col>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()" :loading="loading">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  class Attachment {
    constructor (name, url, file) {
      this.name = name
      this.url = url
      this.file = file
    }
  }
  export default {
    data () {
      var validateAttach = (rule, value, callback) => {
        if (this.dataForm.entStudentAttachmentEntities.length === 0) {
          callback(new Error('附件不能为空'))
        } else {
          callback()
        }
      }
      return {
        loading: false,
        visible: false,
        url: '',
        dataForm: {
          stuExperienceId: 0,
          userPerId: this.$store.state.user.id,
          stuExperienceContent: '',
          stuExperienceTime: new Date(),
          stuExperiencePlace: '',
          stuExperienceWorkType: '',
          inApply: '',
          entStudentAttachmentEntities: [],
          entStudentAttachmentList: []
        },
        workTypeList: [
          {value: 1, label: '社会实践'},
          {value: 2, label: '志愿者活动'}
        ],
        dataRule: {
          userPerId: [
            { required: true, message: '学生基本信息不能为空', trigger: 'blur' }
          ],
          stuExperienceContent: [
            { required: true, message: '工作/项目经历内容不能为空', trigger: 'blur' }
          ],
          stuExperienceTime: [
            { required: true, message: '工作/项目经历时间不能为空', trigger: 'blur' }
          ],
          stuExperiencePlace: [
            { required: true, message: '工作/项目经历地点不能为空', trigger: 'blur' }
          ],
          stuExperienceWorkType: [
            { required: true, message: '请选择经历工作/项目类型', trigger: 'blur' }
          ],
          entStudentAttachmentEntities: [
            { validator: validateAttach, trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.url = this.$http.adornUrl(`/enterprise/student/attachment/save?token=${this.$cookie.get('token')}`)
        this.dataForm.stuExperienceId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.stuExperienceId) {
            this.$http({
              url: this.$http.adornUrl(`/enterprise/student/experience/info/${this.dataForm.stuExperienceId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm = data.entStudentExperienceInfo
                // 新建Attachment类，用于el-upload进行数据展示
                let attachmentList = []
                for (var index = 0; index < this.dataForm.entStudentAttachmentEntities.length; index++) {
                  attachmentList.push(new Attachment(
                    this.dataForm.entStudentAttachmentEntities[index].stuAttachmentName,
                    this.dataForm.entStudentAttachmentEntities[index].stuAttachmentUrl,
                    this.dataForm.entStudentAttachmentEntities[index]
                  ))
                }
                this.dataForm.entStudentAttachmentList = attachmentList
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.loading = true
            this.$http({
              url: this.$http.adornUrl(`/enterprise/student/experience/${!this.dataForm.stuExperienceId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'stuExperienceId': this.dataForm.stuExperienceId || undefined,
                'userPerId': this.dataForm.userPerId,
                'stuExperienceContent': this.dataForm.stuExperienceContent,
                'stuExperienceTime': new Date(this.dataForm.stuExperienceTime).getTime(),
                'stuExperiencePlace': this.dataForm.stuExperiencePlace,
                'stuExperienceWorkType': this.dataForm.stuExperienceWorkType,
                'inApply': 0,
                'entStudentAttachmentEntities': this.dataForm.entStudentAttachmentEntities
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.dataForm.entStudentAttachmentEntities = []
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
              this.loading = false
            })
          }
        })
      },
      // 上传成功
      successHandle (response, file, fileList) {
        if (response && response.code === 0) {
          this.dataForm.entStudentAttachmentEntities.push(response.entStudentAttachmentEntity)
        } else {
          this.$message.error(response.msg)
        }
      },
      removeHandle (file, fileList) {
        let tempFileList = []
        for (var index = 0; index < fileList.length; index++) {
          tempFileList.push(fileList[index].file)
        }
        this.dataForm.fileList = fileList
        this.dataForm.entStudentAttachmentEntities = tempFileList
      }
    }
  }
</script>
