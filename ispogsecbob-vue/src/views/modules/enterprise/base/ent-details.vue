<template>
  <el-dialog
    :title="'企业详情'"
    :modal-append-to-body='false'
    width="60%"
    :visible.sync="visible">
    <el-row>
      <table border="1" align="center" cellpadding="10" width="100%">
        <tr>
          <th>企业名称:</th>
          <td colspan="5"><span v-text="dataForm.entName" align="center"></span></td>
        </tr>
        <tr>
          <th>公司地址:</th>
          <td colspan="5"><span v-text="dataForm.entAddress" align="center"></span></td>
        </tr>
        <tr>
          <th>入驻时间:</th>
          <td colspan="5"><span v-text="dataForm.entInTime" align="center"></span></td>
        </tr>
        <tr>
          <th>是否高新区:</th>
          <td colspan="5"><span v-text="dataForm.newHighZones" align="center"></span></td>
        </tr>
        <tr>
          <th height="160">企业简介</th>
          <td colspan="5"><span v-text="dataForm.entIntroduce" align="center"></span></td>
        </tr>
        <tr>
          <th colspan="6">工商信息</th>
        </tr>

        <tr>
          <th>法人代表:</th>
          <td><span v-text="dataForm.entCorporate" align="center"></span></td>
          <th>注册资本:</th>
          <td><span v-text="dataForm.entRegister" align="center"></span></td>
          <th>成立时间:</th>
          <td><span v-text="dataForm.entFoundingTime" align="center"></span></td>
        </tr>

        <tr>
          <th>企业类型:</th>
          <td><span v-text="dataForm.entType" align="center"></span></td>
          <th>经营状态:</th>
          <td><span v-text="dataForm.entStatus" align="center"></span></td>
          <th>统一信用代码:</th>
          <td><span v-text="dataForm.entCode" align="center"></span></td>
        </tr>

        <tr>
          <th>注册地址:</th>
          <td colspan="5"><span v-text="dataForm.entRegister" align="center"></span></td>
        </tr>

        <tr>
          <th height="160">经营范围:</th>
          <td colspan="5"><span v-text="dataForm.entBusiness" align="center"></span></td>
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
        dataForm: {
          entInfoId: 0,
          entName: '',
          entCorporate: '',
          entBusiness: '',
          entCode: '',
          entRegister: '',
          entFoundingTime: '',
          entType: '',
          entStatus: '',
          entRegisterAddress: '',
          entAddress: '',
          entIntroduce: '',
          newHighZones: '',
          entInTime: '',
          inApply: '1'
        }
      }
    },
    methods: {
      init (id, hasApply) {
        this.visible = true
        this.dataForm.entInfoId = id || 0
        this.dataForm.inApply = hasApply || 1
        console.log(id + hasApply)
        if (this.dataForm.entInfoId) {
          this.$http({
            url: this.$http.adornUrl(`/enterprise/info/info/${this.dataForm.entInfoId}/${this.dataForm.inApply}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            console.log(this.dataForm.entInfoId)
            console.log(this.dataForm.inApply)
            if (data && data.code === 0) {
              this.dataForm.entName = data.data.entName
              this.dataForm.entCorporate = data.data.entCorporate
              this.dataForm.entBusiness = data.data.entBusiness
              this.dataForm.entCode = data.data.entCode
              this.dataForm.entRegister = data.data.entRegister
              this.dataForm.entFoundingTime = new Date(data.data.entFoundingTime)
              this.dataForm.entType = data.data.entType
              this.dataForm.entStatus = data.data.entStatus
              this.dataForm.entRegisterAddress = data.data.entRegisterAddress
              this.dataForm.entAddress = data.data.entAddress
              this.dataForm.entIntroduce = data.data.entIntroduce
              this.dataForm.newHighZones = data.data.newHighZones
              this.dataForm.entInTime = new Date(data.data.entInTime)
              this.dataForm.inApply = data.data.inApply
            }
          })
        }
      }
    }
  }
</script>
