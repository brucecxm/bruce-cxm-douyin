import{g as c}from"./shop.f10800ec.js";import{n as i}from"./index.368bb805.js";import"./request.2e0c4af0.js";import"./token.138d03eb.js";const o={data(){return{searchQuery:this.$route.query.search||"",products:[],current:1,size:10}},mounted(){this.search()},methods:{search(){!this.searchQuery||(c(this.searchQuery,this.current,this.size).then(r=>{console.log("\u83B7\u53D6\u6570\u636E\u6210\u529F:",r.data.data),this.products=r.data.data}).catch(r=>{console.error("\u8BF7\u6C42\u5931\u8D25:",r)}),this.$router.push({path:"/searchshop",query:{search:this.searchQuery}}))}}};var n=function(){var s=this,e=s._self._c;return e("div",{staticClass:"wallet-container"},[e("div",{staticClass:"header"},[e("input",{directives:[{name:"model",rawName:"v-model",value:s.searchQuery,expression:"searchQuery"}],staticClass:"search-input",attrs:{type:"text",placeholder:"\u641C\u7D22\u5546\u54C1..."},domProps:{value:s.searchQuery},on:{keyup:function(t){return!t.type.indexOf("key")&&s._k(t.keyCode,"enter",13,t.key,"Enter")?null:s.search.apply(null,arguments)},input:function(t){t.target.composing||(s.searchQuery=t.target.value)}}}),e("button",{staticClass:"search-button",on:{click:s.search}},[s._v("\u641C\u7D22")])]),s.products.length?e("div",{staticClass:"product-list"},s._l(s.products,function(t,a){return e("div",{key:a,staticClass:"product"},[e("img",{staticClass:"product-img",attrs:{src:t.shop_img,alt:"product image"}}),e("div",{staticClass:"product-info"},[e("h3",[s._v(s._s(t.shop_name))]),e("p",[s._v(s._s(t.shop_desc))]),e("p",[s._v("\u4EF7\u683C\uFF1A"+s._s(t.shop_price)+"\u5143")]),e("p",[s._v("\u8BE6\u7EC6\u4FE1\u606F\uFF1A"+s._s(t.shop_info))])])])}),0):e("div",{staticClass:"loading"},[s._v(" \u6B63\u5728\u52A0\u8F7D... ")])])},u=[],l=i(o,n,u,!1,null,"ad30cbe0",null,null);const m=l.exports;export{m as default};
