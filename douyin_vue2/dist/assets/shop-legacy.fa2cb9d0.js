!function(){function e(o){return e="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(e){return typeof e}:function(e){return e&&"function"==typeof Symbol&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e},e(o)}function o(o,t,a){return(t=function(o){var t=function(o,t){if("object"!=e(o)||!o)return o;var a=o[Symbol.toPrimitive];if(void 0!==a){var n=a.call(o,t||"default");if("object"!=e(n))return n;throw new TypeError("@@toPrimitive must return a primitive value.")}return("string"===t?String:Number)(o)}(o,"string");return"symbol"==e(t)?t:t+""}(t))in o?Object.defineProperty(o,t,{value:a,enumerable:!0,configurable:!0,writable:!0}):o[t]=a,o}function t(e){return function(e){if(Array.isArray(e))return a(e)}(e)||function(e){if("undefined"!=typeof Symbol&&null!=e[Symbol.iterator]||null!=e["@@iterator"])return Array.from(e)}(e)||function(e,o){if(e){if("string"==typeof e)return a(e,o);var t={}.toString.call(e).slice(8,-1);return"Object"===t&&e.constructor&&(t=e.constructor.name),"Map"===t||"Set"===t?Array.from(e):"Arguments"===t||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(t)?a(e,o):void 0}}(e)||function(){throw new TypeError("Invalid attempt to spread non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.")}()}function a(e,o){(null==o||o>e.length)&&(o=e.length);for(var t=0,a=Array(o);t<o;t++)a[t]=e[t];return a}System.register(["./shop-legacy.ca43b366.js","./4-legacy.55c8a3c0.js","./token-legacy.d0b3a640.js","./footer-legacy.a7b4e34f.js","./index-legacy.a351f814.js","./request-legacy.314233a9.js"],(function(e,a){"use strict";var n,s,i,r,l,d,c,u,h,g,f,m,v,p,b,x,w,y,A,I,k,S=document.createElement("style");return S.textContent=".box-containeraaa[data-v-97d8ea2d]{height:100vh;overflow-y:scroll;border:1px solid #ccc}.boxaaa[data-v-97d8ea2d]{padding:10px;border:1px solid #ccc;margin:5px;background-color:#f9f9f9}.loadingaaa[data-v-97d8ea2d]{text-align:center;padding:10px;color:#888}.lian[data-v-97d8ea2d]{z-index:10;background-color:#f8f9fa;border-radius:8px;box-shadow:0 4px 10px rgba(0,0,0,.1);padding:10px 15px;max-width:300px;width:100%;font-family:Arial,sans-serif;text-align:left}.lian ul[data-v-97d8ea2d]{list-style-type:none;padding:0;margin:0}.lian li[data-v-97d8ea2d]{padding:8px;font-size:14px;color:#333;cursor:pointer;transition:background-color .3s,color .3s}.lian li[data-v-97d8ea2d]:hover{background-color:#f0f0f0;color:#007bff}html[data-v-97d8ea2d],body[data-v-97d8ea2d]{height:100%;overflow:hidden}.shop[data-v-97d8ea2d]{width:100vw;height:100vh;display:block;position:relative;overflow:hidden;background-color:#fff}.allowscoll[data-v-97d8ea2d]{height:100vh;overflow-y:scroll}.allowscoll[data-v-97d8ea2d]::-webkit-scrollbar{display:none}.allowscoll[data-v-97d8ea2d]{-ms-overflow-style:none;scrollbar-width:none}.boxm[data-v-97d8ea2d]{width:40vw;height:30vh;background-color:green;margin:2vw;background-size:cover;background-position:center;background-repeat:no-repeat;cursor:pointer}.main[data-v-97d8ea2d]{width:95vw;display:flex;flex-wrap:wrap;justify-content:center;align-content:center;margin:0 auto}.nav img[data-v-97d8ea2d],p[data-v-97d8ea2d]{width:10vw;display:block;transform:translate(3vw);font-size:3vw;color:#2f2f2f;line-height:0px}.header[data-v-97d8ea2d]{overflow:hidden;position:absolute;top:0px;width:100%;background-color:#fff;z-index:9}.search input[data-v-97d8ea2d]{width:50%;height:3vh;margin-left:5vw;border:none;display:block;float:left;outline:none;margin-top:.8vh}.search[data-v-97d8ea2d]{border:solid 2px rgb(233,111,111);overflow:hidden;border-radius:10px;margin-top:1vh;width:95%;float:right;margin:5px}.search button[data-v-97d8ea2d]{display:block;float:right;height:3vh;width:15vw;border:none;background-color:red;color:#fff;border-radius:4vw;margin:1vw}.didian[data-v-97d8ea2d]{width:10vw;line-height:20vw;font-size:5vw;margin-left:1vw;margin-right:0;float:left}.box1[data-v-97d8ea2d]{height:20vw;width:20vw;margin:2px;flex-shrink:0;background-size:cover}.box2[data-v-97d8ea2d]{width:18vw;height:18vw;margin:1px}.nav[data-v-97d8ea2d]{width:100%;display:flex;flex-wrap:wrap;align-content:center;justify-content:center;margin-top:1vh}.meum[data-v-97d8ea2d]{margin-top:7vh;display:flex;flex-direction:row;align-items:center;justify-content:flex-start;overflow-x:auto;overflow-y:hidden;white-space:nowrap}.meum[data-v-97d8ea2d]::-webkit-scrollbar{width:0;background:transparent}.footer[data-v-97d8ea2d]{background-color:#000;color:#000;z-index:10;position:absolute;bottom:0px}.jiahao[data-v-97d8ea2d]{background-image:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAgCAYAAABgrToAAAAH6ElEQVRYhb1YfVAU5xl/9t139772+BThTkBF4HQaGSTGJEwjgYQYmz9sqCgZtcYmbWqjNBnT1CYd66RWnWgmTWtUzDiQKG3j+JFPRgUlMmIbExQRUD4MisjXwR2yd8fd7e67nXfvMI0inJe2P+a9WXZ2f/vbd5/3eX7vw6hzs2E8VL+5KaWtrT3VbrdPFkVR8Pl8eno5IQSpqgqqqqJxCTQgQIglGLMyz3N+QTC54uLi+2229NacV9ZfHe/OMQVW/umN9Kamlvs6O68n9/b1xjsc9vUejwf8fj8osqxdo9I/VTu4C9TgdUGJDAKGYYBlWeB5DowmASbFxm6Oj4/vS0xK7MrKmnMu55Xf3iH2DoEHX10378SJ6ry6rxu2DA+7gGURGE0ccBynkSMUmDCGAVBVZpx3v1154H9FIaAoCkiSBB7PCP0SIAgCzM/54a9zcubXLNq0pf6uAkt/9fO8jz/+bNFXZ88VM4DBYrFCSso0SE1LhsjIyGqj0WTX6XQOYKhARmYgKHA8naPyAuGAfT6f2e12W50OZ257ezt8800H9PT0ai+ekzN/a0HBj48sfnP72dH78OhBzVtbk2tK9+W0tXUUm4VIWLAgX344+8E30m2pB0wmQz/HYTFl6Qp5YikT48o/9hkkSdKLomtqy+XWJadP1/6utvYMNDY2red1vD9px587H1zzUu8tga3lZfyxsg+evHypZYNBb4L7s7Ng4Y8WPPvkhg3l/w1Bt2NG0YoRAKDD+QBAffTG37e4PZ6yhgsN0NzUvKH65KmWqP2lh23LV3m1gOru7rFWVZ0ssdsHIT0tDQqXFP7kfyVuLDy1cdP7+fmPbZk+fRo4nU6orj5V3tvblwDa+geAG129iQP2m8BhHqZNT+p8/LVXD/+/xI3ip3/d+drs2bNbAFjout4NLtElaAJb9pfpBwYGJsmyAiaTEaKiIq6E+5DG0hJrx4H9OIRLx8Tk+LgLEZECeDxuGL4pRmkC3S63MDQ0fIQQAIPRCEaj0R4O+Vc738msqDhaVnm8avf5kh22cDgEQegWBBP4JQluDg9H0HPY6/XpXS63lgYMej3o9HpHOOR1dfVrdr67J1+v5+HFNavNcwCW3isHz3OiTqfTUqbocgmt5e/zSCEKkmVJS5gcTxMyFsMRqBIVE4UBSaKJmIT1mTHGIzzHacd+r+9Dv9/HI6IQRDM7lc2ymI6wcp2iEAMtfUSrFMQQDgdCSEYsqx3LikwrDsZEJYioKjAIAYtoUWdCFtj+930GUAE7nUMzHBXH02mYEBXA6RxKqX9v1wya4GnFSX1m5UgofIyWVgJlifIQAgirhNBZHKfo34nKzRsLTp489XbjS79JJgqAJMkwOOgAmgnoYjtaccx2rq6unUEALMtA1lNP1BcUFCzKeO6XnaE+g5oSRVHQrVihTkMbIRTW5uZLyxoaGpIvX2rVBKmE0cRB0OH02+3gHBoM8CIa/PrMWbNmLcsA2DIhORN0Ipqe/6jFgSkMbRpNJqHbYrGA5Jc1QZKswJBzGPp6+zXSmJhoiJ0UAyxiNDdksSTIGHMhfeZbMoJeDgMDhGHUoNsIiQKeL9m7dt7eXdt6enofIgrhRNGVXFv75ebDhz4B6saysx+Gx/MfXWswGJwcx4kJCQlf37fqF92haft2oqgezCKWUJ+nhqouiIznVndmAGgx1bK/LLqz80YuQkw+FThliuVfmZmZu1OWLg/b/XAcpkNGCDGERawWfzTd0KV9r2S25c86TSZTN+WgQWQ0GvvDEUcIwQoh2izSSWNZVsYMQgRhpBlGuholSTHfKzGFRoY5wJgFjuO94XAQomBFCbwXx3HPYIxlTDcxelpeQAWfzwd+vy8sgVHRkS1WawLExEaDxWr5ZzgcsqwY6MILfgXPzBU/82KDweA1mwWgC4VujDyekcnhkGdmZuxeW7wajEZD/8yZ6QfC4fCOeKPdLo+2wTKbzS56DguC4IqJjV6AWOYYtTnisJgcDnn6spViOsC2cO4dhcMxZHO5R0Bv0IM5wjxMT6PpS5YRiyWhl64a0eWGgUFHWFbp++J8yS5b57WuXLfbDTExURBhFgIC6Y/VYu3Oypqz3WQ0wvnz9fgPCxc2tpZ/EFYshotDhz76/MKFi9oWNCtrzusxsbGa7WM3WpPA+nmFRyxaonZcvSZ3dd2YMzAwOBlUda76/Kqu5GOV4+78vy9q3tqaeyAycs+Jquq5AwMOmDo1aXdh4dMHH1v/ejvcvi9e98gjxVWV1e8MDjohOjoS8p/IEx/Nnf+yJSGhjud5kcXsSMDtqDCa80LFaCEgCtErisJJsqy/drUz/4svarZ9+slnmDYBEhOTIC8v54U/Hj+6Z5T2OwIv7t0T8dGRTwvKyvaV+v0SGIwGbcp5Xg9Y6yow2gBG0Vb9vSEokARKKjUZIx4f0O6Fy+2CuLhYKCoqXLF48dMH05ev9I4pkKJu945JZ858md3W2p7e1nZlW9f1G+ByeUCW5eAs0EEg0F0IkjAhtRZgVGagR8OD2RwBiVOsYLOlrZs5K635gXn3n8164cXvbDnu2t06/fb2xIsXmzLa2q6kDtodcV6vVy9JEh/saq0J3P2twIlsWvAl/qKVVpaVdTq9P25yXF9aWmp7ZuYP6h8qfnlMMzFh++12dHz4NyTLtCQRHKq/YBhEbZdM22/TlxaRkB8GAP8Gcox/aOD4bYkAAAAASUVORK5CYII=)}\n",document.head.appendChild(S),{setters:[function(e){n=e.s,s=e.a},function(e){i=e.i,r=e.a,l=e.b,d=e.c,c=e.d,u=e.e,h=e.f,g=e.g,f=e.h,m=e.j,v=e.k,p=e.l,b=e.m,x=e.n,w=e.s,y=e.o},function(e){A=e.u},function(e){I=e.f},function(e){k=e.n},function(){}],execute:function(){var a={beforeDestroy:function(){document.removeEventListener("click",this.handleOutsideClick)},components:{footerVue:I},data:function(){return{searchText:"",showSuggestions:!1,suggestions:["苹果","香蕉","橙子","葡萄","西瓜","草莓","芒果"],searchlian:[{id:1,name:"ssadsas"},{id:2,name:"ssdasss"},{id:3,name:"dsadssss"}],search:"",isFocused:!1,boxes:Array.from({length:50},(function(e,o){return o+1})),loading:!1,meumbox:[{name:"超d市",menuImg:i,hrefurl:"order"},{name:"超d市",menuImg:r,hrefurl:"chongzhi"},{name:"超d市",menuImg:l,hrefurl:"shopmessage"},{name:"超d市",menuImg:d,hrefurl:""},{name:"超d市",menuImg:c,hrefurl:""},{name:"超d市",menuImg:c,hrefurl:""},{name:"超d市",menuImg:c,hrefurl:""}],navbox:[{navname:"ss",navImg:u,navhrefurl:"order"},{navname:"ss",navImg:h,navhrefurl:"chongzhi"},{navname:"ss",navImg:g,navhrefurl:"shopmessage"},{navname:"ss",navImg:f,navhrefurl:"order"},{navname:"ss",navImg:m,navhrefurl:""},{navname:"ss",navImg:m,navhrefurl:""},{navname:"ss",navImg:m,navhrefurl:""},{navname:"ss",navImg:m,navhrefurl:""},{navname:"ss",navImg:m,navhrefurl:""},{navname:"ss",navImg:m,navhrefurl:""}],mainbox:[{image:v,name:"商品1",hrefurl:"/shopdetail/1",shopId:"25"},{image:p,name:"商品1",hrefurl:"/shopdetail/1",shopId:"55"},{image:b,name:"商品1",hrefurl:"/shopdetail/1",shopId:"66"},{image:x,name:"商品1",hrefurl:"/shopdetail/1",shopId:"55"},{image:x,name:"商品1",hrefurl:"/shopdetail/1",shopId:"4"},{image:x,name:"商品1",hrefurl:"/shopdetail/1",shopId:"5"}],pagenum:1,size:10}},computed:{filteredSuggestions:function(){var e=this;return this.suggestions.filter((function(o){return o.toLowerCase().includes(e.searchText.toLowerCase())}))}},mounted:function(){var e=this;document.addEventListener("click",this.handleOutsideClick),n(this.pagenum,this.size).then((function(o){console.log(o),o&&o.data&&o.data.data?(e.mainbox=o.data.data.records,console.log(e.mainbox)):console.log("获取 shoplist 数据失败: 数据无效")})).catch((function(e){console.log("获取 shoplist 数据出错:",e),console.log("获取 shoplist 数据出错")})),s().then((function(o){console.log(o),o&&o.data&&o.data.data?(e.navbox=o.data.data,console.log(e.navbox)):console.log("获取 shoplist 数据失败: 数据无效")})).catch((function(e){console.log("获取 shoplist 数据出错:",e),console.log("获取 shoplist 数据出错")})),w().then((function(o){console.log(o),o&&o.data&&o.data.data?(e.meumbox=o.data.data.records,console.log(e.meumbox)):console.log("获取 shopmenulist 数据失败: 数据无效")})).catch((function(e){console.log("获取 shopmenulist 数据出错:",e),console.log("获取 shopmenulist 数据出错")})),y().then((function(o){console.log(o),o&&o.data&&o.data.data?(e.navbox=o.data.data.records,console.log(e.navbox)):console.log("获取 shopnavlist 数据失败: 数据无效")})).catch((function(e){console.log("获取 shopnavlist 数据出错:",e),console.log("获取 shopnavlist 数据出错")}))},methods:o(o(o(o(o(o(o(o(o(o({selectSuggestion:function(e){this.searchText=e,this.showSuggestions=!1},hideSuggestions:function(){var e=this;setTimeout((function(){e.showSuggestions=!1}),100)},handleOutsideClick:function(e){var o=this.$refs.searchBox,t=this.$refs.suggestionBox;o.contains(e.target)||t.contains(e.target)||(this.showSuggestions=!1)},searchmeth:function(){console.log("搜索内容:",this.searchText),this.$router.push({path:"/searchshop",query:{search:this.searchText}})},submitlian:function(e){this.search=e},handleScroll:function(e){var o=e.target;o.scrollHeight<=o.scrollTop+o.clientHeight&&!this.loading&&this.loadMoreBoxes()},loadMoreBoxes:function(){var e=this;this.loading=!0,setTimeout((function(){n(e.pagenum,e.size).then((function(o){if(console.log(o),o&&o.data&&o.data.data){var a,n=o.data.data.records;if(Array.isArray(n))console.log("New data:",n),(a=e.mainbox).push.apply(a,t(n)),e.pagenum++,console.log(e.mainbox);else console.log("新数据不是数组:",n)}else console.log("获取 shoplist 数据失败: 数据无效")})).catch((function(e){console.log("获取 shoplist 数据出错:",e)})),e.loading=!1}),1e3)},go:function(e){console.log(e)},goshopdetail:function(e){console.log(e)}},"go",(function(e){var o=A().getToken;this.$router.push({path:"/".concat(e,"/").concat(o)})})),"handleFocus",(function(){this.isFocused=!0})),"handleBlur",(function(){this.isFocused=!1})),"goshopdetail",(function(e){this.$router.push({path:"/shopdetail/".concat(e)})})),"getmenu",(function(){})),"getnavone",(function(){s()})),"getshopinfo",(function(){})),"getinputciyu",(function(){})),"startsearch",(function(){})),"gettuijian",(function(){}))};e("default",k(a,(function(){var e=this,o=e._self._c;return o("div",{staticClass:"ssss"},[o("div",{staticClass:"header"},[o("div",{ref:"searchBox",staticClass:"search"},[o("button",{on:{click:e.searchmeth}},[e._v("搜索")]),o("input",{directives:[{name:"model",rawName:"v-model",value:e.searchText,expression:"searchText"}],attrs:{type:"text",placeholder:"输入搜索内容"},domProps:{value:e.searchText},on:{focus:function(o){e.showSuggestions=!0},blur:e.hideSuggestions,input:function(o){o.target.composing||(e.searchText=o.target.value)}}})]),o("div",{ref:"suggestionBox",staticClass:"lian"},[e.showSuggestions&&e.filteredSuggestions.length>0?o("ul",e._l(e.filteredSuggestions,(function(t,a){return o("li",{key:a,on:{click:function(o){return e.selectSuggestion(t)}}},[e._v(" "+e._s(t)+" ")])})),0):e._e()])]),o("div",{staticClass:"box-containeraaa",on:{scroll:e.handleScroll}},[o("div",{staticClass:"meum"},e._l(e.meumbox,(function(t,a){return o("div",{key:a,staticClass:"box1"},[o("img",{staticStyle:{width:"100%",height:"100%"},attrs:{src:t.menuImg,alt:""},on:{click:function(o){return e.go(t.hrefurl)}}})])})),0),o("div",{staticClass:"nav"},e._l(e.navbox,(function(e,t){return o("div",{key:t,staticClass:"box2"},[o("img",{staticStyle:{width:"100%",height:"100%"},attrs:{src:e.navImg,alt:""}})])})),0),o("div",{staticClass:"main"},e._l(e.mainbox,(function(t,a){return o("div",{key:a,staticClass:"boxm",style:{backgroundImage:"url("+t.image+")"},on:{click:function(o){return e.goshopdetail(t.shopId)}}})})),0),e.loading?o("div",{staticClass:"loadingaaa"},[e._v("加载中...")]):e._e()]),o("footer-vue",{staticClass:"footer"})],1)}),[],!1,null,"97d8ea2d",null,null).exports)}}}))}();
