import request from '@/utils/request.js';

// params 处理get请求  data处理post请求

export const homegetVideo = (page, size, videoType) => {
  return request.get('/video/test', {
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
export const getauthzzz = (userid) => {
  return request.get('/video/auth', {
    params: { userid } // 将 userid 包装在 params 对象中
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
export const getcomment = (params) => {
  return request.get('/video/comment/getfu', { params }); // 将参数放入 { params } 中
};

export const getmumsic = (musicId) => {
  return request.get('/video/music/getmusic', {
    params: {
      musicId
    }
  });
};

// // 前端代码
// export const addcomment = (params) => {
//   return request.post("/video/comment/addVideoComment", { params }); // 将参数放入 { params } 中
// };
export const addcomment = (params) => {
  return request.post('/video/comment/addVideoComment', params, {
    headers: {
      'Content-Type': 'application/json' // 设置请求头为 JSON
    }
  });
};
