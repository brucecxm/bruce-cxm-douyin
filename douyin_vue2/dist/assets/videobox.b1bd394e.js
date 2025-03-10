import"./token.fe39c3a6.js";import{n as a,e as n}from"./index.f6d0a6bc.js";import{l as _,h as g}from"./video.c6d97de0.js";const p={props:{videoData:Object},data(){return{userId:"1",isLiked:!1,likeNum:this.videoData.likeNum,isCollected:!1,isCommentClicked:!1,isShareClicked:!1,videoDateOne:{}}},methods:{showcomment(){n.$emit("messageSent",!0),n.$emit("messageSent_videoid",this.videoDateOne),console.log(this.videoData)},toggleComment(){this.isCommentClicked=!0,setTimeout(()=>{this.isCommentClicked=!1},500),this.showcomment()},test(){console.log("test")},toggleLike(){this.isLiked=!this.isLiked,this.isLiked?this.likeNum+=1:this.likeNum-=1,this.behaviour("like"),_(this.videoData.videoId).then(t=>{console.log("\u70B9\u8D5E\u6210\u529F:",t)}).catch(t=>{console.error("\u70B9\u8D5E\u51FA\u9519:",t)})},toggleCollect(){this.isCollected=!this.isCollected,this.behaviour("collect")},toggleShare(){this.isShareClicked=!0,setTimeout(()=>{this.isShareClicked=!1},500),this.behaviour("share")},behaviour(t){console.log(t),console.log("behaviour called")},goauth(t){this.$router.push({path:`/auth/${t}`})},gomusic(t){this.$router.push({path:`/music/${t}`})}},mounted(){this.videoDateOne=this.videoData}};var r=function(){var e=this,i=e._self._c;return i("div",{staticClass:"videoaside"},[i("div",{staticClass:"touxiang avatar",on:{click:function(s){return e.goauth(e.videoData.userid)}}},[i("img",{staticStyle:{width:"1.4rem",height:"1.4rem"},attrs:{src:e.videoData.userAvatar,alt:"avatar"}})]),i("div",{staticClass:"aside like",class:{liked:e.isLiked},on:{click:e.toggleLike}}),i("div",{staticClass:"num"},[e._v(e._s(e.likeNum))]),e._v(" "),i("div",{staticClass:"aside comment",class:{clicked:e.isCommentClicked},on:{click:e.toggleComment}}),i("div",{staticClass:"num"},[e._v(e._s(e.videoData.commentNum))]),i("div",{staticClass:"aside collect",class:{collected:e.isCollected},on:{click:e.toggleCollect}}),i("div",{staticClass:"num"},[e._v(e._s(e.videoData.collectNum))]),i("div",{staticClass:"aside share",class:{clicked:e.isShareClicked},on:{click:e.toggleShare}}),i("div",{staticClass:"num"},[e._v(e._s(e.videoData.shareNum))]),i("div",{staticClass:"touxiang music",on:{click:function(s){return e.gomusic(e.videoData.music_id)}}},[i("img",{staticStyle:{width:"1.4rem",height:"1.4rem"},attrs:{src:e.videoData.musicAvatar,alt:"music"}})])])},b=[];r._withStripped=!0;var c=a(p,r,b,!1,null,"418fc459",null,null);c.options.__file="F:/GitHub/bruce-cxm-douyin/douyin_vue2/src/components/videoaside.vue";const x=c.exports;const f={props:{videoData:{type:Object,default(){return{collectNum:"",commentNum:"",likeNum:"",shareNum:"",userAvatar:"",userid:"",username:"",videoUrl:"",video_comment:"",videoid:""}}}},data(){return{}},methods:{testone(){console.log("sssssss")},goauth(t){this.$router.push({path:`/auth/${t}`})}}};var l=function(){var e=this,i=e._self._c;return i("div",{staticClass:"videoarticle",on:{click:e.testone}},[i("div",{staticClass:"username",on:{click:function(s){return e.goauth(e.videoData.userid)}}},[e._v(" "+e._s(e.videoData.username))]),i("div",{staticClass:"article",attrs:{div:""}},[e._v(e._s(e.videoData.video_comment))])])},C=[];l._withStripped=!0;var h=a(f,l,C,!1,null,"913b70d1",null,null);h.options.__file="F:/GitHub/bruce-cxm-douyin/douyin_vue2/src/components/videoarticle.vue";const k=h.exports;const y={components:{videoasideVue:x,videoarticleVue:k},data(){return{videoboxdata:[],isDragging:!1,startY:0,currentY:0,boxWidth:"100%",activeIndex:null,boxHeight:950,boxes:[],videodatahistory:[],page:1,pageSize:4,debounceTimer:null}},watch:{videoboxdata(t){this.updateBoxesBasedOnVideoData(t)}},created(){this.updateBoxesBasedOnVideoData(this.videoboxdata)},mounted(){this.loadVideos()},methods:{loadVideos(){g(this.page,this.pageSize,"tuijian").then(t=>{Array.isArray(t.data)&&t.data.length>0&&(this.videodatahistory.push(...t.data),this.videoboxdata=[...this.videodatahistory])}).catch(t=>{console.error("\u83B7\u53D6\u89C6\u9891\u51FA\u9519:",t)})},updateBoxesBasedOnVideoData(t){this.boxes=t.map((e,i)=>({color:"black",top:i*this.boxHeight})),this.updateBoxesPosition()},debounceLoadVideos(){this.debounceTimer&&clearTimeout(this.debounceTimer),this.debounceTimer=setTimeout(()=>{this.isAtBottom()&&(this.page++,this.loadVideos())},300)},updateVideoPlayback(){const t=this.$el.clientHeight,e=.9;this.$refs.videos.forEach((i,s)=>{const d=this.boxes[s].top,m=d+this.boxHeight*.1;(Math.min(t,m)-Math.max(0,d))/(this.boxHeight*.1)>=e?i.play():i.pause()})},startDrag(t){this.isDragging=!0,this.startY=this.getEventClientY(t)-this.currentY},stopDrag(t){this.isDragging=!1,this.updateBoxPositionBasedOnCurrentY(),this.updateVideoPlayback()},drag(t){if(this.isDragging){let e=this.getEventClientY(t)-this.startY;e=Math.max(-(this.boxHeight*(this.boxes.length-1)),Math.min(e,0)),this.currentY=e,this.updateBoxesPosition(),this.updateVideoPlayback(),this.debounceLoadVideos()}},getEventClientY(t){return t.type.startsWith("touch")?t.touches[0].clientY:t.clientY},updateBoxesPosition(){this.boxes.forEach((t,e)=>{t.top=this.currentY+e*this.boxHeight})},updateBoxPositionBasedOnCurrentY(){const t=Math.round(Math.abs(this.currentY)/this.boxHeight);this.currentY=-t*this.boxHeight,this.updateBoxesPosition()},isAtBottom(){const t=this.boxes[this.boxes.length-1],e=this.$el.clientHeight;return t.top+this.boxHeight*.1<=e},go(t){this.activeIndex=t;const e=-t*this.boxHeight;this.currentY=e,this.updateBoxesPosition(),this.updateVideoPlayback()}}};var u=function(){var e=this,i=e._self._c;return i("div",{staticClass:"container",style:{width:e.boxWidth,height:e.boxHeight*.1+"vh"},on:{mousedown:e.startDrag,mouseup:e.stopDrag,mousemove:e.drag,mouseleave:e.stopDrag,touchstart:e.startDrag,touchend:e.stopDrag,touchmove:e.drag}},e._l(e.boxes,function(s,o){return i("div",{key:o,staticClass:"box",style:{top:s.top*.1+"vh",backgroundColor:s.color,width:e.boxWidth,height:e.boxHeight*.1+"vh"}},[i("div",{staticClass:"videoasideone"},[i("videoaside-vue",{attrs:{"video-data":e.videoboxdata[o]}})],1),i("div",{staticClass:"videoarticle"},[i("videoarticle-vue",{attrs:{"video-data":e.videoboxdata[o]}})],1),i("video",{ref:"videos",refInFor:!0,staticClass:"videoone",attrs:{src:e.videoboxdata[o].videoUrl,preload:"true",loop:"","x5-video-player-type":"h5-page","x5-video-player-fullscreen":"false","webkit-playsinline":"true","x5-playsinline":"true",playsinline:"true"}},[i("p",[e._v("\u60A8\u7684\u6D4F\u89C8\u5668\u4E0D\u652F\u6301 video \u6807\u7B7E\u3002")])])])}),0)},D=[];u._withStripped=!0;var v=a(y,u,D,!1,null,"0d15d4e6",null,null);v.options.__file="F:/GitHub/bruce-cxm-douyin/douyin_vue2/src/components/videobox.vue";const $=v.exports,w=Object.freeze(Object.defineProperty({__proto__:null,default:$},Symbol.toStringTag,{value:"Module"}));export{x as a,k as b,w as c,$ as v};
