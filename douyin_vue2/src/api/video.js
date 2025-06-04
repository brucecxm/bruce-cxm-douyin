import request from '@/utils/request.js';

// params 处理get请求  data处理post请求

export const homegetVideo = (page, size, videoType) => {
  return request.get('/video/getVideoPage', {
    params: {
      page,
      size,
      videoType
    }
  });
};

export const homegetVideomore = () => {
  return request.get('/video/more');
};
export const homegetVideocontent = (params) => {
  return request.get('/video/comment/getcomment', { params });
};

// 修改 getauthzzz 函数
export const getauthzzz = (userid, page = 1, size = 10) => {
  return request.get('/video/auth', {
    params: { userid, page, size } // 将 userid 包装在 params 对象中
  });
};

export const like = (params) => {
  return request.post('/video/like', { params });
};

export const getcai = (params) => {
  return request.get('/dish/list', { params });
};

export const uploadFile = (file, additionalParams) => {
  // 创建 FormData 对象
  const formData = new FormData();

  // 将文件添加到 FormData 对象中，'file' 是后端接收文件的字段名
  formData.append('file', file);

  // 添加其他参数
  if (additionalParams instanceof Map) {
    additionalParams.forEach((value, key) => {
      formData.append(key, value);
    });
  } else {
    for (const key in additionalParams) {
      if (additionalParams.Object.prototype.hasOwnPropertyhasOwnProperty(key)) {
        formData.append(key, additionalParams[key]);
      }
    }
  }

  // 发送 POST 请求上传文件
  return request.post('/video/upvideo', formData, {
    headers: {
      'Content-Type': 'multipart/form-data' // 设置请求头
    }
  });
};
// 前端代码
export const getauthinfo = (params) => {
  return request.get('/user/friend/getauth', { params }); // 将参数放入 { params } 中
};

// 前端代码
export const getComment = (params) => {
  return request.get('/video/comment/getFatherComment', { params }); // 将参数放入 { params } 中
};

export const getmumsic = (musicId, page = 1, size = 10) => {
  return request.get('/video/music/getmusic', {
    params: {
      musicId,
      page,
      size
    }
  });
};


export const addcomment = (params) => {
  return request.post('/video/comment/addVideoComment', params, {
    headers: {
      'Content-Type': 'application/json' // 设置请求头为 JSON
    }
  });
};
