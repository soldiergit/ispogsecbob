<template>
  <el-dialog
    width="60%"
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="10rem" style="width: 94%; margin: 0 auto">
      <el-form-item  label="项目名称" prop="proName">
        <el-select v-model="value" @change="getProInfoId(value)" filterable placeholder="请选择">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="项目成果" prop="proAchievementContent">
        <el-input type="textarea" maxlength="400" :rows="5" v-model="entProjectAchievementInfo.proAchievementContent" placeholder="请输入"></el-input>
      </el-form-item>
    </el-form>
    <el-form label-width="10rem" style="width: 94%; margin: 0 auto">
      <el-form-item>
        <el-row>
          <el-col :span="10">
            <span>成果附件</span>
            <el-upload
              class="upload-demo"
              :action="url"
              :on-change="handleChange"
              :file-list="dataForm.attachments">
              <el-button size="small" type="primary">点击上传</el-button>
            </el-upload>
          </el-col>
        </el-row>
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
      url: '',
      dataForm: {
        proAchievementId: '',
        attachments: []
      },
      entProjectAchievementInfo: {
        proInfoId: '',
        proAchievementContent: ''
      },
      options: null,
      value: '',
      dataRule: {
        proInfoId: [
                    { required: true, message: '项目信息外键不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  activated () {
    this.url = this.$http.adornUrl('/common/file/upload')
  },
  mounted () {
    this.url = this.$http.adornUrl('/common/file/upload')
  },
  methods: {
    init (id) {
      this.dataForm.proAchievementId = id || 0
      this.visible = true
      this.$nextTick(() => {
        if (this.dataForm.proAchievementId) {
          this.$http({
            url: this.$http.adornUrl(`/enterprise/project/achievement/info/${this.dataForm.proAchievementId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm.proAchievementId = data.entProjectAchievementInfo.proAchievementId
            }
          })
        }
      })
    },
    handleChange (file, fileList) {
      this.dataForm.attachments = fileList.slice(-3)
      console.log(this.dataForm.attachments)
    },
    handleAvatarSuccess (res, file) {
      // console.log(res, file)
      this.upload = false
    },
      // 查询用户对应项目信息
    selectProject () {
      this.$nextTick(() => {
        if (!this.dataForm.proCooperationInfoId) {
          this.$http({
            url: this.$http.adornUrl(`/enterprise/project/info/queryPeojectsByAchieve`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            console.log(data)
            const arr = []
            if (data && data.code === 0) {
              data.data && data.data.forEach(function (item, index, a) {
                arr.push({value: item.proInfoId, label: item.proName})
              })
              this.options = arr
              console.log(this.options)
            }
          })
        }
      })
    },
    getProInfoId (proInfoId) {
      this.entProjectAchievementInfo.proInfoId = proInfoId
    },
        // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/enterprise/project/achievement/${!this.dataForm.proAchievementId ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'proAchievementId': this.dataForm.proAchievementId ? this.dataForm.proAchievementId : undefined,
              'entProjectAchievementInfo': this.entProjectAchievementInfo,
              'attachments': this.dataForm.attachments
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
