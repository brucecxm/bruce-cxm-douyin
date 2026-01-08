// API调用工具类

// API基础URL
const BASE_URL = import.meta.env.VITE_APP_API_BASE_URL || '/api';

// 默认请求配置
const defaultConfig = {
  headers: {
    'Content-Type': 'application/json'
  },
  timeout: 10000,
  withCredentials: true
};

/**
 * 处理请求URL
 * @param {string} url - 请求URL
 * @returns {string} 完整URL
 */
function handleUrl(url) {
  // 如果是完整URL，则直接使用
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url;
  }
  // 如果是相对路径，则拼接基础URL
  return `${BASE_URL}${url.startsWith('/') ? '' : '/'}${url}`;
}

/**
 * 处理请求头
 * @param {Object} customHeaders - 自定义请求头
 * @returns {Object} 合并后的请求头
 */
function handleHeaders(customHeaders = {}) {
  // 获取本地存储的token
  const token = localStorage.getItem('token') || sessionStorage.getItem('token');

  // 基础请求头
  const headers = {
    ...defaultConfig.headers,
    ...customHeaders
  };

  // 如果有token，则添加到请求头
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  return headers;
}

/**
 * 拼接GET请求参数到URL
 * @param {string} url - 基础URL
 * @param {Object} params - 请求参数
 * @returns {string} 完整URL
 */
function buildUrlWithParams(url, params) {
  if (!params || Object.keys(params).length === 0) {
    return url;
  }

  const urlObj = new URL(url);
  Object.entries(params).forEach(([key, value]) => {
    if (value !== null && value !== undefined) {
      urlObj.searchParams.append(key, value);
    }
  });
  return urlObj.href;
}

/**
 * 发送HTTP请求
 * @param {string} url - 请求URL
 * @param {string} method - 请求方法
 * @param {Object} data - 请求参数
 * @param {Object} config - 请求配置
 * @returns {Promise} 请求结果
 */
export async function request(url, method = 'GET', data = {}, config = {}) {
  let fullUrl = handleUrl(url);

  // 处理GET请求参数
  let requestData = data;
  if (method.toUpperCase() === 'GET') {
    if (requestData) {
      fullUrl = buildUrlWithParams(fullUrl, requestData);
      requestData = undefined;
    }
  }

  // 处理请求头
  const headers = handleHeaders(config.headers);
  const finalHeaders = { ...headers };

  // 如果是FormData，移除默认的Content-Type，让浏览器自动添加
  if (data instanceof FormData) {
    delete finalHeaders['Content-Type'];
  }

  try {
    // 发送请求
    const response = await fetch(fullUrl, {
      method: method,
      headers: finalHeaders,
      body: data instanceof FormData ? data : (requestData ? JSON.stringify(requestData) : undefined),
      credentials: config.withCredentials || defaultConfig.withCredentials ? 'include' : 'same-origin'
    });

    // 检查HTTP状态码
    if (!response.ok) {
      throw new Error(`HTTP错误，状态码: ${response.status}`);
    }

    // 解析响应体
    let responseData = {};
    try {
      responseData = await response.json();
      console.log('请求成功，原始返回数据:', responseData);
    } catch (e) {
      console.error('解析响应体失败:', e);
      throw new Error('解析响应体失败');
    }

    // 直接返回原始响应数据
    return responseData;
  } catch (error) {
    console.error('请求错误:', error);
    throw error;
  }
}

/**
 * GET请求
 * @param {string} url - 请求URL
 * @param {Object} params - 请求参数
 * @param {Object} config - 请求配置
 * @returns {Promise} 请求结果
 */
export function get(url, params = {}, config = {}) {
  return request(url, 'GET', params, config);
}

/**
 * POST请求
 * @param {string} url - 请求URL
 * @param {Object} data - 请求参数
 * @param {Object} config - 请求配置
 * @returns {Promise} 请求结果
 */
export function post(url, data = {}, config = {}) {
  return request(url, 'POST', data, config);
}

/**
 * PUT请求
 * @param {string} url - 请求URL
 * @param {Object} data - 请求参数
 * @param {Object} config - 请求配置
 * @returns {Promise} 请求结果
 */
export function put(url, data = {}, config = {}) {
  return request(url, 'PUT', data, config);
}

/**
 * DELETE请求
 * @param {string} url - 请求URL
 * @param {Object} params - 请求参数
 * @param {Object} config - 请求配置
 * @returns {Promise} 请求结果
 */
export function del(url, params = {}, config = {}) {
  return request(url, 'DELETE', params, config);
}

/**
 * 上传文件
 * @param {string} url - 请求URL
 * @param {FormData} formData - 表单数据
 * @param {Object} config - 请求配置
 * @returns {Promise} 请求结果
 */
export function uploadFile(url, formData, config = {}) {
  return request(url, 'POST', formData, {
    ...config,
    headers: {
      ...config.headers
    }
  });
}

/**
 * 下载文件
 * @param {string} url - 请求URL
 * @param {Object} params - 请求参数
 * @param {string} filename - 文件名
 */
export function downloadFile(url, params = {}, filename = '') {
  const fullUrl = handleUrl(url);
  const urlObj = new URL(fullUrl);

  // 添加请求参数
  Object.entries(params).forEach(([key, value]) => {
    if (value !== null && value !== undefined) {
      urlObj.searchParams.append(key, value);
    }
  });

  // 创建a标签进行下载
  const link = document.createElement('a');
  link.href = urlObj.href;
  link.download = filename;
  link.target = '_blank';
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

export default {
  request,
  get,
  post,
  put,
  delete: del,
  uploadFile,
  downloadFile
};
