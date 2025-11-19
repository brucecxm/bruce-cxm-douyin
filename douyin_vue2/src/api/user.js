//这里为什么是request   因为request.js导出了一个instance（axios实例） 这里的request相当于一个变量名
//这里为什么是request   因为request.js导出了一个instance（axios实例） 这里的request相当于一个变量名
import request from '@/utils/request.js';

//提供调用注册接口的函数
export const userRegisterService = (registerData) => {
  // 这里为什么可以不用指定表单格式 是因为 axios 会根据请求数据的类型自动设置正确的 Content-Type
  // 这里为什么可以不用指定表单格式 是因为 axios 会根据请求数据的类型自动设置正确的 Content-Type
  // 这里为什么可以不用指定表单格式 是因为 axios 会根据请求数据的类型自动设置正确的 Content-Type
  const params = new URLSearchParams();
  for (let key in registerData) {
    params.append(key, registerData[key]);
  }
  return request.post('/user/register', params, {
    // headers: { 'Content-Type': 'application/x-www-form-urlencoded' } // 多余！可能会覆盖默认的
    // headers: { 'Content-Type': 'application/x-www-form-urlencoded' } // 多余！可能会覆盖默认的
    // headers: { 'Content-Type': 'application/x-www-form-urlencoded' } // 多余！可能会覆盖默认的
  });
};

export const getUserInfo = (userId, page, pageSize) => {
  //构建参数对象
  const params = {
    userId: userId,
    page: page,
    pageSize: pageSize
  };
  return request.get('/user/userInfo', { params });
};

//提供调用登录接口的函数
export const userLoginService = (loginData) => {
  const params = new URLSearchParams();
  for (let key in loginData) {
    params.append(key, loginData[key]);
  }
  return request.post('/user/doLogin', params);
};

export const requestFriendApi = (User) => {
  return request.post('/user/friend/request', User, {
    headers: {
      'Content-Type': 'application/json'
    }
  });
};

export const getrequestFriendApi = () => {
  return request.get('/user/friend/requests');
};
export const getFriendApiDetail = () => {
  return request.get('/user/friend/list/detail');
};
export const getFriendApi = () => {
  return request.get('/user/friend/list');
};

export const getVerificationCodeService = (params) => {
  const queryParams = new URLSearchParams();
  for (let key in params) {
    queryParams.append(key, params[key]);
  }
  return request.get('/user/getVerificationCode', {
    params: queryParams,
    responseType: 'blob',
    transformResponse: [
      (data, headers) => {
        // 直接通过属性名访问响应头
        return {
          data,
          uuid: headers['uuid'] // 修正为直接访问headers对象的属性
        };
      }
    ]
  });
};

// 获取验证码服务，确保发送正确的请求，且设置 responseType: 'blob'
export const getEmailVerificationCodeService = (Data) => {
  const params = new URLSearchParams();
  for (let key in Data) {
    params.append(key, Data[key]);
  }
  return request.post('/user/getEmailVerificationCode', params);
};
