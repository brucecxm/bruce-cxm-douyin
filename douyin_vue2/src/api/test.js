import request from "@/utils/request.js";

// params 处理get请求  data处理post请求
export const testzeng = (data) => {
  return request.post("/user/zeng", data);
};


export const testshan = (data) => {
  return request.post("/user/shan", data);
};

export const testgai = (data) => {
  return request.post("/user/gai", data);
};

export const testcha = (data) => {
  return request.get("/user/cha", data);
};
export const testchashop = (data) => {
  return request.get("/user/chashop", data);
};
export const testlogin = (data) => {
  return request.post("/user/login", data);
};
