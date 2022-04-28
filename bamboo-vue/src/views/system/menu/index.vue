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
      <el-table-column prop="icon" label="图标" align="center" />

      <!-- 菜单节点元素 -->
      <!-- <el-table-column prop="menuName" label="名称" /> -->
      <el-table-column prop="menuPath" label="路径" />
      <el-table-column prop="redirect" label="跳转" />
      <el-table-column prop="component" label="组件映射" align="center" />
      <el-table-column label="隐藏" align="center" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.isHidden | statusFilter">
            {{ row.isHidden === '1' ?'YES':'NO' }}
          </el-tag>
        </template>
      </el-table-column>

      <!-- 菜单列表排序 -->
      <el-table-column prop="sort" label="排序" align="center" width="80" />

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
            :options="tableData"
            :props="{ checkStrictly: true, label: 'title', value: 'id', children: 'children' }"
            size="small"
            clearable
            @change="getCasVal"
          />
        </el-form-item>
        <el-form-item label="菜单标题" prop="title">
          <el-input v-model="temp.title" size="small" autocomplete="off" placeholder="请输入菜单标题" />
        </el-form-item>
        <el-form-item label="菜单路径" prop="menuPath">
          <el-input v-model="temp.menuPath" size="small" autocomplete="off" placeholder="请输入菜单路径" />
        </el-form-item>
        <el-form-item label="组件映射" prop="component">
          <el-input v-model="temp.component" size="small" autocomplete="off" placeholder="请输入组件映射" />
        </el-form-item>

        <el-form-item label="菜单图标" prop="icon">
          <el-input v-model="temp.icon" size="small" autocomplete="off" placeholder="请输入菜单图标" />
        </el-form-item>
        <el-form-item v-if="!sonStatus" label="菜单跳转" prop="redirect">
          <el-input v-model="temp.redirect" size="small" autocomplete="off" placeholder="顶级菜单需要" />
        </el-form-item>

        <!-- 编辑面板专用 -->
        <el-form-item v-if="dialogStatus === 'update'" label="菜单隐藏" prop="isHidden">
          <el-input v-model="temp.isHidden" size="small" autocomplete="off" placeholder="请输入菜单隐藏" />
        </el-form-item>
        <el-form-item v-if="dialogStatus === 'update'" label="菜单排序" prop="sort">
          <el-input v-model="temp.sort" size="small" autocomplete="off" placeholder="请输入菜单排序" />
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
  filters: {
    statusFilter(status) {
      if (status === null) {
        return 'success'
      } else {
        const statusMap = {
          0: 'success',
          1: 'info'
        }
        return statusMap[status]
      }
    }
  },
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
        children: [{ required: true, message: '请选择子级位置', trigger: 'blur' }],
        menuPath: [{ required: true, message: '请输入菜单路径', trigger: 'blur' }],
        component: [{ required: true, message: '请输入组件映射', trigger: 'blur' }],
        title: [{ required: true, message: '请输入菜单标题', trigger: 'blur' }]
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
    // 是否显示子级位置(初始化添加面板)
    locationChange(v) {
      if (v === '2') {
        this.sonStatus = true
      } else {
        this.sonStatus = false
      }
    },
    // 获取子级位置(子级位置选择变更事件)
    getCasVal(v) {
      this.casArr = v
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
    // 递归表格数据(添加)
    find(arr, i) {
      let currVal
      for (let j = 0; j < arr.length; j++) {
        if (arr[j].id === this.casArr[i]) {
          currVal = j
          break
        }
      }
      if (i === (this.casArr.length - 1)) {
        return arr[currVal].children
      } else {
        return this.find(arr[currVal].children, (i += 1))
      }
    },
    // 添加
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          delete this.temp.children
          const obj = Object.assign({}, this.temp)
          obj.children = []
          obj.id = new Date().getTime() // 添加树时需要，此处使用时间戳来确保树节点唯一
          if (this.sonStatus === false) { // 顶级菜单
            // 新增节点自动排在末尾
            this.temp.sort = String(this.tableData.length) // 后续考虑放到后端统计生成
            obj.parentId = 0
            this.tableData.push(obj)
          } else { // 子级菜单
            // obj.parentId = this.findParent(this.tableData, 0, this.casArr).id
            obj.parentId = this.casArr[this.casArr.length - 1]
            // 获取当前子级节点位置下的集合
            const currentArr = this.find(this.tableData, 0)
            currentArr.push(obj)
          }
          // debugger
          // TODO 新增菜单
          // await createMenu(obj)
          this.$message({
            type: 'success',
            message: '新增菜单成功',
            duration: 2000
          })
          this.dialogFormVisible = false
        } else {
          return false
        }
      })
    },
    // 删除根节点
    deleteRootNode(item) {
      this.tableData.forEach((it, ix, arrs) => {
        if (it === item) {
          return arrs.splice(ix, 1)
        }
      })
    },
    // 递归表格删除数据(删除)
    findDel(arr, i, item) {
      // const casArr = item.childKey
      // if (i === casArr.length - 2) {
      //   const index = casArr[i].substr(casArr[i].length - 1, 1)
      //   arr[index].children.forEach((it, ix, arrs) => {
      //     if (it === item) {
      //       return arrs.splice(ix, 1)
      //     }
      //   })
      // } else {
      //   return this.findDel(arr[casArr[i].substr(casArr[i].length - 1, 1)].children, (i += 1), item)
      // }
      // 第二层级子节点删除
      for (let j = 0; j < arr.length; j++) {
        arr[j].children.forEach((it, ix, arrs) => {
          if (it === item) {
            return arrs.splice(ix, 1)
          }
        })
      }
    },
    // 删除节点
    deleteClick(item) {
      this.$confirm(`此操作将删除该菜单, 是否继续?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (item.children.length !== 0) {
          this.$message.warning({
            message: '请删除子节点',
            duration: 1000
          })
        } else {
          ++this.isResouceShow
          if (item.parentId === 0) { // 根结点
            this.deleteRootNode(item)
          } else { // 子节点
            this.findDel(this.tableData, 0, item)
          }
          // debugger
          // TODO 删除节点
          // await deleteMenu(item)
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
        }
      }).catch((err) => { console.log(err) })
    },
    // 打开更新
    handleUpdate(row) {
      row.parentId !== 0 ? (this.sonStatus = true) : (this.sonStatus = false)
      this.temp = Object.assign({}, row) // copy obj
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
      })
    },

    // 同步子名称
    useChildLable(arr) {
      if (arr !== []) {
        arr.forEach((item) => {
          item.parent = this.temp.title
        })
      }
    },
    // 递归表格数据(编辑)
    findSd(arr, i, casArr) {
      if (i === casArr.length - 1) {
        const index = casArr[i].substr(casArr[i].length - 1, 1)
        return arr.splice(index, 1, this.temp)
      } else {
        return this.findSd(
          arr[casArr[i].substr(casArr[i].length - 1, 1)].children, (i += 1), casArr
        )
      }
    },
    // 递归寻找同步名称
    findLable(arr, i, casArr) {
      if (i === casArr.length - 1) {
        const index = casArr[i].substr(casArr[i].length - 1, 1)
        return arr[index]
      } else {
        return this.findLable(
          arr[casArr[i].substr(casArr[i].length - 1, 1)].children,
          (i += 1),
          casArr
        )
      }
    },
    // 更新
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.temp.location === '1') {
            this.tableData.splice(this.idx, 1, this.temp)
            this.useChildLable(this.tableData[this.idx].children)
          } else {
            this.findSd(this.tableData, 0, this.childKey)
            this.useChildLable(this.findLable(this.tableData, 0, this.childKey).children)
          }
          // TODO 编辑节点
          // await updateMenu(item)
          this.$message({
            type: 'success',
            message: '编辑成功'
          })
          this.dialogFormVisible = false
        } else {
          return false
        }
      })
    },
    // 查找父级(添加)
    findParent(arr, i, casArr) {
      let currVal
      for (let j = 0; j < arr.length; j++) {
        if (arr[j].id === casArr[i]) {
          currVal = j
          break
        }
      }
      if (i === (casArr.length - 1)) {
        return arr[currVal]
      } else {
        return this.findParent(arr[currVal].children, (i += 1), this.casArr)
      }
    }
  }
}
</script>
