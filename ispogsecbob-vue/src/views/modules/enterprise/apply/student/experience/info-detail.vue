<template>
  <el-dialog
    :title="'详情'"
    width="80%"
    v-loading="dataListLoading"
    @close="closeDialog"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-row>
      <table border="1" cellspacing="0" width="100%" class="table">
        <tr align='center'>
          <td colspan="10" style="height: 1.2rem"></td>
        </tr>
        <tr class="contents" align="center">
          <th colspan="10">
            学生工作/项目经历基本信息
          </th>
        </tr>
        <tr align='center'>
          <td colspan="10" style="height: 1.2rem"></td>
        </tr>
        <tr align='center' style="height: 2.5rem">
          <th colspan="2">工作/项目经历内容</th>
          <td colspan="3"><span v-text="entStudentExperienceInfo.stuExperienceContent"></span></td>
          <th colspan="2">工作/项目经历时间</th>
          <td colspan="3"><span v-text="dateFormat(entStudentExperienceInfo.stuExperienceTime)"></span></td>
        </tr>
        <tr align='center' style="height: 2.5rem">
          <th colspan="2">工作/项目经历地点</th>
          <td colspan="3"><span v-text="entStudentExperienceInfo.stuExperiencePlace"></span></td>
          <th colspan="2">工作/项目经历类型</th>
          <td colspan="3"><span v-for="stuExperienceWorkType in workTypeList" v-if="entStudentExperienceInfo.stuExperienceWorkType === stuExperienceWorkType.value" v-text="stuExperienceWorkType.label" ></span></td>
        </tr>
        <tr v-for="item in entStudentExperienceInfo.entStudentAttachmentEntities">
          <th colspan="2">附件</th>
          <td align="center" colspan="5" v-text="item.stuAttachmentName"></td>
          <td align="center" colspan="3">
            <el-button type="success" @click="attachDown(item)">下载附件</el-button>
          </td>
        </tr>
        <tr align='center' v-if="entStudentExperienceInfo.inApply === 3">
          <th colspan="2">不通过意见</th>
          <td colspan="8"><span style="color: red" v-text="entStudentExperienceInfo.retreatOption"></span></td>
        </tr>
        <!--项目负责人开始-->
        <tr align='center'>
          <td colspan="10" style="height: 1.2rem"></td>
        </tr>
        <tr class="contents"><th colspan="10">学生信息</th></tr>
        <tr align='center'>
          <td colspan="10" style="height: 1.2rem"></td>
        </tr>
        <template v-if="perInfo != null">
          <tr align='center'>
            <th>姓名</th>
            <td><span v-if="perInfo.sysUserEntity != null" v-text="perInfo.sysUserEntity.name" align="center"></span></td>
            <th>性别</th>
            <template v-for="sex in sexList">
              <td v-if="perInfo.perSex === sex.value" v-text="sex.label"></td>
            </template>
            <th>企业职务</th>
            <td><span v-text="perInfo.perPost" align="center"></span></td>
            <th>联系电话</th>
            <td><span v-if="perInfo.sysUserEntity != null" v-text="perInfo.sysUserEntity.mobile" align="center"></span></td>
            <th>QQ号码</th>
            <td><span v-text="perInfo.perQq" align="center"></span></td>
          </tr>

          <tr align='center'>
            <th>政治面貌</th>
            <td><span v-text="perInfo.perPoliticsType" align="center"></span></td>
            <th>学号</th>
            <td><span v-text="perInfo.perStuNo" align="center"></span></td>
            <th>所在二级学院</th>
            <td>
              <span v-if="perInfo.sysUserEntity != null">
              <span v-for="item in instituteList"
                    v-if="item.instituteId === perInfo.sysUserEntity.instituteId"
                    v-text="item.instituteName"
                    align="center"></span>
              </span>
            </td>
            <th>所在年级</th>
            <td>
              <span v-for="item in gradeList"
                    v-if="item.gradeId === perInfo.gradeId"
                    v-text="item.gradeName"
                    align="center">
              </span>
            </td>
            <th>所在班级</th>
            <td><span v-text="perInfo.perClassNo" align="center"></span></td>
          </tr>
          <tr align='center'>
            <th>所在宿舍</th>
            <td><span v-text="perInfo.perCormNo" align="center"></span></td>
            <th>个人电子邮箱</th>
            <td colspan="2"><span v-if="perInfo.sysUserEntity != null" v-text="perInfo.sysUserEntity.email" align="center"></span></td>
<!--            <th colspan="1">身份证号码</th>-->
<!--            <td colspan="4"><span v-text="perInfo.perCardNo" align="center"></span></td>-->
          </tr>
          <tr align='center'>
            <th colspan="2">在校期间担任学生职务情况</th>
            <td colspan="8"><span v-text="perInfo.perSchoolPost" align="center"></span></td>
          </tr>
          <tr align='center'>
            <th colspan="2">在校期间所获等级证书及技能证书</th>
            <td colspan="8"><span v-text="perInfo.perSchoolHonor" align="center"></span></td>
          </tr>
          <tr align='center'>
            <th colspan="2">社会实践主要成绩简述</th>
            <td colspan="8"><span v-text="perInfo.perSocialPractice" align="center"></span></td>
          </tr>
        </template>
        <!--项目负责人结束-->
      </table>
    </el-row>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataListLoading: false,
        downloadLoading: false,
        instituteList: this.$store.state.user.institute,
        gradeList: this.$store.state.user.grade,
        roleLists: this.$store.state.user.roleIdList,
        sexList: [
          {value: 1, label: '男'}, {value: 2, label: '女'}
        ],
        workTypeList: [
          {value: 1, label: '社会实践'},
          {value: 2, label: '志愿者活动'}
        ],
        dataForm: {
          id: 0,
          name: '',
          studentName: '',
          studentSex: '',
          studentUnit: '',
          studentWorkStatus: '',
          studentPhone: '',
          studentJob: '',
          studentInstinct: ''
        },
        entStudentExperienceInfo: {},
        perInfo: {}
      }
    },
    methods: {
      init (id) {
        this.visible = true
        this.dataListLoading = true
        this.id = id
        this.dataForm.id = this.id || 0
        if (this.dataForm.id) {
          this.$http({
            url: this.$http.adornUrl(`/enterprise/student/experience/info/${this.dataForm.id}`),
            params: this.$http.adornParams({
              'stuExperienceId': this.dataForm.id
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.entStudentExperienceInfo = data.entStudentExperienceInfo
              this.perInfo = data.perInfo
              this.dataListLoading = false
            }
          })
        } else {
          this.dataListLoading = false
        }
      },
      attachDown (attach) {
        this.downloadLoading = true
        this.$httpFile({
          url: this.$httpFile.adornUrl(`/innovate/check/attach/download`),
          method: 'post',
          params: this.$httpFile.adornParams({
            'filePath': attach.stuAttachmentUrl
          })
        }).then(response => {
          if (!response) {
            this.downloadLoading = false
            return
          }
          let url = window.URL.createObjectURL(new Blob([response.data]))
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.setAttribute('download', attach.stuAttachmentName)
          document.body.appendChild(link)
          link.click()
          this.downloadLoading = false
        }).catch(err => {
          console.log(err)
          this.downloadLoading = false
        })
      },
      closeDialog () {
        this.visible = false
        this.$emit('refreshDataList')
      },
      // 时间格式化
      dateFormat (stuExperienceTime) {
        var t = new Date(stuExperienceTime)
        return t.getFullYear() + '年'
      }
    }
  }
</script>
<style>
  .contents{
    height: 60px;
    font-size: 18px;
  }
  .el-card__body {
    padding: 10px;
  }
  .el-step__title {
    font-size: 12px;
    line-height: 28px;
  }
  .table {
    border: 1px solid #ddd;
    /*为表格设置合并边框模型*/
    border-collapse: collapse;
    /*列宽由表格宽度和列宽度设定*/
    table-layout: fixed;
  }
  .table td {
    /*允许在单词内换行。*/
    word-break: break-word;
    /*设置宽度*/
    width: 80%;
  }
  .table th {
    /*允许在单词内换行。*/
    word-break: break-word;
    /*设置宽度*/
    width: 80%;
  }
</style>
