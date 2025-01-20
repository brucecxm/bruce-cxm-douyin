import request from "@/utils/request.js";

// params 处理get请求  data处理post请求

export const shoplist = (current, size) => {
  return request.get(`/shop?current=${current}&size=${size}`);
};

export const searchForDish = (name) => {
  return request.get(`/shop/dish/sear/${name}`);
};
export const liansearchForDish = (name) => {
  return request.get(`/shop/dish/liansear/${name}`);
};

export const shoplistpage = (page = 1, pageSize = 10) => {
  return request.get("/shop/dish/page", {
    params: {
      page: page, // 当前页码
      pageSize: pageSize, // 每页条数
      name: "", // 你可以在这里根据需要传递查询的名称（如果需要的话）
    },
  });
};
export const getnav = (page = 1, pageSize = 10) => {
  return request.get("/shop/fileImg", {
    params: {
      page: page, // 当前页码
      pageSize: pageSize, // 每页条数
      type: "shop_menu",
    },
  });
};
