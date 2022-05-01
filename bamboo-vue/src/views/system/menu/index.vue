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
      class="menu-tree"
      style="width: 100%; margin-top: 20px"
    >
      <!-- meta元素 -->
      <el-table-column label="菜单标题">
        <template slot-scope="{row}">
          <svg-icon :icon-class="row.icon" />
          <span style="margin-left: 16px">{{ row.title }}</span>
        </template>
      </el-table-column>

      <!-- 菜单节点元素 -->
      <!-- <el-table-column prop="menuName" label="名称" /> -->
      <el-table-column prop="menuPath" label="菜单路径" />
      <el-table-column prop="redirect" label="菜单跳转" />
      <el-table-column prop="component" label="组件映射" align="center" />
      <el-table-column label="隐藏" align="center" width="120">
        <template slot-scope="{row}">
          <el-tag :type="row.isHidden | statusFilter">
            {{ row.isHidden === '1' ?'YES':'NO' }}
          </el-tag>
        </template>
      </el-table-column>

      <!-- 菜单列表排序 -->
      <el-table-column prop="sort" label="排序" align="center" width="100" />

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
        <el-form-item v-if="dialogStatus === 'update'" label="菜单隐藏">
          <el-select v-model="temp.isHidden" size="small" class="filter-item">
            <el-option v-for="(item, index) in statusOptions" :key="index" :label="item.label" :value="item.value" />
          </el-select>
          <!-- <el-input v-model="temp.isHidden" size="small" autocomplete="off" placeholder="请输入菜单隐藏" /> -->
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
import { getMenus, getChildKeys, addMenu, deleteMenu, updateMenu } from '@/api/system/menu'

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
      statusOptions: [
        {
          value: '0',
          label: 'NO'
        }, {
          value: '1',
          label: 'YES'
        }
      ],
      dialogStatus: '',
      dialogFormVisible: false,
      temp: {},
      isResouceShow: 1,
      sonStatus: false,
      casArr: [],
      idx: '',
      rules: {
        location: [{ required: true, message: '请选择层级', trigger: 'blur' }],
        children: [{ required: true, message: '请选择子级位置', trigger: 'blur' }],
        menuPath: [{ required: true, message: '请输入菜单路径', trigger: 'blur' }],
        component: [{ required: true, message: '请输入组件映射', trigger: 'blur' }],
        title: [{ required: true, message: '请输入菜单标题', trigger: 'blur' }],
        icon: [{ required: true, message: '请输入菜单图标', trigger: 'blur' }]
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
            obj.parentId = 0
            this.tableData.push(obj)
          } else { // 子级菜单
            obj.parentId = this.casArr[this.casArr.length - 1]
            // 获取当前子级节点位置下的集合
            const currentArr = this.find(this.tableData, 0)
            currentArr.push(obj)
          }
          // 新增菜单
          addMenu(obj).then(response => {
            if (response.code === 20000) {
              this.dialogFormVisible = false
              this.$confirm('菜单新增成功，是否刷新即刻生效？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                location.reload()
              })
            }
          })
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
    // 递归表格删除子级数据(删除，最少两个层级)
    findDel(arr, i, item, childKeys) {
      let index
      for (let j = 0; j < arr.length; j++) {
        if (arr[j].id === childKeys[i]) {
          index = j
          break
        }
      }
      if (i === (childKeys.length - 2)) {
        arr[index].children.forEach((it, ix, arrs) => {
          if (it === item) {
            return arrs.splice(ix, 1)
          }
        })
      } else {
        return this.findDel(arr[index].children, (i += 1), item, childKeys)
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
          this.toDelete(item)
        }
      }).catch((err) => { console.log(err) })
    },
    async toDelete(item) {
      const res = await deleteMenu(item.id)
      if (res && res.code === 20000) {
        if (item.parentId === 0) { // 根结点
          this.deleteRootNode(item)
        } else { // 子节点
          // 获取childKeys
          const resChildKeys = await getChildKeys(item.parentId)
          const childKeys = resChildKeys.data
          childKeys.push(item.id)
          this.findDel(this.tableData, 0, item, childKeys)
        }
        this.$message({
          type: 'success',
          message: '删除成功!'
        })
      }
    },
     findIndex(arr, id) {
      for (let index = 0; index < arr.length; index++) {
        if (arr[index].id === id) {
          return index
        }
      }
    },
    // 打开更新
    handleUpdate(row) {
      row.parentId !== 0 ? (this.sonStatus = true) : (this.sonStatus = false)
      this.temp = Object.assign({}, row) // copy obj
      this.idx = this.findIndex(this.tableData, row.id)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
      })
    },
    // 递归表格修改子级节点数据(编辑)
    findSd(arr, i, childKeys) {
      let index
      for (let j = 0; j < arr.length; j++) {
        if (arr[j].id === childKeys[i]) {
          index = j
          break
        }
      }
      if (i === (childKeys.length - 1)) {
        return arr.splice(index, 1, this.temp)
      } else {
        return this.findSd(arr[index].children, (i += 1), childKeys)
      }
    },
    // 更新
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.toUpdate(this.temp)
        } else {
          return false
        }
      })
    },
    async toUpdate(temp) {
      const res = await updateMenu(temp)
      if (res && res.code === 20000) {
        if (temp.parentId === 0) { // 根节点
          this.tableData.splice(this.idx, 1, temp)
        } else {
          // 获取childKeys
          const resChildKeys = await getChildKeys(temp.parentId)
          const childKeys = resChildKeys.data
          childKeys.push(temp.id)
          this.findSd(this.tableData, 0, childKeys)
        }
        this.$message({
          type: 'success',
          message: '编辑成功',
          duration: 2000
        })
        this.dialogFormVisible = false
      }
    }
  }
}
</script>
