import{s as h,a as c}from"./shop.a32a9985.js";import{i as g,a as m,b as u,c as d,d as i,e as f,f as v,g as p,h as x,j as n,k as _,l as b,m as I,n as l,s as k,o as C}from"./4.b829fc46.js";import{u as S}from"./token.c8436611.js";import{f as y}from"./footer.ab6ac887.js";import{n as T}from"./index.d6cacc81.js";import"./request.b4f983bf.js";const w={beforeDestroy(){document.removeEventListener("click",this.handleOutsideClick)},components:{footerVue:y},data(){return{searchText:"",showSuggestions:!1,suggestions:["\u82F9\u679C","\u9999\u8549","\u6A59\u5B50","\u8461\u8404","\u897F\u74DC","\u8349\u8393","\u8292\u679C"],searchlian:[{id:1,name:"ssadsas"},{id:2,name:"ssdasss"},{id:3,name:"dsadssss"}],search:"",isFocused:!1,boxes:Array.from({length:50},(e,s)=>s+1),loading:!1,meumbox:[{name:"\u8D85d\u5E02",menuImg:g,hrefurl:"order"},{name:"\u8D85d\u5E02",menuImg:m,hrefurl:"chongzhi"},{name:"\u8D85d\u5E02",menuImg:u,hrefurl:"shopmessage"},{name:"\u8D85d\u5E02",menuImg:d,hrefurl:""},{name:"\u8D85d\u5E02",menuImg:i,hrefurl:""},{name:"\u8D85d\u5E02",menuImg:i,hrefurl:""},{name:"\u8D85d\u5E02",menuImg:i,hrefurl:""}],navbox:[{navname:"ss",navImg:f,navhrefurl:"order"},{navname:"ss",navImg:v,navhrefurl:"chongzhi"},{navname:"ss",navImg:p,navhrefurl:"shopmessage"},{navname:"ss",navImg:x,navhrefurl:"order"},{navname:"ss",navImg:n,navhrefurl:""},{navname:"ss",navImg:n,navhrefurl:""},{navname:"ss",navImg:n,navhrefurl:""},{navname:"ss",navImg:n,navhrefurl:""},{navname:"ss",navImg:n,navhrefurl:""},{navname:"ss",navImg:n,navhrefurl:""}],mainbox:[{image:_,name:"\u5546\u54C11",hrefurl:"/shopdetail/1",shopId:"25"},{image:b,name:"\u5546\u54C11",hrefurl:"/shopdetail/1",shopId:"55"},{image:I,name:"\u5546\u54C11",hrefurl:"/shopdetail/1",shopId:"66"},{image:l,name:"\u5546\u54C11",hrefurl:"/shopdetail/1",shopId:"55"},{image:l,name:"\u5546\u54C11",hrefurl:"/shopdetail/1",shopId:"4"},{image:l,name:"\u5546\u54C11",hrefurl:"/shopdetail/1",shopId:"5"}],pagenum:1,size:10}},computed:{filteredSuggestions(){return this.suggestions.filter(e=>e.toLowerCase().includes(this.searchText.toLowerCase()))}},mounted(){document.addEventListener("click",this.handleOutsideClick),h(this.pagenum,this.size).then(e=>{console.log(e),e&&e.data&&e.data.data?(this.mainbox=e.data.data.records,console.log(this.mainbox)):console.log("\u83B7\u53D6 shoplist \u6570\u636E\u5931\u8D25: \u6570\u636E\u65E0\u6548")}).catch(e=>{console.log("\u83B7\u53D6 shoplist \u6570\u636E\u51FA\u9519:",e),console.log("\u83B7\u53D6 shoplist \u6570\u636E\u51FA\u9519")}),c().then(e=>{console.log(e),e&&e.data&&e.data.data?(this.navbox=e.data.data,console.log(this.navbox)):console.log("\u83B7\u53D6 shoplist \u6570\u636E\u5931\u8D25: \u6570\u636E\u65E0\u6548")}).catch(e=>{console.log("\u83B7\u53D6 shoplist \u6570\u636E\u51FA\u9519:",e),console.log("\u83B7\u53D6 shoplist \u6570\u636E\u51FA\u9519")}),k().then(e=>{console.log(e),e&&e.data&&e.data.data?(this.meumbox=e.data.data.records,console.log(this.meumbox)):console.log("\u83B7\u53D6 shopmenulist \u6570\u636E\u5931\u8D25: \u6570\u636E\u65E0\u6548")}).catch(e=>{console.log("\u83B7\u53D6 shopmenulist \u6570\u636E\u51FA\u9519:",e),console.log("\u83B7\u53D6 shopmenulist \u6570\u636E\u51FA\u9519")}),C().then(e=>{console.log(e),e&&e.data&&e.data.data?(this.navbox=e.data.data.records,console.log(this.navbox)):console.log("\u83B7\u53D6 shopnavlist \u6570\u636E\u5931\u8D25: \u6570\u636E\u65E0\u6548")}).catch(e=>{console.log("\u83B7\u53D6 shopnavlist \u6570\u636E\u51FA\u9519:",e),console.log("\u83B7\u53D6 shopnavlist \u6570\u636E\u51FA\u9519")})},methods:{selectSuggestion(e){this.searchText=e,this.showSuggestions=!1},hideSuggestions(){setTimeout(()=>{this.showSuggestions=!1},100)},handleOutsideClick(e){const s=this.$refs.searchBox,a=this.$refs.suggestionBox;!s.contains(e.target)&&!a.contains(e.target)&&(this.showSuggestions=!1)},searchmeth(){console.log("\u641C\u7D22\u5185\u5BB9:",this.searchText),this.$router.push({path:"/searchshop",query:{search:this.searchText}})},submitlian(e){this.search=e},handleScroll(e){const s=e.target;s.scrollHeight<=s.scrollTop+s.clientHeight&&!this.loading&&this.loadMoreBoxes()},loadMoreBoxes(){this.loading=!0,setTimeout(()=>{h(this.pagenum,this.size).then(e=>{if(console.log(e),e&&e.data&&e.data.data){const s=e.data.data.records;Array.isArray(s)?(console.log("New data:",s),this.mainbox.push(...s),this.pagenum++,console.log(this.mainbox)):console.log("\u65B0\u6570\u636E\u4E0D\u662F\u6570\u7EC4:",s)}else console.log("\u83B7\u53D6 shoplist \u6570\u636E\u5931\u8D25: \u6570\u636E\u65E0\u6548")}).catch(e=>{console.log("\u83B7\u53D6 shoplist \u6570\u636E\u51FA\u9519:",e)}),this.loading=!1},1e3)},go(e){console.log(e)},goshopdetail(e){console.log(e)},go(e){const a=S().getToken;this.$router.push({path:`/${e}/${a}`})},handleFocus(){this.isFocused=!0},handleBlur(){this.isFocused=!1},goshopdetail(e){this.$router.push({path:`/shopdetail/${e}`})},getmenu(){},getnavone(){c()},getshopinfo(){},getinputciyu(){},startsearch(){},gettuijian(){}}};var $=function(){var s=this,a=s._self._c;return a("div",{staticClass:"ssss"},[a("div",{staticClass:"header"},[a("div",{ref:"searchBox",staticClass:"search"},[a("button",{on:{click:s.searchmeth}},[s._v("\u641C\u7D22")]),a("input",{directives:[{name:"model",rawName:"v-model",value:s.searchText,expression:"searchText"}],attrs:{type:"text",placeholder:"\u8F93\u5165\u641C\u7D22\u5185\u5BB9"},domProps:{value:s.searchText},on:{focus:function(t){s.showSuggestions=!0},blur:s.hideSuggestions,input:function(t){t.target.composing||(s.searchText=t.target.value)}}})]),a("div",{ref:"suggestionBox",staticClass:"lian"},[s.showSuggestions&&s.filteredSuggestions.length>0?a("ul",s._l(s.filteredSuggestions,function(t,o){return a("li",{key:o,on:{click:function(r){return s.selectSuggestion(t)}}},[s._v(" "+s._s(t)+" ")])}),0):s._e()])]),a("div",{staticClass:"box-containeraaa",on:{scroll:s.handleScroll}},[a("div",{staticClass:"meum"},s._l(s.meumbox,function(t,o){return a("div",{key:o,staticClass:"box1"},[a("img",{staticStyle:{width:"100%",height:"100%"},attrs:{src:t.menuImg,alt:""},on:{click:function(r){return s.go(t.hrefurl)}}})])}),0),a("div",{staticClass:"nav"},s._l(s.navbox,function(t,o){return a("div",{key:o,staticClass:"box2"},[a("img",{staticStyle:{width:"100%",height:"100%"},attrs:{src:t.navImg,alt:""}})])}),0),a("div",{staticClass:"main"},s._l(s.mainbox,function(t,o){return a("div",{key:o,staticClass:"boxm",style:{backgroundImage:"url("+t.image+")"},on:{click:function(r){return s.goshopdetail(t.shopId)}}})}),0),s.loading?a("div",{staticClass:"loadingaaa"},[s._v("\u52A0\u8F7D\u4E2D...")]):s._e()]),a("footer-vue",{staticClass:"footer"})],1)},B=[],z=T(w,$,B,!1,null,"97d8ea2d",null,null);const H=z.exports;export{H as default};
