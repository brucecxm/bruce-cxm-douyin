import request from "@/utils/request.js";

// params 处理get请求  data处理post请求

export const homegetVideo = () => {
  return request.get("/video/test");
};

export const homegetVideomore = () => {
  return request.get("/video/more");
};
export const homegetVideocontent = (params) => {
  return request.get("/comment/getcomment", { params });
};

export const getcai = (params) => {
  return request.get("/dish/list", { params });
};
