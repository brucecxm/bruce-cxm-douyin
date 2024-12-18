import request from "@/utils/request";

export function getList(params) {
  return request({
    url: "/exam/swiper/list",
    method: "get",
    params
  });
}
export function save(params) {
  return request({
    url: "/exam/swiper/save",
    method: "post",
    data: params
  });
}
// export function drop(param) {
//   return request({
//     url: '/exam/swiper/delete',
//     method: 'post',
//     data: param
//   })
// }

export function drop(params) {
  return request({
    url: `/exam/swiper/${params.id}`, // 使用模板字符串来构建 URL
    method: "delete" // 将方法改为 'delete'
  });
}
