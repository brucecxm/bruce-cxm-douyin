import"./request.2e0c4af0.js";import{u as n}from"./video.68079bed.js";import{n as l}from"./index.368bb805.js";import"./token.138d03eb.js";const a={data(){return{selectedFile:null,videoTitle:"",videoContext:""}},methods:{gohome(){this.$router.push("/")},handleFileUpload(t){this.selectedFile=t.target.files[0]},upload(){const t=new Map;t.set("videoContext",this.videoContext),t.set("videoTitle",this.videoTitle),n(this.selectedFile,t)}}};var r=function(){var e=this,o=e._self._c;return o("div",[o("input",{attrs:{type:"file"},on:{change:e.handleFileUpload}}),o("input",{directives:[{name:"model",rawName:"v-model",value:e.videoTitle,expression:"videoTitle"}],attrs:{type:"text",placeholder:"\u89C6\u9891\u6807\u9898"},domProps:{value:e.videoTitle},on:{input:function(i){i.target.composing||(e.videoTitle=i.target.value)}}}),o("input",{directives:[{name:"model",rawName:"v-model",value:e.videoContext,expression:"videoContext"}],attrs:{type:"text",placeholder:"\u89C6\u9891\u6587\u6848"},domProps:{value:e.videoContext},on:{input:function(i){i.target.composing||(e.videoContext=i.target.value)}}}),o("button",{on:{click:e.upload}},[e._v("\u4E0A\u4F20\u6587\u4EF6")])])},s=[],d=l(a,r,s,!1,null,"e847f962",null,null);const m=d.exports;export{m as default};
