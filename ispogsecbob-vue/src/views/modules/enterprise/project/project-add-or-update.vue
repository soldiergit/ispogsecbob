<template>
  <el-dialog
    width="60%"
    :title="!dataForm.id ? '新增项目' : '修改项目'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-row>
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="10rem" style="width: 94%; margin: 0 auto">
        <el-col>
          <el-form-item label="项目名称" prop="project.proName">
            <el-input v-model="dataForm.project.proName" placeholder="请输入"></el-input>
          </el-form-item>
        </el-col>
        <el-col>
          <el-form-item label="项目类型" prop="project.proType">
            <el-select v-model="dataForm.project.proType"  placeholder="请选择">
              <el-option v-for="item in proTypeList" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col>
          <el-form-item label="项目介绍" prop="project.proIntroduce">
          <el-input type="textarea" maxlength="400" :rows="5" v-model="dataForm.project.proIntroduce" placeholder="请输入"></el-input>
        </el-form-item>
        </el-col>
        <el-col>
          <el-form-item label="项目经费" prop="project.proOutlay">
            <el-input v-model="dataForm.project.proOutlay" style="width: 24%" placeholder="请输入"></el-input> 万元
          </el-form-item>
        </el-col>
        <el-col>
          <el-form-item label="项目登记" prop="project.proRegister">
            <el-input type="textarea" v-model="dataForm.project.proRegister" placeholder="请输入"></el-input>
          </el-form-item>
        </el-col>
        <el-col>
          <el-form-item label="项目来源" prop="project.proOrigin">
            <el-input type="textarea" v-model="dataForm.project.proOrigin" placeholder="请输入"></el-input>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <el-form label-width="10rem" style="width: 94%; margin: 0 auto">
      <el-form-item>
        <el-row>
          <el-col :span="10">
            <span>项目附件</span>
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
        project: {
          proName: '',
          proRegister: '',
          proOrigin: '',
          proOutlay: '',
          proType: '',
          proIntroduce: '',
          inApply: '0'
        },
        attachments: [] // 项目附件
      },
      proTypeList: [
        {value: 1, label: '科研项目'},
        {value: 2, label: '横向项目'},
        {value: 3, label: '企业项目'},
        {value: 4, label: '大创项目'},
        {value: 5, label: '企业招聘'},
        {value: 6, label: '实习项目对接'}
      ],
      dataRule: {
        proName: [
                    { required: true, message: '项目名称不能为空', trigger: 'blur' }
        ],
        proRegister: [
                    { required: true, message: '项目登记不能为空', trigger: 'blur' }
        ],
        proOrigin: [
                    { required: true, message: '项目来源不能为空', trigger: 'blur' }
        ],
        proOutlay: [
                    { required: true, message: '项目经费不能为空', trigger: 'blur' }
        ],
        proType: [
                    { required: true, message: '项目类型不能为空', trigger: 'blur' }
        ],
        proIntroduce: [
                    { required: true, message: '项目介绍不能为空', trigger: 'blur' }
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
      this.dataForm.proInfoId = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.proInfoId) {
          this.$http({
            url: this.$http.adornUrl(`/enterprise/entprojectinfo/info/${this.dataForm.proInfoId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm.project = data.entprojectinfo
            }
          })
        }
      })
    },
    handleChange (file, fileList) {
      this.dataForm.attachments = fileList.slice(-3)
    },
    handleAvatarSuccess (res, file) {
      // console.log(res, file)
      this.upload = false
    },
    beforeAvatarUpload (file) {
      // // console.log(this.url)
      // const isJPG = file.type === 'image/jpeg'
      // const isLt2M = file.size / 1024 / 1024 < 2
      // if (!isJPG) {
      //   this.$message.error('上传头像图片只能是 JPG 格式!')
      // }
      // if (!isLt2M) {
      //   this.$message.error('上传头像图片大小不能超过 2MB!')
      // }
      // return isJPG && isLt2M
    },
        // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/enterprise/project/info/${!this.dataForm.proInfoId ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'project': this.dataForm.project,
              'proInfoId': this.dataForm.proInfoId ? this.dataForm.project.proInfoId : undefined,
              'proName': this.dataForm.project.proName,
              'proRegister': this.dataForm.project.proRegister,
              'proOrigin': this.dataForm.project.proOrigin,
              'proOutlay': this.dataForm.project.proOutlay,
              'proType': this.dataForm.project.proType,
              'proIntroduce': this.dataForm.project.proIntroduce,
              'inApply': this.dataForm.project.inApply,
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
