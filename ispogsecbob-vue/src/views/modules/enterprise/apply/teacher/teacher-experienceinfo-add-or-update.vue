<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="科研内容" prop="teaExperienceContent">
        <el-input type="textarea" maxlength="400"
                  :rows="5" v-model="dataForm.teaExperienceContent" placeholder="科研内容" ></el-input>
      </el-form-item>
      <el-col :span="24">
        <el-form-item label="附件">
          <el-upload
            ref="upLoadFiles"
            class="upload-demo"
            :file-list="dataForm.entTeacherAttachmentList"
            :action="url"
            :data="{teacherId: dataForm.userTeacherId}"
            :on-success="successHandle"
            :on-remove="removeHandle">
            <el-button size="small" icon="el-icon-upload" type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
      </el-col>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button :loading="loading" type="primary" @click="dataFormSubmit()">确定</el-button>
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
      return {
        url: '',
        visible: false,
        loading: false,
        dataForm: {
          teaExperienceId: 0,
          userTeacherId: this.$store.state.user.id,
          teaExperienceContent: '',
          entTeacherAttachmentList: [],
          entTeacherAttachmentEntities: [],
          inApply: ''
        },
        dataRule: {
          teaExperienceContent: [
            { required: true, message: '科研内容不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.url = this.$http.adornUrl(`/enterprise/teacher/attachment/save?token=${this.$cookie.get('token')}`)
        this.dataForm.teaExperienceId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs.upLoadFiles.clearFiles()
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.teaExperienceId) {
            this.$http({
              url: this.$http.adornUrl(`/enterprise/teacher/experience/info/${this.dataForm.teaExperienceId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.userTeacherId = data.entTeacherExperienceInfo.userTeacherId
                this.dataForm.teaExperienceContent = data.entTeacherExperienceInfo.teaExperienceContent
                this.dataForm.inApply = data.entTeacherExperienceInfo.inApply
                this.dataForm.entTeacherAttachmentEntities = data.entTeacherExperienceInfo.entTeacherAttachmentEntities
                let attachmentList = []
                for (var index = 0; index < this.dataForm.entTeacherAttachmentEntities.length; index++) {
                  attachmentList.push(new Attachment(
                    this.dataForm.entTeacherAttachmentEntities[index].teaAttachmentName,
                    this.dataForm.entTeacherAttachmentEntities[index].teaAttachmentUrl,
                    this.dataForm.entTeacherAttachmentEntities[index]
                  ))
                }
                this.dataForm.entTeacherAttachmentList = attachmentList
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
              url: this.$http.adornUrl(`/enterprise/teacher/experience/${!this.dataForm.teaExperienceId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'teaExperienceId': this.dataForm.teaExperienceId || undefined,
                'userTeacherId': this.$store.state.user.id,
                'teaExperienceContent': this.dataForm.teaExperienceContent,
                'entTeacherAttachmentEntities': this.dataForm.entTeacherAttachmentEntities,
                'inApply': 0
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
              this.$refs.upLoadFiles.clearFiles()
              this.loading = false
            })
          }
        })
      },
      // 上传成功
      successHandle (response, file, fileList) {
        if (response && response.code === 0) {
          this.dataForm.entTeacherAttachmentEntities.push(response.entTeacherAttachmentEntity)
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
        this.dataForm.entTeacherAttachmentEntities = tempFileList
      }
    }
  }
</script>
