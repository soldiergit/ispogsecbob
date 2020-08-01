<template>
  <el-dialog
    :title="'学生基本信息'"
    :modal-append-to-body='false'
    width="60%"
    :visible.sync="visible">
    <el-row>
      <table border="1" align="center" cellpadding="10" width="100%">
        <tr>
          <th colspan="7">学生详情</th>
        </tr>

        <tr>
          <th>姓名:</th>
          <td><span v-text="dataForm.sysUserEntity.name" align="center"></span></td>
          <th>年龄:</th>
          <td><span v-text="dataForm.perAge" align="center"></span></td>
          <th>民族:</th>
          <td><span v-text="dataForm.sysUserEntity.name" align="center"></span></td>
          <td rowspan="3" width="100"> <p><a href=""><img border="0" src="http://" />
        </a></p></td>
        </tr>

        <tr>
          <th>出生日期:</th>
          <td><span v-text="dataForm.sysUserEntity.name" align="center"></span></td>
          <th>政治面貌:</th>
          <td><span v-text="dataForm.perPoliticsType" align="center"></span></td>
          <th>性别:</th>
          <td><span v-text="dataForm.perSex" align="center"></span></td>
        </tr>

        <tr>
          <th>学号:</th>
          <td><span v-text="dataForm.perStuNo" align="center"></span></td>
          <th>专业:</th>
          <td><span v-text="dataForm.sysUserEntity.name" align="center"></span></td>
          <th>年级:</th>
          <td><span v-text="dataForm.gradeId" align="center"></span></td>
        </tr>

        <tr>
          <th>班级:</th>
          <td><span v-text="dataForm.perClassNo" align="center"></span></td>
          <th>宿舍:</th>
          <td><span v-text="dataForm.perCormNo" align="center"></span></td>
<!--          <th>身份证号:</th>-->
<!--          <td colspan="2"><span v-text="dataForm.perCardNo" align="center"></span></td>-->
        </tr>

        <tr>
          <th>在校职务:</th>
          <td><span v-text="dataForm.perSchoolPost" align="center"></span></td>
          <th>爱好:</th>
          <td><span v-text="dataForm.perInterest" align="center"></span></td>
          <th>籍贯:</th>
          <td colspan="2"><span v-text="dataForm.perNative" align="center"></span></td>
        </tr>

        <tr>
          <th>工作方式:</th>
          <td><span v-text="dataForm.perWorking" align="center"></span></td>
          <th>企业职务:</th>
          <td><span v-text="dataForm.perPost" align="center"></span></td>
          <th>ＱＱ:</th>
          <td colspan="2"><span v-text="dataForm.perQq" align="center"></span></td>
        </tr>

        <tr>
          <th height="160">个人特长:</th>

          <td colspan="6">
            <p>我爱计算机，我爱计算机我爱计算机，我爱计算机我爱计算机，我爱计算机</p>
            <p>我爱计算机，我爱计算机我爱计算机，我爱计算机我爱计算机，我爱计算机</p>
            <p>我爱计算机，我爱计算机我爱计算机，我爱计算机我爱计算机，我爱计算机</p>
          </td>
        </tr>

        <tr>
          <th height="160">所获荣誉:</th>
          <td colspan="6"><span v-text="dataForm.perSchoolHonor" align="center"></span></td>
        </tr>

        <tr>
          <th height="160">社会实践:</th>
          <td colspan="6"><span v-text="dataForm.perSocialPractice" align="center"></span></td>
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
        userPerId: '',
        dataList: [],
        dataForm: {
        }
      }
    },
    methods: {
      init (id) {
        this.visible = true
        this.userPerId = id || 0
        console.log(id)
        if (this.userPerId) {
          this.$http({
            url: this.$http.adornUrl(`/innovate/use/person/perInfo/${this.userPerId}`),
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
