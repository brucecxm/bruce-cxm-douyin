System.register(["./request-legacy.314233a9.js","./video-legacy.6510fc3a.js","./index-legacy.a351f814.js","./token-legacy.d0b3a640.js"],(function(e,t){"use strict";var n,o,i=document.createElement("style");return i.textContent="",document.head.appendChild(i),{setters:[function(){},function(e){n=e.u},function(e){o=e.n},function(){}],execute:function(){e("default",o({data:function(){return{selectedFile:null,videoTitle:"",videoContext:""}},methods:{gohome:function(){this.$router.push("/")},handleFileUpload:function(e){this.selectedFile=e.target.files[0]},upload:function(){var e=new Map;e.set("videoContext",this.videoContext),e.set("videoTitle",this.videoTitle),n(this.selectedFile,e)}}},(function(){var e=this,t=e._self._c;return t("div",[t("input",{attrs:{type:"file"},on:{change:e.handleFileUpload}}),t("input",{directives:[{name:"model",rawName:"v-model",value:e.videoTitle,expression:"videoTitle"}],attrs:{type:"text",placeholder:"视频标题"},domProps:{value:e.videoTitle},on:{input:function(t){t.target.composing||(e.videoTitle=t.target.value)}}}),t("input",{directives:[{name:"model",rawName:"v-model",value:e.videoContext,expression:"videoContext"}],attrs:{type:"text",placeholder:"视频文案"},domProps:{value:e.videoContext},on:{input:function(t){t.target.composing||(e.videoContext=t.target.value)}}}),t("button",{on:{click:e.upload}},[e._v("上传文件")])])}),[],!1,null,"e847f962",null,null).exports)}}}));
