<template>
  <div class="app-container">
    <div class="filter-container" style="margin-bottom: 20px;">
      <el-input v-model="listQuery.keyword" placeholder="输入用户名或昵称搜索" style="width: 200px; margin-right: 10px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        添加
      </el-button>
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="用户名" width="130" align="center">
        <template slot-scope="{row}">
          <span>{{ row.username }}</span>
        </template>
      </el-table-column>
      <el-table-column label="昵称" class-name="status-col" width="140" align="center">
        <template slot-scope="{row}">
          <span>{{ row.nickname }}</span>
        </template>
      </el-table-column>
      <el-table-column label="性别" class-name="status-col" width="80" align="center">
        <template slot-scope="{row}">
          <span>{{ row.sex }}</span>
        </template>
      </el-table-column>
      <el-table-column label="手机号" class-name="status-col" width="120" align="center">
        <template slot-scope="{row}">
          <span>{{ row.phone }}</span>
        </template>
      </el-table-column>
      <el-table-column label="邮件地址" class-name="status-col" width="170" align="center">
        <template slot-scope="{row}">
          <span>{{ row.email }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.disabled | statusFilter">
            {{ row.disabled === 0 ?'启用':'禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="160" align="center">
        <template slot-scope="{row}">
          <!-- | parseTime('{y}-{m}-{d} {h}:{i}')  -->
          <span>{{ row.createTimeStr }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="248" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button size="mini" type="success" @click="handleModifyPass(row)">
            修改密码
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--分页组件-->
    <pagination v-show="total" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="temp.username" :disabled="dialogStatus==='update'?true:false" />
        </el-form-item>
        <el-form-item label="密码" prop="password" :hidden="dialogStatus==='update'?true:false">
          <el-input v-model="temp.password" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="temp.sex" class="filter-item">
            <el-option v-for="item in sexOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" :hidden="dialogStatus==='create'?true:false">
          <el-select v-model="temp.disabled" class="filter-item">
            <el-option v-for="(item, index) in statusOptions" :key="index" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="temp.nickname" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="temp.phone" />
        </el-form-item>
        <el-form-item label="邮件地址">
          <el-input v-model="temp.email" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogPassVisible" title="修改密码">
      <el-form ref="dataPassForm" :rules="passRules" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item label="用户名" prop="username" required>
          <el-input v-model="temp.username" disabled />
        </el-form-item>
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input v-model="temp.oldPassword" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="temp.newPassword" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogPassVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="updatePass()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { add, del, edit, getList, updatePass } from '@/api/system/user'
  import Pagination from '@/components/Pagination'

  export default {
    name: 'Users',
    components: { Pagination },
    filters: {
      statusFilter(status) {
        const statusMap = {
          0: 'success',
          1: 'info'
          // deleted: 'danger'
        }
        return statusMap[status]
      }
    },
    data() {
      return {
        tableKey: 0,
        list: null,
        total: 0,
        listLoading: true,
        listQuery: {
          pageNum: 1,
          pageSize: 10
        },
        showReviewer: false,
        temp: {
          id: undefined,
          importance: 1,
          remark: '',
          timestamp: new Date(),
          title: '',
          type: '',
          status: ''
        },
        dialogFormVisible: false,
        dialogStatus: '',
        textMap: {
          update: '编辑',
          create: '添加'
        },
        dialogPassVisible: false,
        passData: [],
        statusOptions: [
          {
            value: 0,
            label: '启用'
          }, {
            value: 1,
            label: '禁用'
          }
        ],
        sexOptions: ['男', '女'],
        rules: {
          username: [{ required: true, message: 'username is required', trigger: 'change' }],
          password: [{ required: true, message: 'password is required', trigger: 'change' }]
        },
        passRules: {
          oldPassword: [{ required: true, message: 'oldPassword is required', trigger: 'change' }],
          newPassword: [{ required: true, message: 'newPassword is required', trigger: 'change' }]
        },
        downloadLoading: false
      }
    },
    created() {
      this.getList()
    },
    methods: {
      getList() {
        this.listLoading = true
        getList(this.listQuery).then(response => {
          const data = response.data
          this.list = data.list
          this.total = data.total
          this.pageNum = data.pageNum
          this.pageSize = data.pageSize
          this.listLoading = false
        })
      },
      handleFilter() {
        this.listQuery.pageNum = 1
        this.getList()
      },
      resetTemp() {
        this.temp = {
          id: undefined,
          username: '',
          password: '',
          phone: '',
          email: '',
          sex: '',
          disabled: '',
          nickname: ''
        }
      },
      handleCreate() {
        this.resetTemp()
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      createData() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            add(this.temp).then(() => {
              this.list.unshift(this.temp)
              this.dialogFormVisible = false
              this.handleFilter()
              this.$notify({
                title: '成功',
                message: '添加用户成功',
                type: 'success',
                duration: 2000
              })
            })
          }
        })
      },
      handleUpdate(row) {
        this.temp = Object.assign({}, row) // copy obj
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      updateData() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            const tempData = Object.assign({}, this.temp)
            edit(tempData).then(() => {
              const index = this.list.findIndex(v => v.id === this.temp.id)
              this.list.splice(index, 1, this.temp)
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
            })
          }
        })
      },
      handleModifyPass(row) {
        this.temp = Object.assign({}, row) // copy obj
        this.dialogPassVisible = true
        this.$nextTick(() => {
          this.$refs['dataPassForm'].clearValidate()
        })
      },
      updatePass() {
        this.$refs['dataPassForm'].validate((valid) => {
          if (valid) {
            const tempData = Object.assign({}, this.temp)
            const data = {
              userId: tempData.id,
              username: tempData.username,
              oldPassword: tempData.oldPassword,
              newPassword: tempData.newPassword
            }
            updatePass(data).then(() => {
              const index = this.list.findIndex(v => v.id === this.temp.id)
              this.list.splice(index, 1, this.temp)
              this.dialogPassVisible = false
              this.temp.oldPassword = ''
              this.temp.newPassword = ''
              this.$notify({
                title: '成功',
                message: '修改密码成功',
                type: 'success',
                duration: 2000
              })
            })
          }
        })
      },
      handleDelete(row) {
        this.$confirm('此操作将永久删除数据，是否继续？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
            del(row.username).then(() => {
              this.dialogFormVisible = false
              this.handleFilter()
              this.$notify({
                title: '成功',
                message: '删除成功',
                type: 'success',
                duration: 2000
              })
            })
          }
        )
      }
    }
  }
</script>
