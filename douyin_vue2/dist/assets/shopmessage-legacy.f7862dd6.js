System.register(["./index-legacy.d81978a6.js"],(function(e,t){"use strict";var s,a=document.createElement("style");return a.textContent=".message-page[data-v-eb44eb02]{font-family:Arial,sans-serif;background-color:#f5f5f5;height:100vh;display:flex;flex-direction:column}.message-header[data-v-eb44eb02]{background-color:#fff;display:flex;justify-content:space-between;padding:12px 20px;box-shadow:0 2px 8px rgba(0,0,0,.1);align-items:center}.message-title[data-v-eb44eb02]{font-size:18px;font-weight:700;color:#333}.settings[data-v-eb44eb02]{font-size:18px;color:#ff6a00;cursor:pointer}.message-tabs[data-v-eb44eb02]{display:flex;justify-content:space-around;background-color:#fff;padding:10px 0;box-shadow:0 2px 8px rgba(0,0,0,.05)}.tab[data-v-eb44eb02]{font-size:16px;color:#333;cursor:pointer;padding:8px 12px;transition:background-color .3s}.tab.active[data-v-eb44eb02]{background-color:#ff6a00;color:#fff}.message-list[data-v-eb44eb02]{flex:1;padding:10px 20px;background-color:#fff;overflow-y:auto}.message-item[data-v-eb44eb02]{background-color:#f9f9f9;margin-bottom:15px;border-radius:8px;padding:12px;box-shadow:0 2px 5px rgba(0,0,0,.05)}.message-header[data-v-eb44eb02]{display:flex;justify-content:space-between;margin-bottom:8px}.message-title[data-v-eb44eb02]{font-size:16px;font-weight:700;color:#333}.message-time[data-v-eb44eb02]{font-size:12px;color:#999}.message-body[data-v-eb44eb02]{font-size:14px;color:#555;margin-bottom:10px}.message-footer[data-v-eb44eb02]{display:flex;justify-content:space-between;align-items:center}.message-status[data-v-eb44eb02]{font-size:12px;color:#ff6a00}.message-status.read[data-v-eb44eb02]{color:#28a745}.view-details[data-v-eb44eb02]{background-color:#ff6a00;color:#fff;border:none;padding:6px 12px;font-size:14px;border-radius:6px;cursor:pointer}.view-details[data-v-eb44eb02]:hover{background-color:#e65c00}\n",document.head.appendChild(a),{setters:[function(e){s=e.n}],execute:function(){var t={data:function(){return{currentTab:"all",messages:[{id:1,title:"订单已发货",time:"2024-12-18 14:30",body:"您的订单 #12345 已经发货，预计3日内到达。",status:"unread",type:"order"},{id:2,title:"系统通知",time:"2024-12-17 10:00",body:"我们的系统将于今晚进行维护，请您提前做好准备。",status:"read",type:"system"},{id:3,title:"订单已支付",time:"2024-12-16 09:00",body:"您已成功支付订单 #12346，等待发货。",status:"read",type:"order"},{id:4,title:"新活动通知",time:"2024-12-15 08:00",body:"快来参加我们的黑五大促活动，优惠多多！",status:"unread",type:"system"}]}},computed:{filteredMessages:function(){var e=this;return"all"===this.currentTab?this.messages:this.messages.filter((function(t){return t.type===e.currentTab}))}},methods:{goBack:function(){this.$router.go(-1)},goToSettings:function(){this.$router.push("/settings")},viewDetails:function(e){console.log("View details for message:",e),this.$router.push("/message/".concat(e))}}},a=function(){var e=this,t=e._self._c;return t("div",{staticClass:"message-page"},[t("div",{staticClass:"message-header"},[t("div",{staticClass:"back-button",on:{click:e.goBack}},[t("i",{staticClass:"fas fa-arrow-left"})]),t("div",{staticClass:"message-title"},[e._v("购物消息")]),t("div",{staticClass:"settings",on:{click:e.goToSettings}},[t("i",{staticClass:"icon-settings"})])]),t("div",{staticClass:"message-tabs"},[t("div",{staticClass:"tab",class:{active:"all"===e.currentTab},on:{click:function(t){e.currentTab="all"}}},[e._v(" 全部消息 ")]),t("div",{staticClass:"tab",class:{active:"system"===e.currentTab},on:{click:function(t){e.currentTab="system"}}},[e._v(" 系统消息 ")]),t("div",{staticClass:"tab",class:{active:"order"===e.currentTab},on:{click:function(t){e.currentTab="order"}}},[e._v(" 订单消息 ")])]),t("div",{staticClass:"message-list"},e._l(e.filteredMessages,(function(s){return t("div",{key:s.id,staticClass:"message-item"},[t("div",{staticClass:"message-header"},[t("div",{staticClass:"message-title"},[e._v(e._s(s.title))]),t("div",{staticClass:"message-time"},[e._v(e._s(s.time))])]),t("div",{staticClass:"message-body"},[e._v(e._s(s.body))]),t("div",{staticClass:"message-footer"},[t("div",{staticClass:"message-status",class:{read:"read"===s.status}},[e._v(" "+e._s("read"===s.status?"已读":"未读")+" ")]),t("button",{staticClass:"view-details",on:{click:function(t){return e.viewDetails(s.id)}}},[e._v("查看详情")])])])})),0)])},i=[];a._withStripped=!0;var o=s(t,a,i,!1,null,"eb44eb02",null,null);o.options.__file="F:/GitHub/bruce-cxm-douyin/douyin_vue2/src/views/shop/shopmessage.vue",e("default",o.exports)}}}));
