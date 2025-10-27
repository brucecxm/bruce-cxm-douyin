import request from '@/utils/request.js';
export const getButtonInfo = (menu) => {
  return request.post('/admin/menu/list', menu);
};
