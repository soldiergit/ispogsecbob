<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    width="600px"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="95px">
      <el-col :span="24">
        <el-row :gutter="12">
          <el-form-item label="文件">
            <el-upload
              class="upload-demo"
              drag
              ref="upload"
              :action=uploadUrl
              multiple
              :auto-upload="false"
              :limit="1"
              :data="data"
              :on-exceed="overlimit"
              :on-success="successHandler"
              :on-error="errorHandler"
              :file-list="fileList"
            >
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            </el-upload>
          </el-form-item>
        </el-row>
      </el-col>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submitUpload()">确定上传</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        fileList: [],
        uploadUrl: this.$http.adornUrl(`/enterprise/fabric/upload?token=${this.$cookie.get('token')}`),
        data: {
          'userId': this.$store.state.user.id
        },
        dataForm: {
          id: 0,
          fileName: '',
          filePath: '',
          fileHash: '',
          fileTime: '',
          userId: '',
          status: ''
        },
        dataRule: {
          fileName: [
            { required: true, message: '文件名不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.fileList = []
        this.visible = true
        this.$nextTick(() => {
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/enterprise/fabric/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                console.log(data)
              }
            })
          }
        })
      },
      // 表单提交
      submitUpload () {
        // console.log(this.fileList)
        // if (this.fileList.length === 0) {
        //   this.$message.error('请选择文件')
        //   return
        // }
        this.$refs.upload.submit()
      },
      overlimit () {
        this.$notify.error({
          title: '错误',
          message: '超出文件上传个数限制'
        })
      },
      successHandler (res) {
        if (res.code === 0) {
          this.$notify({
            title: '成功',
            message: '添加成功',
            type: 'success'
          })
        } else {
          this.$notify.error({
            title: '错误',
            message: res.msg
          })
        }
        this.visible = false
        this.$emit('refreshDataList')
      },
      errorHandler () {
        this.$notify.error({
          title: '错误',
          message: '上传失败'
        })
      }
    }
  }
</script>
