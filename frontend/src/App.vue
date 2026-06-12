<template>
  <section v-if="!session.token" class="login-shell">
    <div class="login-visual">
      <div class="brand-lockup">
        <span class="brand-mark">T</span>
        <div>
          <strong>Tlias 教学管理平台</strong>
          <small>Training Lifecycle Administration</small>
        </div>
      </div>
      <div class="campus-panel">
        <div class="campus-grid">
          <span v-for="item in campusStats" :key="item.label">
            <strong>{{ item.value }}</strong>
            <em>{{ item.label }}</em>
          </span>
        </div>
      </div>
    </div>

    <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form" @keyup.enter="submitLogin">
      <h1>运营后台</h1>
      <p>统一管理员工、班级、学生档案、统计报表与操作日志。</p>
      <el-form-item prop="username">
        <el-input v-model="loginForm.username" size="large" placeholder="用户名" prefix-icon="User" />
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="loginForm.password" size="large" placeholder="密码" prefix-icon="Lock" show-password />
      </el-form-item>
      <el-button type="primary" size="large" :loading="state.loading" @click="submitLogin">登录系统</el-button>
    </el-form>
  </section>

  <el-container v-else class="app-shell">
    <el-aside width="252px" class="sidebar">
      <div class="brand-lockup sidebar-brand">
        <span class="brand-mark">T</span>
        <div>
          <strong>Tlias</strong>
          <small>教学管理</small>
        </div>
      </div>
      <el-menu :default-active="state.active" class="nav-menu" @select="selectView">
        <el-menu-item v-for="item in navItems" :key="item.key" :index="item.key">
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.label }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="topbar">
        <div>
          <h2>{{ currentView?.label }}</h2>
          <p>{{ currentView?.caption }}</p>
        </div>
        <div class="topbar-actions">
          <el-tag effect="plain" type="success">API {{ apiBaseLabel }}</el-tag>
          <el-dropdown>
            <el-button>
              <el-icon><User /></el-icon>
              {{ session.user?.name || session.user?.username || '管理员' }}
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="content" v-loading="state.loading">
        <section v-if="state.active === 'dashboard'" class="view-grid">
          <div class="metric-grid">
            <article v-for="metric in metrics" :key="metric.label" class="metric-card">
              <el-icon><component :is="metric.icon" /></el-icon>
              <span>{{ metric.label }}</span>
              <strong>{{ metric.value }}</strong>
            </article>
          </div>
          <div class="panel wide">
            <div class="panel-head">
              <h3>管理流程</h3>
              <el-button type="primary" @click="refreshAll">
                <el-icon><Refresh /></el-icon>
                刷新数据
              </el-button>
            </div>
            <el-steps :active="3" finish-status="success" align-center>
              <el-step title="身份验证" description="JWT 登录态" />
              <el-step title="档案维护" description="部门与员工" />
              <el-step title="教学编排" description="班级与学生" />
              <el-step title="数据复盘" description="报表与日志" />
            </el-steps>
          </div>
          <div class="panel">
            <div class="panel-head"><h3>员工岗位分布</h3></div>
            <div ref="jobChartRef" class="chart"></div>
          </div>
          <div class="panel">
            <div class="panel-head"><h3>班级人数排行</h3></div>
            <div ref="clazzChartRef" class="chart"></div>
          </div>
        </section>

        <section v-if="state.active === 'depts'" class="panel">
          <div class="panel-head">
            <h3>部门列表</h3>
            <el-button type="primary" @click="openDept()">新增部门</el-button>
          </div>
          <el-table :data="depts" stripe>
            <el-table-column prop="id" label="ID" width="90" />
            <el-table-column prop="name" label="部门名称" />
            <el-table-column prop="updateTime" label="更新时间" min-width="180" />
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="{ row }">
                <el-button link type="primary" @click="openDept(row)">编辑</el-button>
                <el-popconfirm title="确认删除该部门？" @confirm="removeDept(row.id)">
                  <template #reference><el-button link type="danger">删除</el-button></template>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
        </section>

        <section v-if="state.active === 'emps'" class="panel">
          <div class="panel-head">
            <h3>员工管理</h3>
            <el-button type="primary" @click="openEmp()">新增员工</el-button>
          </div>
          <el-form :inline="true" :model="empQuery" class="filter-row">
            <el-form-item label="姓名"><el-input v-model="empQuery.name" clearable /></el-form-item>
            <el-form-item label="性别">
              <el-select v-model="empQuery.gender" clearable style="width: 130px">
                <el-option label="男" :value="1" />
                <el-option label="女" :value="2" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button @click="loadEmps">查询</el-button>
              <el-button @click="resetEmpQuery">重置</el-button>
            </el-form-item>
          </el-form>
          <el-table :data="emps.rows" stripe>
            <el-table-column prop="name" label="姓名" min-width="120" />
            <el-table-column prop="username" label="用户名" min-width="120" />
            <el-table-column label="性别" width="80">
              <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
            </el-table-column>
            <el-table-column prop="phone" label="手机号" min-width="130" />
            <el-table-column label="职位" min-width="120">
              <template #default="{ row }">{{ jobText(row.job) }}</template>
            </el-table-column>
            <el-table-column prop="deptName" label="部门" min-width="120" />
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="{ row }">
                <el-button link type="primary" @click="openEmp(row)">编辑</el-button>
                <el-popconfirm title="确认删除该员工？" @confirm="removeEmp(row.id)">
                  <template #reference><el-button link type="danger">删除</el-button></template>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            v-model:current-page="empQuery.page"
            v-model:page-size="empQuery.pageSize"
            layout="total, sizes, prev, pager, next"
            :total="emps.total"
            @change="loadEmps"
          />
        </section>

        <section v-if="state.active === 'clazzs'" class="panel">
          <div class="panel-head">
            <h3>班级管理</h3>
            <el-button type="primary" @click="openClazz()">新增班级</el-button>
          </div>
          <el-form :inline="true" :model="clazzQuery" class="filter-row">
            <el-form-item label="班级"><el-input v-model="clazzQuery.name" clearable /></el-form-item>
            <el-form-item><el-button @click="loadClazzs">查询</el-button></el-form-item>
          </el-form>
          <el-table :data="clazzs.rows" stripe>
            <el-table-column prop="name" label="班级" min-width="160" />
            <el-table-column prop="room" label="教室" min-width="120" />
            <el-table-column prop="masterName" label="班主任" min-width="120" />
            <el-table-column prop="beginDate" label="开课" min-width="120" />
            <el-table-column prop="endDate" label="结课" min-width="120" />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === '在读' ? 'success' : row.status === '未开班' ? 'warning' : 'info'">{{ row.status }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="{ row }">
                <el-button link type="primary" @click="openClazz(row)">编辑</el-button>
                <el-popconfirm title="确认删除该班级？" @confirm="removeClazz(row.id)">
                  <template #reference><el-button link type="danger">删除</el-button></template>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            v-model:current-page="clazzQuery.page"
            v-model:page-size="clazzQuery.pageSize"
            layout="total, sizes, prev, pager, next"
            :total="clazzs.total"
            @change="loadClazzs"
          />
        </section>

        <section v-if="state.active === 'students'" class="panel">
          <div class="panel-head">
            <h3>学生管理</h3>
            <el-button type="primary" @click="openStudent()">新增学生</el-button>
          </div>
          <el-form :inline="true" :model="studentQuery" class="filter-row">
            <el-form-item label="姓名"><el-input v-model="studentQuery.name" clearable /></el-form-item>
            <el-form-item label="班级">
              <el-select v-model="studentQuery.clazzId" clearable filterable style="width: 180px">
                <el-option v-for="item in clazzOptions" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
            <el-form-item><el-button @click="loadStudents">查询</el-button></el-form-item>
          </el-form>
          <el-table :data="students.rows" stripe>
            <el-table-column prop="name" label="姓名" min-width="120" />
            <el-table-column prop="no" label="学号" min-width="120" />
            <el-table-column prop="phone" label="手机号" min-width="130" />
            <el-table-column prop="clazzName" label="班级" min-width="150" />
            <el-table-column prop="violationCount" label="违纪次数" width="100" />
            <el-table-column prop="violationScore" label="扣分" width="90" />
            <el-table-column label="操作" width="240" fixed="right">
              <template #default="{ row }">
                <el-button link type="primary" @click="openStudent(row)">编辑</el-button>
                <el-button link type="warning" @click="openViolation(row)">违纪</el-button>
                <el-popconfirm title="确认删除该学生？" @confirm="removeStudent(row.id)">
                  <template #reference><el-button link type="danger">删除</el-button></template>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            v-model:current-page="studentQuery.page"
            v-model:page-size="studentQuery.pageSize"
            layout="total, sizes, prev, pager, next"
            :total="students.total"
            @change="loadStudents"
          />
        </section>

        <section v-if="state.active === 'reports'" class="view-grid">
          <div class="panel"><div class="panel-head"><h3>员工性别统计</h3></div><div ref="genderChartRef" class="chart"></div></div>
          <div class="panel"><div class="panel-head"><h3>学生学历统计</h3></div><div ref="degreeChartRef" class="chart"></div></div>
          <div class="panel wide"><div class="panel-head"><h3>班级人数统计</h3></div><div ref="studentCountChartRef" class="chart chart-wide"></div></div>
        </section>

        <section v-if="state.active === 'logs'" class="panel">
          <div class="panel-head"><h3>操作日志</h3><el-button @click="loadLogs">刷新</el-button></div>
          <el-table :data="logs.rows" stripe>
            <el-table-column prop="operateEmpName" label="操作人" min-width="120" />
            <el-table-column prop="className" label="类名" min-width="240" show-overflow-tooltip />
            <el-table-column prop="methodName" label="方法" min-width="140" />
            <el-table-column prop="operateTime" label="时间" min-width="180" />
            <el-table-column prop="costTime" label="耗时(ms)" width="120" />
          </el-table>
          <el-pagination
            v-model:current-page="logQuery.page"
            v-model:page-size="logQuery.pageSize"
            layout="total, sizes, prev, pager, next"
            :total="logs.total"
            @change="loadLogs"
          />
        </section>

        <section v-if="state.active === 'upload'" class="panel upload-panel">
          <div class="panel-head"><h3>素材上传</h3></div>
          <el-upload drag :auto-upload="false" :limit="1" :on-change="handleFileChange" :on-remove="clearUpload">
            <el-icon class="upload-icon"><UploadFilled /></el-icon>
            <div class="el-upload__text">拖拽文件到此处，或点击选择</div>
          </el-upload>
          <el-button type="primary" :disabled="!uploadFile" :loading="state.loading" @click="submitUpload">上传到 OSS</el-button>
          <el-alert v-if="uploadUrl" title="上传完成" type="success" :closable="false">
            <template #default><a :href="uploadUrl" target="_blank">{{ uploadUrl }}</a></template>
          </el-alert>
        </section>
      </el-main>
    </el-container>

    <el-dialog v-model="dialogs.dept" :title="deptForm.id ? '编辑部门' : '新增部门'" width="420px">
      <el-form :model="deptForm" label-width="88px">
        <el-form-item label="部门名称"><el-input v-model="deptForm.name" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogs.dept=false">取消</el-button><el-button type="primary" @click="saveDept">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="dialogs.emp" :title="empForm.id ? '编辑员工' : '新增员工'" width="720px">
      <el-form :model="empForm" label-width="88px" class="form-grid">
        <el-form-item label="用户名"><el-input v-model="empForm.username" /></el-form-item>
        <el-form-item label="密码"><el-input v-model="empForm.password" show-password :placeholder="empForm.id ? '留空则不修改' : '默认 123456'" /></el-form-item>
        <el-form-item label="姓名"><el-input v-model="empForm.name" /></el-form-item>
        <el-form-item label="性别"><el-select v-model="empForm.gender"><el-option label="男" :value="1" /><el-option label="女" :value="2" /></el-select></el-form-item>
        <el-form-item label="手机号"><el-input v-model="empForm.phone" /></el-form-item>
        <el-form-item label="职位"><el-select v-model="empForm.job"><el-option v-for="item in jobOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
        <el-form-item label="薪资"><el-input-number v-model="empForm.salary" :min="0" /></el-form-item>
        <el-form-item label="部门"><el-select v-model="empForm.deptId" filterable clearable><el-option v-for="item in depts" :key="item.id" :label="item.name" :value="item.id" /></el-select></el-form-item>
        <el-form-item label="入职日期"><el-date-picker v-model="empForm.entryDate" value-format="YYYY-MM-DD" type="date" /></el-form-item>
        <el-form-item label="头像地址"><el-input v-model="empForm.image" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogs.emp=false">取消</el-button><el-button type="primary" @click="saveEmp">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="dialogs.clazz" :title="clazzForm.id ? '编辑班级' : '新增班级'" width="680px">
      <el-form :model="clazzForm" label-width="88px" class="form-grid">
        <el-form-item label="班级名称"><el-input v-model="clazzForm.name" /></el-form-item>
        <el-form-item label="教室"><el-input v-model="clazzForm.room" /></el-form-item>
        <el-form-item label="班主任"><el-select v-model="clazzForm.masterId" filterable clearable><el-option v-for="item in empOptions" :key="item.id" :label="item.name" :value="item.id" /></el-select></el-form-item>
        <el-form-item label="学科"><el-input-number v-model="clazzForm.subject" :min="1" /></el-form-item>
        <el-form-item label="开课日期"><el-date-picker v-model="clazzForm.beginDate" value-format="YYYY-MM-DD" type="date" /></el-form-item>
        <el-form-item label="结课日期"><el-date-picker v-model="clazzForm.endDate" value-format="YYYY-MM-DD" type="date" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogs.clazz=false">取消</el-button><el-button type="primary" @click="saveClazz">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="dialogs.student" :title="studentForm.id ? '编辑学生' : '新增学生'" width="760px">
      <el-form :model="studentForm" label-width="88px" class="form-grid">
        <el-form-item label="姓名"><el-input v-model="studentForm.name" /></el-form-item>
        <el-form-item label="学号"><el-input v-model="studentForm.no" /></el-form-item>
        <el-form-item label="性别"><el-select v-model="studentForm.gender"><el-option label="男" :value="1" /><el-option label="女" :value="2" /></el-select></el-form-item>
        <el-form-item label="手机号"><el-input v-model="studentForm.phone" /></el-form-item>
        <el-form-item label="身份证"><el-input v-model="studentForm.idCard" /></el-form-item>
        <el-form-item label="学历"><el-select v-model="studentForm.degree"><el-option v-for="item in degreeOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
        <el-form-item label="院校来源"><el-select v-model="studentForm.isCollege"><el-option label="是" :value="1" /><el-option label="否" :value="0" /></el-select></el-form-item>
        <el-form-item label="班级"><el-select v-model="studentForm.clazzId" filterable><el-option v-for="item in clazzOptions" :key="item.id" :label="item.name" :value="item.id" /></el-select></el-form-item>
        <el-form-item label="毕业日期"><el-date-picker v-model="studentForm.graduationDate" value-format="YYYY-MM-DD" type="date" /></el-form-item>
        <el-form-item label="联系地址"><el-input v-model="studentForm.address" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogs.student=false">取消</el-button><el-button type="primary" @click="saveStudent">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="dialogs.violation" title="违纪处理" width="420px">
      <el-form :model="violationForm" label-width="88px">
        <el-form-item label="学生"><el-input v-model="violationForm.name" disabled /></el-form-item>
        <el-form-item label="扣分"><el-input-number v-model="violationForm.score" :min="1" :max="100" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogs.violation=false">取消</el-button><el-button type="warning" @click="saveViolation">确认处理</el-button></template>
    </el-dialog>
  </el-container>
</template>

<script setup>
import { computed, nextTick, onMounted, reactive, ref } from 'vue'
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'
import { api } from './api'

const apiBaseLabel = import.meta.env.VITE_API_BASE_URL || '/api'
const loginFormRef = ref()
const jobChartRef = ref()
const clazzChartRef = ref()
const genderChartRef = ref()
const degreeChartRef = ref()
const studentCountChartRef = ref()
const uploadFile = ref(null)
const uploadUrl = ref('')

const state = reactive({ active: 'dashboard', loading: false })
const session = reactive({
  token: localStorage.getItem('tlias-token'),
  user: JSON.parse(localStorage.getItem('tlias-user') || 'null'),
})

const loginForm = reactive({ username: '', password: '' })
const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

const campusStats = [
  { label: '业务模块', value: '8' },
  { label: '核心流程', value: '4' },
  { label: '数据看板', value: '实时' },
]

const navItems = [
  { key: 'dashboard', label: '工作台', icon: 'DataBoard', caption: '运营总览与关键流程' },
  { key: 'depts', label: '部门管理', icon: 'OfficeBuilding', caption: '维护组织部门信息' },
  { key: 'emps', label: '员工管理', icon: 'UserFilled', caption: '维护员工档案、岗位和部门' },
  { key: 'clazzs', label: '班级管理', icon: 'School', caption: '管理班级周期、教室和班主任' },
  { key: 'students', label: '学生管理', icon: 'Reading', caption: '维护学生档案与违纪处理' },
  { key: 'reports', label: '统计报表', icon: 'PieChart', caption: '查看员工和学生多维统计' },
  { key: 'logs', label: '操作日志', icon: 'Tickets', caption: '追踪关键业务操作' },
  { key: 'upload', label: '文件上传', icon: 'Upload', caption: '上传头像和素材到 OSS' },
]

const jobOptions = [
  { label: '班主任', value: 1 },
  { label: '讲师', value: 2 },
  { label: '学工主管', value: 3 },
  { label: '教研主管', value: 4 },
  { label: '咨询师', value: 5 },
]

const degreeOptions = [
  { label: '初中', value: 1 },
  { label: '高中', value: 2 },
  { label: '大专', value: 3 },
  { label: '本科', value: 4 },
  { label: '硕士', value: 5 },
  { label: '博士', value: 6 },
]

const currentView = computed(() => navItems.find((item) => item.key === state.active))
const metrics = computed(() => [
  { label: '员工', value: empOptions.value.length, icon: 'UserFilled' },
  { label: '部门', value: depts.value.length, icon: 'OfficeBuilding' },
  { label: '班级', value: clazzOptions.value.length, icon: 'School' },
  { label: '学生', value: students.total || '-', icon: 'Reading' },
])

const depts = ref([])
const empOptions = ref([])
const clazzOptions = ref([])
const emps = reactive({ rows: [], total: 0 })
const clazzs = reactive({ rows: [], total: 0 })
const students = reactive({ rows: [], total: 0 })
const logs = reactive({ rows: [], total: 0 })
const reportData = reactive({ job: null, gender: [], clazz: null, degree: [] })

const empQuery = reactive({ page: 1, pageSize: 10, name: '', gender: undefined })
const clazzQuery = reactive({ page: 1, pageSize: 10, name: '' })
const studentQuery = reactive({ page: 1, pageSize: 10, name: '', clazzId: undefined })
const logQuery = reactive({ page: 1, pageSize: 10 })

const dialogs = reactive({ dept: false, emp: false, clazz: false, student: false, violation: false })
const deptForm = reactive({ id: undefined, name: '' })
const empForm = reactive({})
const clazzForm = reactive({})
const studentForm = reactive({})
const violationForm = reactive({ id: undefined, name: '', score: 1 })

onMounted(() => {
  if (session.token) refreshAll()
})

async function run(task, successMessage) {
  state.loading = true
  try {
    const result = await task()
    if (successMessage) ElMessage.success(successMessage)
    return result
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
    if (error.message?.includes('登录')) logout(false)
    throw error
  } finally {
    state.loading = false
  }
}

async function submitLogin() {
  await loginFormRef.value.validate()
  const data = await run(() => api.login(loginForm))
  session.token = data.token
  session.user = data
  localStorage.setItem('tlias-token', data.token)
  localStorage.setItem('tlias-user', JSON.stringify(data))
  ElMessage.success('登录成功')
  refreshAll()
}

function logout(showMessage = true) {
  session.token = ''
  session.user = null
  localStorage.removeItem('tlias-token')
  localStorage.removeItem('tlias-user')
  if (showMessage) ElMessage.success('已退出登录')
}

async function refreshAll() {
  await Promise.allSettled([loadDepts(), loadEmpOptions(), loadClazzOptions(), loadEmps(), loadClazzs(), loadStudents(), loadReports()])
}

function selectView(key) {
  state.active = key
  if (key === 'reports') loadReports()
  if (key === 'logs') loadLogs()
}

async function loadDepts() {
  depts.value = await run(() => api.depts.list())
}

async function loadEmpOptions() {
  empOptions.value = await run(() => api.emps.list())
}

async function loadClazzOptions() {
  clazzOptions.value = await run(() => api.clazzs.list())
}

async function loadEmps() {
  const data = await run(() => api.emps.page(empQuery))
  emps.rows = data?.rows || []
  emps.total = data?.total || 0
}

async function loadClazzs() {
  const data = await run(() => api.clazzs.page(clazzQuery))
  clazzs.rows = data?.rows || []
  clazzs.total = data?.total || 0
}

async function loadStudents() {
  const data = await run(() => api.students.page(studentQuery))
  students.rows = data?.rows || []
  students.total = data?.total || 0
}

async function loadLogs() {
  const data = await run(() => api.logs.page(logQuery))
  logs.rows = data?.rows || []
  logs.total = data?.total || 0
}

async function loadReports() {
  const [job, gender, clazz, degree] = await Promise.all([
    run(() => api.reports.job()),
    run(() => api.reports.gender()),
    run(() => api.reports.clazz()),
    run(() => api.reports.degree()),
  ])
  reportData.job = job
  reportData.gender = gender || []
  reportData.clazz = clazz
  reportData.degree = degree || []
  nextTick(renderCharts)
}

function renderCharts() {
  renderBar(jobChartRef.value, reportData.job?.jobList || [], reportData.job?.dataList || [], '#2f6fed')
  renderBar(clazzChartRef.value, reportData.clazz?.clazzList || [], reportData.clazz?.dataList || [], '#17a673')
  renderPie(genderChartRef.value, reportData.gender)
  renderPie(degreeChartRef.value, reportData.degree)
  renderBar(studentCountChartRef.value, reportData.clazz?.clazzList || [], reportData.clazz?.dataList || [], '#805ad5')
}

function renderBar(el, labels, values, color) {
  if (!el) return
  echarts.init(el).setOption({
    grid: { left: 32, right: 16, top: 24, bottom: 36 },
    tooltip: {},
    xAxis: { type: 'category', data: labels, axisTick: { show: false } },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', data: values, itemStyle: { color, borderRadius: [4, 4, 0, 0] } }],
  })
}

function renderPie(el, source) {
  if (!el) return
  echarts.init(el).setOption({
    tooltip: { trigger: 'item' },
    series: [{ type: 'pie', radius: ['46%', '72%'], data: source, label: { formatter: '{b}' } }],
  })
}

function resetEmpQuery() {
  Object.assign(empQuery, { page: 1, pageSize: 10, name: '', gender: undefined })
  loadEmps()
}

function openDept(row = {}) {
  Object.assign(deptForm, { id: row.id, name: row.name || '' })
  dialogs.dept = true
}

async function saveDept() {
  await run(() => (deptForm.id ? api.depts.update(deptForm) : api.depts.create(deptForm)), '保存成功')
  dialogs.dept = false
  loadDepts()
}

async function removeDept(id) {
  await run(() => api.depts.remove(id), '删除成功')
  loadDepts()
}

async function openEmp(row = {}) {
  Object.assign(empForm, emptyEmp(), row, { password: '' })
  dialogs.emp = true
}

function emptyEmp() {
  return { id: undefined, username: '', password: '', name: '', gender: 1, phone: '', job: 2, salary: 0, image: '', entryDate: '', deptId: undefined }
}

async function saveEmp() {
  const payload = { ...empForm }
  if (payload.id && !payload.password) delete payload.password
  await run(() => (payload.id ? api.emps.update(payload) : api.emps.create(payload)), '保存成功')
  dialogs.emp = false
  await Promise.all([loadEmps(), loadEmpOptions()])
}

async function removeEmp(id) {
  await run(() => api.emps.remove([id]), '删除成功')
  await Promise.all([loadEmps(), loadEmpOptions()])
}

function openClazz(row = {}) {
  Object.assign(clazzForm, { id: undefined, name: '', room: '', masterId: undefined, subject: 1, beginDate: '', endDate: '' }, row)
  dialogs.clazz = true
}

async function saveClazz() {
  await run(() => (clazzForm.id ? api.clazzs.update(clazzForm) : api.clazzs.create(clazzForm)), '保存成功')
  dialogs.clazz = false
  await Promise.all([loadClazzs(), loadClazzOptions()])
}

async function removeClazz(id) {
  await run(() => api.clazzs.remove(id), '删除成功')
  await Promise.all([loadClazzs(), loadClazzOptions()])
}

function openStudent(row = {}) {
  Object.assign(studentForm, emptyStudent(), row)
  dialogs.student = true
}

function emptyStudent() {
  return { id: undefined, name: '', no: '', gender: 1, phone: '', idCard: '', isCollege: 1, address: '', degree: 4, graduationDate: '', clazzId: undefined }
}

async function saveStudent() {
  await run(() => (studentForm.id ? api.students.update(studentForm) : api.students.create(studentForm)), '保存成功')
  dialogs.student = false
  loadStudents()
}

async function removeStudent(id) {
  await run(() => api.students.remove([id]), '删除成功')
  loadStudents()
}

function openViolation(row) {
  Object.assign(violationForm, { id: row.id, name: row.name, score: 1 })
  dialogs.violation = true
}

async function saveViolation() {
  await run(() => api.students.violation(violationForm.id, violationForm.score), '处理成功')
  dialogs.violation = false
  loadStudents()
}

function handleFileChange(file) {
  uploadFile.value = file.raw
}

function clearUpload() {
  uploadFile.value = null
  uploadUrl.value = ''
}

async function submitUpload() {
  uploadUrl.value = await run(() => api.upload(uploadFile.value), '上传成功')
}

function jobText(value) {
  return jobOptions.find((item) => item.value === value)?.label || '其他'
}
</script>
