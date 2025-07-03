// axios-instance.js
import axios from 'axios';
import store from '@/stores';
import { Message } from 'element-ui';
// 定义公共的 baseURL
const baseURL = '/api'; // 根据实际情况设置你的后端 API 地址

// 创建 Axios 实例
const instance = axios.create({
  withCredentials: true, // 允许携带 cookies
  baseURL
});
// 添加请求拦截器
instance.interceptors.request.use(
  (config) => {
    // 从 Vuex 中获取 token
    const storeToken = store.state.token.token;
    const localToken = localStorage.getItem('token');
    const token = storeToken || localToken;

    if (token) {
      config.headers.Authorization = token;
      config.headers.token = token;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 添加响应拦截器
instance.interceptors.response.use(
  (response) => {
    const res = response.data;
    if (res.code !== undefined) {
      // 如果存在 code
      if (res.code !== 200) {
        // 且不等于 200
        Message.error(res.msg || '请求错误');
        return Promise.reject(new Error(res.msg || '请求错误'));
      }
      return res; // 存在且等于 200，返回 res
    }
    return response; // 如果不存在 code，返回原始 response
  },
  (error) => {
    debugger;
    Message.error(error.message || '服务器错误');
    return Promise.reject(error);
  }
);

export default instance;
