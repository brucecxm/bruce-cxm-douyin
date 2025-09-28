import request from '@/utils/request.js';

// 获取店铺列表（分页）
export const shoplistPage = (params) => {
  return request.get('/shop/page', {
    params: {
      current: params.current || 1,
      size: params.size || 10,
      ...params.shop,
      sort: params.sort,
      order: params.order
    }
  });
};

// 简化版获取店铺列表
export const shoplist = (current, size) => {
  return request.get(`/shop?current=${current}&size=${size}`);
};

// 获取店铺分类列表
export const getCategories = () => {
  return request.get('/shop/categories');
};

// 根据关键字搜索店铺
export const getlikeall = (key, current, size) => {
  return request.get('/shop/likeall', {
    params: {
      key,
      current,
      size
    }
  });
};

// 搜索菜品相关
export const searchForDish = (name) => {
  return request.get(`/shop/dish/sear/${name}`);
};

export const liansearchForDish = (name) => {
  return request.get(`/shop/dish/liansear/${name}`);
};

// 获取菜品分页列表
export const shoplistpage = (page = 1, pageSize = 10) => {
  return request.get('/shop/dish/page', {
    params: {
      page: page,
      pageSize: pageSize,
      name: ''
    }
  });
};

// 获取导航菜单
export const getnav = (page = 1, pageSize = 10) => {
  return request.get('/shop/fileImg', {
    params: {
      page: page,
      pageSize: pageSize,
      type: 'shop_menu'
    }
  });
};

// 获取钱包信息
export const getwallet = (name) => {
  return request.get(`/shop/dish/sear/${name}`);
};
