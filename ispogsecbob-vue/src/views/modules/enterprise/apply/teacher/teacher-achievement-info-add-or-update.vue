<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="奖励内容" prop="teaAchievementContent">
      <el-input type="textarea" maxlength="400"
                :rows="5" v-model="dataForm.teaAchievementContent" placeholder="教师所获奖励内容" ></el-input>
    </el-form-item>
      <el-col :span="24">
        <el-form-item label="获奖证书">
          <el-upload
            ref="upLoadFiles"
            class="upload-demo"
            :action="url"
            :limit="1"
            :data="{teacherId: dataForm.teacherId}"
            :on-success="successHandle">
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
  export default {
    data () {
      return {
        loading: false,
        visible: false,
        url: '',
        dataForm: {
          teaAchievementId: 0,
          userTeacherId: '',
          teaAchievementContent: '',
          inApply: '',
          teacherId: this.$store.state.user.id,
          entTeacherAttachmentEntitys: {}
        },
        dataRule: {
          userTeacherId: [
            { required: true, message: '教师基本信息表外键不能为空', trigger: 'blur' }
          ],
          teaAchievementContent: [
            { required: true, message: '教师所获奖励内容不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.url = this.$http.adornUrl(`/enterprise/teacher/attachment/save?token=${this.$cookie.get('token')}`)
        this.dataForm.teaAchievementId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.teaAchievementId) {
            this.$http({
              url: this.$http.adornUrl(`/enterprise/teacher/achievement/info/${this.dataForm.teaAchievementId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.userTeacherId = data.entTeacherAchievementInfo.userTeacherId
                this.dataForm.teaAchievementContent = data.entTeacherAchievementInfo.teaAchievementContent
                this.dataForm.inApply = data.entTeacherAchievementInfo.inApply
                this.dataForm.entTeacherAttachmentEntity = data.entTeacherAchievementInfo.entTeacherAttachmentEntity
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            console.log(this.dataForm.entEnterpriseAttachmentEntity)
            this.loading = true
            this.$http({
              url: this.$http.adornUrl(`/enterprise/teacher/achievement/${!this.dataForm.teaAchievementId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'teaAchievementId': this.dataForm.teaAchievementId || undefined,
                'userTeacherId': this.dataForm.teacherId,
                'teaAchievementContent': this.dataForm.teaAchievementContent,
                'inApply': 0,
                'entTeacherAttachmentEntity': this.dataForm.entTeacherAttachmentEntity
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$refs.upLoadFiles.clearFiles()
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.dataForm.entTeacherAttachmentEntity = {}
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
          this.dataForm.entTeacherAttachmentEntity = response.entTeacherAttachmentEntity
        } else {
          this.$message.error(response.msg)
        }
      }
    }
  }
</script>
