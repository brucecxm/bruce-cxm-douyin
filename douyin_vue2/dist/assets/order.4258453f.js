import{n as c}from"./index.d6cacc81.js";const r={data(){return{currentTab:"shipped",orders:[{id:1,status:"\u5DF2\u53D1\u8D27",image:"../../assets/op.jpg",name:"\u7269\u6D41\u72B6\u6001 \u5DF2\u7B7E\u6536",time:"2021-05-30 18:06:37",price:"\xA5 299.00",quantity:1,totalPrice:"\xA5 299.00"},{id:2,status:"\u5DF2\u53D1\u8D27",image:"../../assets/op.jpg",name:"\u7269\u6D41\u72B6\u6001 \u5DF2\u7B7E\u6536",time:"2021-05-30 18:06:33",price:"\xA5 199.00",quantity:1,totalPrice:"\xA5 199.00"}]}},computed:{filteredOrders(){return this.currentTab==="all"?this.orders:this.currentTab==="shipped"?this.orders.filter(a=>a.status==="\u5DF2\u53D1\u8D27"):[]}},methods:{goBack(){this.$router.go(-1)},applyAfterSales(a){console.log("Apply after sales for order:",a)},viewLogistics(a){console.log("View logistics for order:",a)},confirmReceipt(a){console.log("Confirm receipt for order:",a)}}};var n=function(){var t=this,s=t._self._c;return s("div",{staticClass:"order-page"},[s("div",{staticClass:"order-header"},[s("div",{staticClass:"back-button",on:{click:t.goBack}},[s("i",{staticClass:"icon-back"}),s("i",{staticClass:"fas fa-arrow-left"})]),s("div",{staticClass:"order-title"},[t._v("\u5168\u90E8\u8BA2\u5355")]),s("div",{staticClass:"manage-auth"},[t._v("\u6388\u6743\u7BA1\u7406")])]),s("div",{staticClass:"order-tabs"},[s("div",{staticClass:"tab",class:{active:t.currentTab==="all"},on:{click:function(i){t.currentTab="all"}}},[t._v("\u5168\u90E8")]),s("div",{staticClass:"tab",class:{active:t.currentTab==="unpaid"},on:{click:function(i){t.currentTab="unpaid"}}},[t._v("\u5F85\u652F\u4ED8")]),s("div",{staticClass:"tab",class:{active:t.currentTab==="unshipped"},on:{click:function(i){t.currentTab="unshipped"}}},[t._v("\u5F85\u53D1\u8D27")]),s("div",{staticClass:"tab",class:{active:t.currentTab==="shipped"},on:{click:function(i){t.currentTab="shipped"}}},[t._v("\u5F85\u6536\u8D27")]),s("div",{staticClass:"tab",class:{active:t.currentTab==="reviewed"},on:{click:function(i){t.currentTab="reviewed"}}},[t._v("\u5F85\u8BC4\u4EF7")])]),s("div",{staticClass:"order-list"},t._l(t.filteredOrders,function(i){return s("div",{key:i.id,staticClass:"order-item"},[s("div",{staticClass:"order-status"},[t._v(t._s(i.status))]),s("div",{staticClass:"order-info"},[s("img",{staticClass:"product-image",attrs:{src:i.image,alt:"Product Image"}}),s("div",{staticClass:"product-details"},[s("div",{staticClass:"product-name"},[t._v(t._s(i.name))]),s("div",{staticClass:"order-time"},[t._v(t._s(i.time))]),s("div",{staticClass:"return-policy"},[t._v("7\u5929\u65E0\u7406\u7531\u9000\u8D27")])]),s("div",{staticClass:"price-info"},[s("div",{staticClass:"price"},[t._v(t._s(i.price))]),s("div",{staticClass:"quantity"},[t._v("x"+t._s(i.quantity))])])]),s("div",{staticClass:"order-actions"},[s("div",{staticClass:"total-price"},[t._v("\u5408\u8BA1: "+t._s(i.totalPrice))]),s("button",{staticClass:"action-button",on:{click:function(e){return t.applyAfterSales(i.id)}}},[t._v("\u7533\u8BF7\u552E\u540E")]),s("button",{staticClass:"action-button",on:{click:function(e){return t.viewLogistics(i.id)}}},[t._v("\u67E5\u770B\u7269\u6D41")]),s("button",{staticClass:"action-button confirm-button",on:{click:function(e){return t.confirmReceipt(i.id)}}},[t._v("\u786E\u8BA4\u6536\u8D27")])])])}),0)])},o=[],l=c(r,n,o,!1,null,"9529f19e",null,null);const u=l.exports;export{u as default};
