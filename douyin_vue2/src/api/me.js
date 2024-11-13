import request from "@/utils/request.js";

// params 处理get请求  data处理post请求
export const getmeinfo = (user_id) => {
  return request.get(`/user/getuserinfo?user_id=${user_id}`);
};
