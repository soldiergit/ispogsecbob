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
            科研项目基本信息
          </th>
        </tr>
        <tr align='center'>
          <td colspan="10" style="height: 1.2rem"></td>
        </tr>
        <tr align='center' style="height: 2.5rem">
          <th colspan="2">科研内容</th>
          <td colspan="8"><span style="font-size: 1.1rem; font-weight: bolder" v-text="entTeacherExperienceInfo.teaExperienceContent" align="center"></span></td>
        </tr>
        <tr v-for="item in entTeacherExperienceInfo.entTeacherAttachmentEntities">
          <th colspan="2">附件</th>
          <td align="center" colspan="5" v-text="item.teaAttachmentName"></td>
          <td align="center" colspan="3">
            <el-button type="success" @click="attachDown(item)">下载附件</el-button>
          </td>
        </tr>
        <tr align='center' v-if="entTeacherExperienceInfo.inApply === 3">
          <th colspan="2">不通过意见</th>
          <td colspan="8"><span style="color: red" v-text="entTeacherExperienceInfo.retreatOption"></span></td>
        </tr>
        <!--导师信息开始-->
        <tr align='center'>
          <td colspan="10" style="height: 1.2rem"></td>
        </tr>
        <tr class="contents" align="center">
          <th colspan="10">老师信息</th>
        </tr>
        <tr align='center'>
          <td colspan="10" style="height: 1.2rem"></td>
        </tr>
        <tr align='center'>
          <th>姓名</th>
          <th>性别</th>
          <th colspan="2">所在单位/二级学院</th>
          <th>职务</th>
          <th>职称</th>
          <th colspan="2">邮箱</th>
          <th colspan="2">联系电话</th>
        </tr>
        <template>
          <tr align="center">
            <td>
              <span v-text="teacherInfo.sysUserEntity!=null?teacherInfo.sysUserEntity.name:''"></span>
            </td>
            <td>
              <span v-for="sex in sexList"
                :key="sex.value"
                v-if="teacherInfo.teacherSex === sex.value"
                v-text="sex.label">

              </span>
            </td>
            <td colspan="2" >
              <span v-if="teacherInfo.sysUserEntity != null">
                <span v-for="institute in instituteList"
                      v-if="teacherInfo.sysUserEntity.instituteId === institute.instituteId"
                      v-text="institute.instituteName">
              </span>
              </span>
            </td>
            <td> <span v-text="teacherInfo.teacherPost"></span></td>
            <td>
              <span v-for="teacherTitle in teacherTitleList" v-if="teacherInfo.teacherTitle === teacherTitle.titleId" v-text="teacherTitle.titleName"></span>
            </td>
            <td colspan="2">
              <span v-if="teacherInfo.sysUserEntity!=null" v-text="teacherInfo.sysUserEntity.email">
              </span>
            </td>
            <td colspan="2">
              <span v-if="teacherInfo.sysUserEntity!=null" v-text="teacherInfo.sysUserEntity.mobile"></span>
            </td>
          </tr>
        </template>
      </table>
    </el-row>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
  // import FileSaver from 'file-saver'
  // import XLSX from 'xlsx'

  export default {
    data () {
      return {
        visible: false,
        dataListLoading: false,
        downloadLoading: false,
        name: '',
        instituteList: this.$store.state.user.institute,
        gradeList: this.$store.state.user.grade,
        userTeacherInfoEntities: this.$store.state.userTeacherInfoEntities,
        roleLists: this.$store.state.user.roleIdList,
        stationList: [],
        teacherSet: '',
        teacherList: [],
        awardList: [],
        staffList: [],
        reviewList: [],
        attachLists: [],
        awardLists: [],
        subList: [],
        perInfoList: [],
        sexList: [
          {value: 1, label: '男'}, {value: 2, label: '女'}
        ],
        teacherTitleList: this.$store.state.user.title,
        dataForm: {
          id: 0,
          name: '',
          teacherName: '',
          teacherSex: '',
          teacherUnit: '',
          teacherWorkStatus: '',
          teacherPhone: '',
          teacherJob: '',
          teacherInstinct: ''
        },
        entTeacherExperienceInfo: {},
        teacherInfo: {}
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
            url: this.$http.adornUrl(`/enterprise/teacher/experience/info/${this.dataForm.id}`),
            params: this.$http.adornParams({
              'teaExperienceId': this.dataForm.id
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.entTeacherExperienceInfo = data.entTeacherExperienceInfo
              this.teacherInfo = data.teacherInfo
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
          url: this.$httpFile.adornUrl(`/enterprise/teacher/attachment/download`),
          method: 'post',
          params: this.$httpFile.adornParams({
            'filePath': attach.teaAttachmentUrl
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
          link.setAttribute('download', attach.teaAttachmentName)
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
