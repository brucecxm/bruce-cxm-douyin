import api from '../index'

export default {

  // 登录
  // 登录
  getAllVideo: (page, size) => api.get('/video/test', {
    params: {
      page,
      size,
    },
  }),

}
