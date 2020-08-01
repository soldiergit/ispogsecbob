<template>
  <div class="mod-user">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
    <el-form-item>
      <el-date-picker
        v-model="dataForm.matchTime"
        align="right"
        type="year"
        placeholder="请选择年度">
      </el-date-picker>
    </el-form-item>
    <el-form-item>
      <el-input v-model="dataForm.recruitmentPost" placeholder="招聘岗位" clearable></el-input>
    </el-form-item>
    <el-form-item>
      <el-button @click="getDataList()">查询</el-button>
      <el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>
    </el-form-item>
  </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      :default-sort="{prop: 'recruitmentInfoId', order: 'ascending'}"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
      <el-table-column
        sortable
        prop="entEnterpriseInfo.entName"
        header-align="center"
        align="center"
        label="企业名称">
      </el-table-column>
      <el-table-column
        sortable
        prop="recruitmentPost"
        header-align="center"
        align="center"
        label="招聘岗位">
      </el-table-column>
      <el-table-column
        sortable
        prop="recruitmentPeopleNumber"
        header-align="center"
        align="center"
        width="180"
        label="招聘人数">
      </el-table-column>
      <el-table-column
        sortable
        prop="recruitmentSpecialty"
        header-align="center"
        align="center"
        label="招聘专业">
      </el-table-column>
      <el-table-column
        sortable
        prop="entEnterpriseInfo.newHighZones"
        header-align="center"
        align="center"
        width="180"
        :formatter="formatterZone"
        label="是否高新区">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="110"
        label="操作">
        <template slot-scope="scope">
          <!-- isAuth('enterprise:info:shenhe') -->
          <el-button v-if="true" type="text" size="small" @click="detailHandle(scope.row.recruitmentInfoId)">详情</el-button>
          <el-button v-if="scope.row.inApply == '1'" type="text" size="small" @click="deleteHandle(scope.row.recruitmentInfoId)">删除</el-button>
          <el-button v-else type="text" size="small">无操作</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[5, 10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <ent-recuitment-details v-if="shenhe" ref="details"/>
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
  import AddOrUpdate from './recruitment-add-or-update'
  import EntRecuitmentDetails from '../recruitment/recruitment-details'
  export default {
    data () {
      return {
        hasApply: '1',
        dataForm: {
          baseId: '',
          recruitmentPost: '',
          matchTime: new Date(),
          idDel: 0
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        addOrUpdateVisible: false,
        shenhe: false,
        dataListSelections: []
      }
    },
    components: {
      AddOrUpdate,
      EntRecuitmentDetails
    },
    activated () {
      this.getDataList()
    },
    methods: {
      // 格式化区域显示
      formatterZone (row, column, cellValue) {
        return cellValue === '0' ? '是' : '否'
      },
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/enterprise/recruitment/info/list'),
          method: 'get',
          params: this.$http.adornParams({
            'currPage': this.pageIndex,
            'pageSize': this.pageSize,
            'inApply': this.hasApply,
            'recruitmentPost': this.dataForm.recruitmentPost
          })
        }).then(({data}) => {
          console.log(data)
          if (data.code === 500) {
            this.$message.error(data.msg)
          }
          if (data && data.code === 0) {
            this.dataList = data.page.list
            this.totalPage = data.page.totalCount
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
        })
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
        // 详情
      detailHandle (id) {
        console.log(id)
        this.shenhe = true
        this.$nextTick(() => {
          this.$refs.details.init(id)
        })
      },
      //  每页数
      sizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getDataList()
      },
      // 当前页
      currentChangeHandle (val) {
        this.pageIndex = val
        this.getDataList()
      },
      // 多选
      selectionChangeHandle (val) {
        this.dataListSelections = val
      },
      // 删除
      deleteHandle (id) {
        let canDelete = true
        let recruitmentInfoIds = id ? [id] : this.dataListSelections.map(item => {
          if (item.inApply === '1') {
            canDelete = false
          } else {
            canDelete = false
          }
          return item.recruitmentInfoId
        })
        this.$confirm(`确定要进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          if (canDelete) {
            this.$http({
              url: this.$http.adornUrl('/enterprise/recruitment/info/delete'),
              method: 'post',
              data: this.$http.adornData(recruitmentInfoIds, false)
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.getDataList()
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          } else {
            this.$message({
              message: '包含不可删除招聘信息',
              type: 'error',
              duration: 1500,
              onClose: () => {
                this.getDataList()
              }
            })
          }
        }).catch(() => {
        })
      }
    }
  }
</script>
<style>
  .el-step__title {
    font-size: 12px;
    line-height: 28px;
  }

  .el-table__expanded-cell[class*=cell] {
    padding: 5px 5px;
  }
</style>
