import request from "@/utils/request.js";

// params 处理get请求  data处理post请求

export const shoplist = () => {
  return request.get("/dish/list");
};
export const searchForDish = (name) => {
  return request.get(`/dish/sear/${name}`);
};
export const liansearchForDish = (name) => {
  return request.get(`/dish/liansear/${name}`);
};

export const shoplistpage = (page = 1, pageSize = 10) => {
  return request.get("/dish/page", {
    params: {
      page: page, // 当前页码
      pageSize: pageSize, // 每页条数
      name: "", // 你可以在这里根据需要传递查询的名称（如果需要的话）
    },
  });
};
export const getnav = (page = 1, pageSize = 10) => {
  return request.get("/fileImg", {
    params: {
      page: page, // 当前页码
      pageSize: pageSize, // 每页条数
      type: "shop_menu",
    },
  });
};
