import request from "@/utils/request.js";

// params 处理get请求  data处理post请求
export const testzeng = (data) => {
  return request.post("/test/zeng", data);
};


export const testshan = (data) => {
  return request.post("/test/shan", data);
};

export const testgai = (data) => {
  return request.post("/test/gai", data);
};

export const testcha = (data) => {
  return request.get("/test/cha", data);
};
