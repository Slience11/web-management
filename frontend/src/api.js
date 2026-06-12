const API_BASE = import.meta.env.VITE_API_BASE_URL || '/api'

function buildUrl(path, params) {
  const url = new URL(`${API_BASE}${path}`, window.location.origin)
  Object.entries(params || {}).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') {
      url.searchParams.set(key, value)
    }
  })
  return API_BASE.startsWith('http') ? url.toString() : `${url.pathname}${url.search}`
}

async function request(path, options = {}) {
  const token = localStorage.getItem('tlias-token')
  const headers = options.body instanceof FormData ? {} : { 'Content-Type': 'application/json' }

  if (token) {
    headers.Authorization = `Bearer ${token}`
    headers.token = token
  }

  const response = await fetch(buildUrl(path, options.params), {
    method: options.method || 'GET',
    headers,
    body: options.body instanceof FormData ? options.body : options.body ? JSON.stringify(options.body) : undefined,
  })

  if (response.status === 401) {
    localStorage.removeItem('tlias-token')
    throw new Error('登录已过期，请重新登录')
  }

  const result = await response.json().catch(() => ({ code: 0, msg: '服务响应异常' }))
  if (!response.ok || result.code === 0) {
    throw new Error(result.msg || '请求失败')
  }
  return result.data
}

export const api = {
  login: (body) => request('/login', { method: 'POST', body }),
  upload: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return request('/upload', { method: 'POST', body: formData })
  },
  depts: {
    list: () => request('/depts'),
    create: (body) => request('/depts', { method: 'POST', body }),
    update: (body) => request('/depts', { method: 'PUT', body }),
    remove: (id) => request('/depts', { method: 'DELETE', params: { id } }),
  },
  emps: {
    page: (params) => request('/emps', { params }),
    list: () => request('/emps/list'),
    get: (id) => request(`/emps/${id}`),
    create: (body) => request('/emps', { method: 'POST', body }),
    update: (body) => request('/emps', { method: 'PUT', body }),
    remove: (ids) => request('/emps', { method: 'DELETE', params: { ids: ids.join(',') } }),
  },
  clazzs: {
    page: (params) => request('/clazzs', { params }),
    list: () => request('/clazzs/list'),
    get: (id) => request(`/clazzs/${id}`),
    create: (body) => request('/clazzs', { method: 'POST', body }),
    update: (body) => request('/clazzs', { method: 'PUT', body }),
    remove: (id) => request(`/clazzs/${id}`, { method: 'DELETE' }),
  },
  students: {
    page: (params) => request('/students', { params }),
    get: (id) => request(`/students/${id}`),
    create: (body) => request('/students', { method: 'POST', body }),
    update: (body) => request('/students', { method: 'PUT', body }),
    remove: (ids) => request(`/students/${ids.join(',')}`, { method: 'DELETE' }),
    violation: (id, score) => request(`/students/violation/${id}/${score}`, { method: 'PUT' }),
  },
  reports: {
    job: () => request('/report/empJobData'),
    gender: () => request('/report/empGenderData'),
    clazz: () => request('/report/studentCountData'),
    degree: () => request('/report/studentDegreeData'),
  },
  logs: {
    page: (params) => request('/log/page', { params }),
  },
}
