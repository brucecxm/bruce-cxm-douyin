import api from '../index'

export default {
  // 查询菜单列表（POST /admin/menu/list）
  getMenuList: menu => api.post('/admin/menu/list', menu),

  // 根据ID查询菜单详情（GET /admin/menu/{id}）
  getMenuById: id => api.get(`/admin/menu/${id}`),

  // 新增菜单（POST /admin/menu）
  addMenu: menu => api.post('/admin/menu', menu),

  // 更新菜单（PUT /admin/menu）
  updateMenu: menu => api.put('/admin/menu', menu),

  // 删除菜单（DELETE /admin/menu?idList=1,2,3）
  deleteMenu: idList => api.delete('/admin/menu', { params: { idList } }),
}
