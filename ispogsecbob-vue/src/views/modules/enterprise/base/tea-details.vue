<template>
  <el-dialog
    :title="'教师基本信息'"
    :modal-append-to-body='false'
    width="60%"
    :visible.sync="visible">
    <el-row>
      <table border="1" align="center" cellpadding="10" width="100%">
        <tr>
          <th colspan="7">教师详情</th>
        </tr>

        <tr>
          <th>姓名:</th>
          <td><span v-text="dataForm.sysUserEntity.name" align="center"></span></td>
          <th>性别:</th>
          <td><span v-text="dataForm.perAge" align="center"></span></td>
          <th>手机号:</th>
          <td><span v-text="dataForm.sysUserEntity.mobile" align="center"></span></td>
          <td rowspan="3" width="100"> <p><a href=""><img border="0" src="http://" />
          </a></p></td>
        </tr>

        <tr>
          <th>学院:</th>
          <td><span v-text="dataForm.sysUserEntity.instituteId" align="center"></span></td>
          <th>职务:</th>
          <td><span v-text="dataForm.teacherPost" align="center"></span></td>
          <th>职称:</th>
          <td><span v-text="dataForm.teacherTitle" align="center"></span></td>
        </tr>

        <tr>
          <th>学历:</th>
          <td><span v-text="dataForm.teacherBackground" align="center"></span></td>
          <th>学位:</th>
          <td><span v-text="dataForm.teacherDegree" align="center"></span></td>
<!--          <th>身份证号:</th>-->
<!--          <td><span v-text="dataForm.gradeId" align="center"></span></td>-->
        </tr>

        <tr>
          <th>电子邮箱:</th>
          <td colspan="7"><span v-text="dataForm.sysUserEntity.email" align="center"></span></td>
        </tr>

        <tr>
          <th height="160">所学方向:</th>
          <td colspan="6"><span v-text="dataForm.teacherStudy" align="center"></span></td>
        </tr>

        <tr>
          <th height="160">从事科研领域:</th>
          <td colspan="6"><span v-text="dataForm.teacherScientific" align="center"></span></td>
        </tr>
      </table>
    </el-row>
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
        teacherId: '',
        dataForm: {
        }
      }
    },
    methods: {
      init (id) {
        this.visible = true
        this.teacherId = id
        console.log(id)
        if (this.teacherId) {
          this.$http({
            url: this.$http.adornUrl(`/innovate/use/teacher/teacherInfo/${this.teacherId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            console.log(data)
            if (data && data.code === 0) {
              this.dataForm = data.data
            }
          })
        }
      }
    }
  }
</script>
