// axios-instance.js
import axios from 'axios';
import store from '../stores'; // 引入 Vuex 实例

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
  (result) => {
    //判断业务状态码
    //判断业务状态码
    /*     if (result.data.code === 0) {
          return result.data;
        } */

    //操作失败
    //alert(result.data.msg?result.data.msg:'服务异常')
    // ElMessage.error(result.data.msg ? result.data.msg : "服务异常");
    //异步操作的状态转换为失败
    return result;
  },
  (err) => {
    //判断响应状态码,如果为401,则证明未登录,提示请登录,并跳转到登录页面
    // if (err.response.status === 401) {
    //   ElMessage.error("请先登录");
    //   router.push("/login");
    // } else {
    //   ElMessage.error("服务异常");
    // }

    return Promise.reject(err); //异步的状态转化成失败的状态
  }
);

export default instance;
