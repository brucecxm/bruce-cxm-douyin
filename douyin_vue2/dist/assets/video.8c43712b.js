import{i as t}from"./request.b4f983bf.js";const m=(e,o,n)=>t.get("/video/test",{params:{page:e,size:o,videoType:n}}),a=()=>t.get("/video/more"),p=e=>t.get("/comment/getcomment",{params:e}),u=e=>t.get("/video/auth",{params:{userid:e}}),g=e=>t.post("/video/like",{params:e}),i=(e,o)=>{const n=new FormData;if(n.append("file",e),o instanceof Map)o.forEach((r,s)=>{n.append(s,r)});else for(const r in o)o.hasOwnProperty(r)&&n.append(r,o[r]);return t.post("/video/upvideo",n,{headers:{"Content-Type":"multipart/form-data"}})},f=e=>t.get("/comment/getfu",{params:e}),h=e=>t.get("/music/getmusic",{params:{music_id:e}});export{p as a,a as b,u as c,h as d,f as g,m as h,g as l,i as u};
