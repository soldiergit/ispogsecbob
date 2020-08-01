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
            项目基本信息
          </th>
        </tr>
        <tr align='center'>
          <td colspan="10" style="height: 1.2rem"></td>
        </tr>
        <tr align='center' style="height: 2.5rem">
          <th colspan="2">申报项目名称</th>
          <td colspan="3"><span style="font-size: 1.1rem; font-weight: bolder" v-text="project.proName" align="center"></span></td>
          <th colspan="2">项目类型</th>
          <td colspan="3" v-for="item in proTypeList"
              :key="item.value"
              v-if="item.value === project.proType">
            <span v-text="item.label" align="center"></span>
          </td>
        </tr>
        <tr align='center' style="height: 2.5rem">
          <th colspan="2">项目经费</th>
          <td colspan="3"><span  v-text="project.proOutlay" align="center"></span></td>
          <!--</tr>-->
          <!--<tr align='center' style="height: 2.5rem">-->
          <th colspan="2">项目来源</th>
          <td colspan="3"><span  v-text="project.proOrigin" align="center"></span></td>
          <!--</tr>-->
        <tr align='center'>
          <th colspan="2">项目介绍</th>
          <td colspan="8">
            <span v-text="project.proIntroduce" align="center"></span>
          </td>
        </tr>

        <tr align='center'>
          <td colspan="10" style="height: 1.2rem"></td>
        </tr>
        <tr class="contents" align="center">
          <th colspan="10">
            项目成果信息
          </th>
        </tr>
        <tr align='center'>
          <td colspan="10" style="height: 1.2rem"></td>
        </tr>
        <tr align='center'>
          <th colspan="2">成果内容</th>
          <td colspan="8">
            <span v-text="ProjectAchievementInfo.proAchievementContent" align="center"></span>
          </td>
        </tr>

        <!-- 合作附件开始 -->
        <template v-if="entProjectAttachEntities.length>0">
          <tr align='center'>
            <td colspan="10" style="height: 1.2rem"></td>
          </tr>
          <tr align="center" class="contents">
            <th colspan="10">项目成果附件</th>
          </tr>
          <tr align='center'>
            <td colspan="10" style="height: 1.2rem"></td>
          </tr>
          <tr align='center'>
            <th colspan="7">附件名</th>
            <th colspan="3">操作</th>
          </tr>
          <tr align='center'>
            <td colspan="10" style="height: 1.2rem"></td>
          </tr>
          <template>
          <tr v-for="item in entProjectAttachEntities"
              align="center">
            <td colspan="7" v-text="item.attachName"></td>
            <td colspan="3"><el-button @click="attachDown(item)" :loading="downloadLoading">下载</el-button></td>
          </tr>
        </template>
        <tr align='center'>
          <td colspan="10" style="height: 1.2rem"></td>
        </tr>
      </template>
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
      dataListLoading: false,
      downloadLoading: false,
      hasType: 'userPerId',
      perInfo: {},
      TeacherInfo: {},
      sysUser: {},
      ent: {},
      Cooperation: '',
      instituteList: this.$store.state.user.institute,
      gradeList: this.$store.state.user.grade,
      teacherTitleList: this.$store.state.user.title,
      entProjectAttachEntities: [],
      sexList: [
        {value: 1, label: '男'}, {value: 2, label: '女'}
      ],
      proTypeList: [
        {value: 1, label: '科研项目'},
        {value: 2, label: '横向项目'},
        {value: 3, label: '企业项目'},
        {value: 4, label: '大创项目'},
        {value: 5, label: '企业招聘'},
        {value: 6, label: '实习项目对接'}
      ],
      project: '',
      proCooperationInfoId: 0,
      proAchievementId: '',
      ProjectAchievementInfo: [],
      dataForm: {
      }
    }
  },
  methods: {
    init (id, hasType) {
      this.proAchievementId = id || 0
      this.visible = true
      this.hasType = hasType
      this.$nextTick(() => {
        if (this.proAchievementId) {
          this.$http({
            url: this.$http.adornUrl(`/enterprise/project/achievement/info/${this.proAchievementId}/${this.hasType}`),
            params: this.$http.adornParams()
          }).then(({data}) => {
            console.log(data)
            if (data && data.code === 0) {
              this.ProjectAchievementInfo = data.entProjectAchievementInfo
              this.project = data.entProjectAchievementInfo.entProjectInfo
              this.entProjectAttachEntities = data.entProjectAchievementInfo.entProjectInfo.entProjectAttachEntities
            }
          })
        }
      })
    },
    closeDialog () {
      this.visible = false
      this.$emit('refreshDataList')
    },
    attachDown (attach) {
      this.downloadLoading = true
      this.$httpFile({
        url: this.$httpFile.adornUrl(`/enterprise/project/attach/download`),
        method: 'post',
        params: this.$httpFile.adornParams({
          'filePath': attach.url
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
        link.setAttribute('download', attach.attachName)
        document.body.appendChild(link)
        link.click()
        this.downloadLoading = false
      }).catch(err => {
        console.log(err)
        this.downloadLoading = false
      })
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
