//导入request.js请求工具
import request from '@/utils/request.js';

//提供调用注册接口的函数
export const userRegisterService = (registerData) => {
  //借助于UrlSearchParams完成传递
  const params = new URLSearchParams();
  for (let key in registerData) {
    params.append(key, registerData[key]);
  }
  return request.post('/user/register', params);
};
// 修改 getauthzzz 函数
export const getUserInfo = (userid, page = 1, size = 10) => {
  return request.get('/user/auth', {
    params: { userid, page, size } // 将 userid 包装在 params 对象中
  });
};

//提供调用登录接口的函数
export const userLoginService = (loginData) => {
  const params = new URLSearchParams();
  for (let key in loginData) {
    params.append(key, loginData[key]);
  }
  return request.post('/user/doLogin', params);
};

//获取用户登录状态
export const userInfoService = () => {
  return request.get('/user/userInfo');
};

//获取用户登录状态
export const userInfoServiceSSSS = () => {
  return request.get('/user/getall');
};

//修改个人信息
// export const userInfoUpdateService = (User) => {
//   return request.put("/user/update", User);
// };

export const userInfoUpdateService = (User) => {
  return request.put('/user/update', User, {
    headers: {
      'Content-Type': 'application/json'
    }
  });
};

export const requestFriendApi = (User) => {
  return request.post('/user/friend/request', User, {
    headers: {
      'Content-Type': 'application/json'
    }
  });
};

//获取用户登录状态
export const getrequestFriendApi = () => {
  return request.get('/user/friend/requests');
};
//获取用户登录状态
export const getFriendApi = () => {
  return request.get('/user/friend/list');
};

//修改头像
export const userAvatarUpdateService = (avatarUrl) => {
  const params = new URLSearchParams();
  params.append('avatarUrl', avatarUrl);
  return request.patch('/user/updateAvatar', params);
};
// 获取验证码服务，确保发送正确的请求，且设置 responseType: 'blob'
export const getVerificationCodeService = (username) => {
  return request.put('/user/getVerificationCode', username, {
    responseType: 'blob' // 确保返回值是图片流
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
