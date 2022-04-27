<template>
  <div class="app-container">
    <!-- 按钮尺寸：mini/small/medium -->
    <el-button type="primary" icon="el-icon-circle-plus-outline" @click="handleCreate">添加</el-button>
    <el-table
      :data="tableData"
      row-key="id"
      :tree-props="defaultProps"
      default-expand-all
      border
      stripe
      class="menu-tree"
      style="width: 100%; margin-top: 20px"
    >
      <!-- meta元素 -->
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="icon" label="图标" />

      <!-- 菜单节点元素 -->
      <el-table-column prop="menuName" label="名称" />
      <el-table-column prop="menuPath" label="路径" />
      <el-table-column prop="redirect" label="跳转" />
      <el-table-column prop="component" label="组件映射" />
      <el-table-column prop="hidden" label="隐藏" />

      <!-- 菜单列表排序 -->
      <el-table-column prop="sort" label="排序" />

      <!-- 树节点操作 -->
      <el-table-column label="操作" :align="alignDir" width="220">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handleUpdate(scope.row)">
            <i class="el-icon-edit" /> 编辑
          </el-button>
          <el-button type="danger" size="small" @click="deleteClick(scope.row)">
            <i class="el-icon-delete" /> 删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px" style="width: 400px; margin-left: 50px">
        <el-form-item v-if="dialogStatus !== 'update'" label="菜单层级" prop="location">
          <el-select v-model="temp.location" placeholder="请选择层级" size="small" @change="locationChange">
            <el-option v-for="item in locationData" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="sonStatus && dialogStatus !== 'update'" label="子级位置" prop="children">
          <el-cascader
            :key="isResouceShow"
            v-model="temp.children"
            placeholder="请选择子级位置"
            :label="'title'"
            :value="'id'"
            :options="tableData"
            :props="{ checkStrictly: true }"
            size="small"
            clearable
            @change="getCasVal"
          />
        </el-form-item>
        <el-form-item label="菜单标题" prop="label">
          <el-input v-model="temp.label" size="small" autocomplete="off" placeholder="请输入标签名称" />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer" size="small">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getMenus } from '@/api/system/menu'

export default {
  data() {
    return {
      tableData: [],
      checkStrictly: false,
      defaultProps: {
        children: 'children'
      },
      alignDir: 'center',
      textMap: {
        update: '编辑',
        create: '新增菜单'
      },
      dialogStatus: '',
      dialogFormVisible: false,
      temp: {},
      isResouceShow: 1,
      sonStatus: false,
      casArr: [],
      idx: '',
      childKey: [],
      rules: {
        location: [{ required: true, message: '请选择层级', trigger: 'blur' }],
        label: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        children: [{ required: true, message: '请选择子级位置', trigger: 'blur' }]
      },
      locationData: [
        { id: '1', name: '顶级' },
        { id: '2', name: '子级' }
      ]
    }
  },
  computed: {
    menuTreeData() {
      return this.tableData
    }
  },
  created() {
    this.getMenus()
  },
  methods: {
    // 获取菜单tree数据源
    async getMenus() {
      const res = await getMenus()
      this.tableData = res.data
    },
    // 是否显示子级位置
    locationChange(v) {
      if (v == 2) {
        this.sonStatus = true;
      } else {
        this.sonStatus = false;
      }
    },
    // 获取子级位置
    getCasVal(v) {
      this.casArr = v;
    },
    // 临时容器置空
    resetTemp() {
      this.temp = {}
    },
    // 打开添加
    handleCreate() {
      this.resetTemp()
      this.dialogFormVisible = true
      this.dialogStatus = 'create'
      this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
      })
    },

    // 新增菜单
      createData() {
          this.$refs['dataForm'].validate((valid) => {
              if (valid) {
                  if (this.sonStatus == false) {
                      this.temp.value = String(this.tableData.length)
                      const obj = Object.assign({}, this.temp)
                      obj.children = []
                      obj.parent = ''
                      this.tableData.push(obj)
                      this.$message({
                          type: 'success',
                          message: '添加成功'
                      })
                      this.dialogFormVisible = false;
                  } else {
                      let arr = this.find(this.tableData, 0);
                      this.temp.value =
                          String(this.casArr[this.casArr.length - 1]) +
                          '-' +
                          String(arr.length)
                      delete this.temp.children

                      const obj = Object.assign({}, this.temp)
                      obj.children = []
                      obj.childKey = [...this.casArr, String(arr.length)]
                      obj.parent = this.findTable(
                          this.tableData,
                          0,
                          this.casArr
                      ).label;
                      if (this.temp.location === '2') {
                          obj.location = String(
                              [...this.casArr, String(arr.length)].length
                          )
                      }
                      arr.push(obj);
                      this.$message({
                          type: 'success',
                          message: '添加成功',
                      })
                      this.dialogFormVisible = false;
                  }
              } else {
                  return false;
              }
          })
      },
    // 删除节点
    deleteClick(item) {
      this.$confirm(`此操作将删除该菜单, 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
      }).then(() => {
          if (item.children.length != 0) {
              this.$message.warning({
                  message: '请删除子节点',
                  duration: 1000,
              })
          } else {
              ++this.isResouceShow;
              if (item.value.length == 1) {
                  this.deleteParent(item)
                  this.$message({
                      type: 'success',
                      message: '删除成功',
                  });
              } else {
                  this.findDel(this.tableData, 0, item)
                  this.$message({
                      type: 'success',
                      message: '删除成功',
                  });
              }
          }
        }).catch((err) => {
            console.log(err);
            this.$message({
                type: 'info',
                message: '已取消删除',
            })
        })
      },
      // 删除父级节点
      deleteParent(item) {
          this.tableData.forEach((it, ix, arrs) => {
              if (it == item) {
                  return arrs.splice(ix, 1)
              }
          })
      },
      // 递归寻找同级
      findSameTable(arr, i, casArr) {
          if (i == casArr.length - 1) {
              return arr
          } else {
              return this.findTable(
                  arr[casArr[i].substr(casArr[i].length - 1, 1)].children,
                  (i += 1),
                  casArr
              )
          }
      },
      // 寻找父级
      findTable(arr, i, casArr) {
          if (i == casArr.length - 1) {
              let index = casArr[i].substr(casArr[i].length - 1, 1)
              return arr[index]
          } else {
              return this.findTable(
                  arr[casArr[i].substr(casArr[i].length - 1, 1)].children,
                  (i += 1),
                  casArr
              )
          }
      },
      // 递归表格数据(添加)
      find(arr, i) {
          if (i == this.casArr.length - 1) {
              return arr[this.casArr[i].substr(this.casArr[i].length - 1, 1)]
                  .children
          } else {
              return this.find(
                  arr[this.casArr[i].substr(this.casArr[i].length - 1, 1)]
                      .children,
                  (i += 1)
              )
          }
      },
      // 递归表格数据(编辑)
      findSd(arr, i, casArr) {
          if (i == casArr.length - 1) {
              let index = casArr[i].substr(casArr[i].length - 1, 1)
              return arr.splice(index, 1, this.temp)
          } else {
              return this.findSd(
                  arr[casArr[i].substr(casArr[i].length - 1, 1)].children,
                  (i += 1),
                  casArr
              )
          }
      },
      // 递归寻找同步名称
      findLable(arr, i, casArr) {
          if (i == casArr.length - 1) {
              let index = casArr[i].substr(casArr[i].length - 1, 1);
              return arr[index];
          } else {
              return this.findLable(
                  arr[casArr[i].substr(casArr[i].length - 1, 1)].children,
                  (i += 1),
                  casArr
              )
          }
      },
      // 同步子名称
      useChildLable(arr) {
          if (arr !== []) {
              arr.forEach((item) => {
                  item.parent = this.temp.label
              })
          }
      },
      // 递归表格数据(删除)
      findDel(arr, i, item) {
          let casArr = item.childKey
          if (i == casArr.length - 2) {
              let index = casArr[i].substr(casArr[i].length - 1, 1)
              arr[index].children.forEach((it, ix, arrs) => {
                  if (it === item) {
                      return arrs.splice(ix, 1)
                  }
              })
          } else {
              return this.findDel(
                  arr[casArr[i].substr(casArr[i].length - 1, 1)].children,
                  (i += 1),
                  item
              )
          }
      },
    // 打开更新
      handleUpdate(row) {
          console.log(row)
          row.value.length !== 1
              ? (this.sonStatus = true)
              : (this.sonStatus = false)
          this.temp = Object.assign({}, row) // copy obj
          if (row.childKey) {
              this.childKey = row.childKey
              this.idx = row.childKey[row.childKey.length - 1]
          } else {
              this.idx = row.value
          }
          console.log(this.idx)

          this.dialogStatus = 'update'
          this.dialogFormVisible = true
          this.$nextTick(() => {
              this.$refs['dataForm'].clearValidate()
          })
      },
    // 更新
      updateData() {
          this.$refs['dataForm'].validate((valid) => {
              if (valid) {
                  if (this.temp.location === '1') {
                      console.log(this.temp)
                      this.tableData.splice(this.idx, 1, this.temp)
                      this.useChildLable(this.tableData[this.idx].children)
                      this.$message({
                          type: 'success',
                          message: '编辑成功'
                      })
                      this.dialogFormVisible = false
                  } else {
                      this.findSd(this.tableData, 0, this.childKey)
                      this.useChildLable(
                          this.findLable(this.tableData, 0, this.childKey)
                              .children
                      )
                      this.$message({
                          type: 'success',
                          message: '编辑成功'
                      })
                      this.dialogFormVisible = false
                  }
              } else {
                  return false
              }
          })
      }
  }
}
</script>
