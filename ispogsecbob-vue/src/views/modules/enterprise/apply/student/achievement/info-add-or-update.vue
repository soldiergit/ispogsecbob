<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-col :span="24">
        <el-form-item label="奖励内容" prop="stuAchievementContent">
          <el-input type="textarea" maxlength="400"
                    :rows="5" v-model="dataForm.stuAchievementContent" placeholder="所获奖励/证书内容" ></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="获奖时间" prop="achievementTime">
          <el-date-picker
            v-model="dataForm.achievementTime"
            align="right"
            type="year"
            placeholder="请选择年度">
          </el-date-picker>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="获奖地点" prop="achievementPlace">
          <el-input v-model="dataForm.achievementPlace" placeholder="请输入奖励/证书获得地点"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="获奖级别" prop="achievementAgree">
          <el-select v-model="dataForm.achievementAgree"  placeholder="请选择">
            <el-option v-for="item in agreeList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="奖励类型" prop="achievementType">
          <el-select v-model="dataForm.achievementType"  placeholder="请选择">
            <el-option v-for="item in typeList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="获奖证书" prop="entStudentAttachmentEntity">
          <el-upload
            ref="upLoadFiles"
            class="upload-demo"
            :action="url"
            :limit="1"
            :data="{userPerId: dataForm.userPerId}"
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
      var validateAttach = (rule, value, callback) => {
        if (JSON.stringify(this.dataForm.entStudentAttachmentEntity) === '{}') {
          callback(new Error('获奖证书不能为空'))
        } else {
          callback()
        }
      }
      return {
        loading: false,
        visible: false,
        url: '',
        dataForm: {
          stuAchievementId: 0,
          userPerId: this.$store.state.user.id,
          stuAchievementContent: '',
          achievementTime: new Date(),
          achievementPlace: '',
          achievementAgree: '',
          achievementType: '',
          inApply: '',
          entStudentAttachmentEntity: {}
        },
        agreeList: [
          {value: 1, label: '校级'},
          {value: 2, label: '省级'},
          {value: 3, label: '国家级'}
        ],
        typeList: [
          {value: 1, label: '学科竞赛'},
          {value: 2, label: '专业比赛'}
        ],
        dataRule: {
          userPerId: [
            { required: true, message: '学生基本信息不能为空', trigger: 'blur' }
          ],
          stuAchievementContent: [
            { required: true, message: '所获奖励/证书内容不能为空', trigger: 'blur' }
          ],
          achievementTime: [
            { required: true, message: '学生获奖时间不能为空', trigger: 'blur' }
          ],
          achievementPlace: [
            { required: true, message: '学生获奖地点不能为空', trigger: 'blur' }
          ],
          achievementAgree: [
            { required: true, message: '请选择获奖级别', trigger: 'blur' }
          ],
          achievementType: [
            { required: true, message: '请选择获奖类型', trigger: 'blur' }
          ],
          entStudentAttachmentEntity: [
            { validator: validateAttach, trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.url = this.$http.adornUrl(`/enterprise/student/attachment/save?token=${this.$cookie.get('token')}`)
        this.dataForm.stuAchievementId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.stuAchievementId) {
            this.$http({
              url: this.$http.adornUrl(`/enterprise/student/achievement/info/${this.dataForm.stuAchievementId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                // this.dataForm.userPerId = data.entStudentAchievementInfo.userPerId
                // this.dataForm.stuAchievementContent = data.entStudentAchievementInfo.stuAchievementContent
                // this.dataForm.achievementTime = data.entStudentAchievementInfo.achievementTime
                // this.dataForm.achievementPlace = data.entStudentAchievementInfo.achievementPlace
                // this.dataForm.achievementAgree = data.entStudentAchievementInfo.achievementAgree
                // this.dataForm.achievementType = data.entStudentAchievementInfo.achievementType
                // this.dataForm.inApply = data.entStudentAchievementInfo.inApply
                // this.dataForm.entStudentAttachmentEntity = data.entStudentAchievementInfo.entStudentAttachmentEntity
                this.dataForm = data.entStudentAchievementInfo
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            console.log(this.dataForm.entStudentAttachmentEntity)
            this.loading = true
            this.$http({
              url: this.$http.adornUrl(`/enterprise/student/achievement/${!this.dataForm.stuAchievementId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'stuAchievementId': this.dataForm.stuAchievementId || undefined,
                'userPerId': this.dataForm.userPerId,
                'stuAchievementContent': this.dataForm.stuAchievementContent,
                'achievementTime': new Date(this.dataForm.achievementTime).getTime(),
                'achievementPlace': this.dataForm.achievementPlace,
                'achievementAgree': this.dataForm.achievementAgree,
                'achievementType': this.dataForm.achievementType,
                'inApply': 0,
                'entStudentAttachmentEntity': this.dataForm.entStudentAttachmentEntity
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.dataForm.entStudentAttachmentEntity = {}
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
          this.dataForm.entStudentAttachmentEntity = response.entStudentAttachmentEntity
        } else {
          this.$message.error(response.msg)
        }
      }
    }
  }
</script>
