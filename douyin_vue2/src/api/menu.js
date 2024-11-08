import request from "@/utils/request.js";

// params 处理get请求  data处理post请求

export const shopmenulist = () => {
  return request.get("/menu/list");
};

export const shopnavlist = () => {
  return request.get("/nav/list");
};
