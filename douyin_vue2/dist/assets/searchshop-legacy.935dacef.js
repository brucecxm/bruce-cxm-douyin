System.register(["./shop-legacy.ca43b366.js","./index-legacy.a351f814.js","./request-legacy.314233a9.js","./token-legacy.d0b3a640.js"],(function(t,e){"use strict";var a,r,o=document.createElement("style");return o.textContent=".wallet-container[data-v-ad30cbe0]{width:100%;height:100%;overflow:auto;font-family:Arial,sans-serif}.header[data-v-ad30cbe0]{background-color:#007bff;color:#fff;padding:10px;display:flex;justify-content:space-between;align-items:center}.search-input[data-v-ad30cbe0]{padding:8px;font-size:16px;border:none;border-radius:4px;width:250px}.search-button[data-v-ad30cbe0]{padding:8px 16px;background-color:#28a745;color:#fff;border:none;border-radius:4px;cursor:pointer}.search-button[data-v-ad30cbe0]:hover{background-color:#218838}.product-list[data-v-ad30cbe0]{display:flex;flex-wrap:wrap;gap:20px;padding:20px}.product[data-v-ad30cbe0]{background-color:#fff;border:1px solid #ccc;border-radius:8px;width:calc(33% - 20px);padding:10px;box-sizing:border-box}.product-img[data-v-ad30cbe0]{max-width:100%;border-radius:4px}.product-info[data-v-ad30cbe0]{margin-top:10px}.product-info h3[data-v-ad30cbe0]{font-size:18px;font-weight:700}.product-info p[data-v-ad30cbe0]{font-size:14px;color:#555}.loading[data-v-ad30cbe0]{text-align:center;padding:20px;font-size:18px;color:#888}\n",document.head.appendChild(o),{setters:[function(t){a=t.g},function(t){r=t.n},function(){},function(){}],execute:function(){var e=function(){var t=this,e=t._self._c;return e("div",{staticClass:"wallet-container"},[e("div",{staticClass:"header"},[e("input",{directives:[{name:"model",rawName:"v-model",value:t.searchQuery,expression:"searchQuery"}],staticClass:"search-input",attrs:{type:"text",placeholder:"搜索商品..."},domProps:{value:t.searchQuery},on:{keyup:function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.search.apply(null,arguments)},input:function(e){e.target.composing||(t.searchQuery=e.target.value)}}}),e("button",{staticClass:"search-button",on:{click:t.search}},[t._v("搜索")])]),t.products.length?e("div",{staticClass:"product-list"},t._l(t.products,(function(a,r){return e("div",{key:r,staticClass:"product"},[e("img",{staticClass:"product-img",attrs:{src:a.shop_img,alt:"product image"}}),e("div",{staticClass:"product-info"},[e("h3",[t._v(t._s(a.shop_name))]),e("p",[t._v(t._s(a.shop_desc))]),e("p",[t._v("价格："+t._s(a.shop_price)+"元")]),e("p",[t._v("详细信息："+t._s(a.shop_info))])])])})),0):e("div",{staticClass:"loading"},[t._v(" 正在加载... ")])])};t("default",r({data:function(){return{searchQuery:this.$route.query.search||"",products:[],current:1,size:10}},mounted:function(){this.search()},methods:{search:function(){var t=this;this.searchQuery&&(a(this.searchQuery,this.current,this.size).then((function(e){console.log("获取数据成功:",e.data.data),t.products=e.data.data})).catch((function(t){console.error("请求失败:",t)})),this.$router.push({path:"/searchshop",query:{search:this.searchQuery}}))}}},e,[],!1,null,"ad30cbe0",null,null).exports)}}}));
