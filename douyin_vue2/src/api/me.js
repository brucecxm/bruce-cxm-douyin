import request from '@/utils/request.js';

// params 处理get请求  data处理post请求
export const getmeinfo = (userId) => {
  return request.get(`/user/getuserinfo?user_id=${userId}`);
};
