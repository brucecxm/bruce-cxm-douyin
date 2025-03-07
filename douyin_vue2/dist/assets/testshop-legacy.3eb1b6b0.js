!function(){function t(e){return t="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(t){return typeof t}:function(t){return t&&"function"==typeof Symbol&&t.constructor===Symbol&&t!==Symbol.prototype?"symbol":typeof t},t(e)}function e(){"use strict";/*! regenerator-runtime -- Copyright (c) 2014-present, Facebook, Inc. -- license (MIT): https://github.com/facebook/regenerator/blob/main/LICENSE */e=function(){return n};var r,n={},o=Object.prototype,a=o.hasOwnProperty,i=Object.defineProperty||function(t,e,r){t[e]=r.value},s="function"==typeof Symbol?Symbol:{},u=s.iterator||"@@iterator",c=s.asyncIterator||"@@asyncIterator",l=s.toStringTag||"@@toStringTag";function f(t,e,r){return Object.defineProperty(t,e,{value:r,enumerable:!0,configurable:!0,writable:!0}),t[e]}try{f({},"")}catch(r){f=function(t,e,r){return t[e]=r}}function h(t,e,r,n){var o=e&&e.prototype instanceof b?e:b,a=Object.create(o.prototype),s=new T(n||[]);return i(a,"_invoke",{value:A(t,r,s)}),a}function d(t,e,r){try{return{type:"normal",arg:t.call(e,r)}}catch(t){return{type:"throw",arg:t}}}n.wrap=h;var p="suspendedStart",v="suspendedYield",m="executing",y="completed",g={};function b(){}function w(){}function _(){}var x={};f(x,u,(function(){return this}));var E=Object.getPrototypeOf,L=E&&E(E(N([])));L&&L!==o&&a.call(L,u)&&(x=L);var j=_.prototype=b.prototype=Object.create(x);function P(t){["next","throw","return"].forEach((function(e){f(t,e,(function(t){return this._invoke(e,t)}))}))}function U(e,r){function n(o,i,s,u){var c=d(e[o],e,i);if("throw"!==c.type){var l=c.arg,f=l.value;return f&&"object"==t(f)&&a.call(f,"__await")?r.resolve(f.__await).then((function(t){n("next",t,s,u)}),(function(t){n("throw",t,s,u)})):r.resolve(f).then((function(t){l.value=t,s(l)}),(function(t){return n("throw",t,s,u)}))}u(c.arg)}var o;i(this,"_invoke",{value:function(t,e){function a(){return new r((function(r,o){n(t,e,r,o)}))}return o=o?o.then(a,a):a()}})}function A(t,e,n){var o=p;return function(a,i){if(o===m)throw Error("Generator is already running");if(o===y){if("throw"===a)throw i;return{value:r,done:!0}}for(n.method=a,n.arg=i;;){var s=n.delegate;if(s){var u=O(s,n);if(u){if(u===g)continue;return u}}if("next"===n.method)n.sent=n._sent=n.arg;else if("throw"===n.method){if(o===p)throw o=y,n.arg;n.dispatchException(n.arg)}else"return"===n.method&&n.abrupt("return",n.arg);o=m;var c=d(t,e,n);if("normal"===c.type){if(o=n.done?y:v,c.arg===g)continue;return{value:c.arg,done:n.done}}"throw"===c.type&&(o=y,n.method="throw",n.arg=c.arg)}}}function O(t,e){var n=e.method,o=t.iterator[n];if(o===r)return e.delegate=null,"throw"===n&&t.iterator.return&&(e.method="return",e.arg=r,O(t,e),"throw"===e.method)||"return"!==n&&(e.method="throw",e.arg=new TypeError("The iterator does not provide a '"+n+"' method")),g;var a=d(o,t.iterator,e.arg);if("throw"===a.type)return e.method="throw",e.arg=a.arg,e.delegate=null,g;var i=a.arg;return i?i.done?(e[t.resultName]=i.value,e.next=t.nextLoc,"return"!==e.method&&(e.method="next",e.arg=r),e.delegate=null,g):i:(e.method="throw",e.arg=new TypeError("iterator result is not an object"),e.delegate=null,g)}function k(t){var e={tryLoc:t[0]};1 in t&&(e.catchLoc=t[1]),2 in t&&(e.finallyLoc=t[2],e.afterLoc=t[3]),this.tryEntries.push(e)}function C(t){var e=t.completion||{};e.type="normal",delete e.arg,t.completion=e}function T(t){this.tryEntries=[{tryLoc:"root"}],t.forEach(k,this),this.reset(!0)}function N(e){if(e||""===e){var n=e[u];if(n)return n.call(e);if("function"==typeof e.next)return e;if(!isNaN(e.length)){var o=-1,i=function t(){for(;++o<e.length;)if(a.call(e,o))return t.value=e[o],t.done=!1,t;return t.value=r,t.done=!0,t};return i.next=i}}throw new TypeError(t(e)+" is not iterable")}return w.prototype=_,i(j,"constructor",{value:_,configurable:!0}),i(_,"constructor",{value:w,configurable:!0}),w.displayName=f(_,l,"GeneratorFunction"),n.isGeneratorFunction=function(t){var e="function"==typeof t&&t.constructor;return!!e&&(e===w||"GeneratorFunction"===(e.displayName||e.name))},n.mark=function(t){return Object.setPrototypeOf?Object.setPrototypeOf(t,_):(t.__proto__=_,f(t,l,"GeneratorFunction")),t.prototype=Object.create(j),t},n.awrap=function(t){return{__await:t}},P(U.prototype),f(U.prototype,c,(function(){return this})),n.AsyncIterator=U,n.async=function(t,e,r,o,a){void 0===a&&(a=Promise);var i=new U(h(t,e,r,o),a);return n.isGeneratorFunction(e)?i:i.next().then((function(t){return t.done?t.value:i.next()}))},P(j),f(j,l,"Generator"),f(j,u,(function(){return this})),f(j,"toString",(function(){return"[object Generator]"})),n.keys=function(t){var e=Object(t),r=[];for(var n in e)r.push(n);return r.reverse(),function t(){for(;r.length;){var n=r.pop();if(n in e)return t.value=n,t.done=!1,t}return t.done=!0,t}},n.values=N,T.prototype={constructor:T,reset:function(t){if(this.prev=0,this.next=0,this.sent=this._sent=r,this.done=!1,this.delegate=null,this.method="next",this.arg=r,this.tryEntries.forEach(C),!t)for(var e in this)"t"===e.charAt(0)&&a.call(this,e)&&!isNaN(+e.slice(1))&&(this[e]=r)},stop:function(){this.done=!0;var t=this.tryEntries[0].completion;if("throw"===t.type)throw t.arg;return this.rval},dispatchException:function(t){if(this.done)throw t;var e=this;function n(n,o){return s.type="throw",s.arg=t,e.next=n,o&&(e.method="next",e.arg=r),!!o}for(var o=this.tryEntries.length-1;o>=0;--o){var i=this.tryEntries[o],s=i.completion;if("root"===i.tryLoc)return n("end");if(i.tryLoc<=this.prev){var u=a.call(i,"catchLoc"),c=a.call(i,"finallyLoc");if(u&&c){if(this.prev<i.catchLoc)return n(i.catchLoc,!0);if(this.prev<i.finallyLoc)return n(i.finallyLoc)}else if(u){if(this.prev<i.catchLoc)return n(i.catchLoc,!0)}else{if(!c)throw Error("try statement without catch or finally");if(this.prev<i.finallyLoc)return n(i.finallyLoc)}}}},abrupt:function(t,e){for(var r=this.tryEntries.length-1;r>=0;--r){var n=this.tryEntries[r];if(n.tryLoc<=this.prev&&a.call(n,"finallyLoc")&&this.prev<n.finallyLoc){var o=n;break}}o&&("break"===t||"continue"===t)&&o.tryLoc<=e&&e<=o.finallyLoc&&(o=null);var i=o?o.completion:{};return i.type=t,i.arg=e,o?(this.method="next",this.next=o.finallyLoc,g):this.complete(i)},complete:function(t,e){if("throw"===t.type)throw t.arg;return"break"===t.type||"continue"===t.type?this.next=t.arg:"return"===t.type?(this.rval=this.arg=t.arg,this.method="return",this.next="end"):"normal"===t.type&&e&&(this.next=e),g},finish:function(t){for(var e=this.tryEntries.length-1;e>=0;--e){var r=this.tryEntries[e];if(r.finallyLoc===t)return this.complete(r.completion,r.afterLoc),C(r),g}},catch:function(t){for(var e=this.tryEntries.length-1;e>=0;--e){var r=this.tryEntries[e];if(r.tryLoc===t){var n=r.completion;if("throw"===n.type){var o=n.arg;C(r)}return o}}throw Error("illegal catch attempt")},delegateYield:function(t,e,n){return this.delegate={iterator:N(t),resultName:e,nextLoc:n},"next"===this.method&&(this.arg=r),g}},n}function r(t,e,r,n,o,a,i){try{var s=t[a](i),u=s.value}catch(t){return void r(t)}s.done?e(u):Promise.resolve(u).then(n,o)}System.register(["./index-legacy.a351f814.js"],(function(t,n){"use strict";var o,a=document.createElement("style");return a.textContent=".error[data-v-14d7dba6]{color:red}.error-message[data-v-14d7dba6]{color:red;font-size:12px;margin-left:10px}.loading[data-v-14d7dba6]{font-size:18px;color:#888}button[data-v-14d7dba6]:disabled{background-color:#ddd}\n",document.head.appendChild(a),{setters:[function(t){o=t.n}],execute:function(){var n={data:function(){return{user:null,isLoading:!1,error:null,avatarPreview:null,formErrors:{}}},computed:{isFormInvalid:function(){return Object.keys(this.formErrors).length>0||!this.user.name||!this.user.email}},beforeCreate:function(){var t=this;setTimeout((function(){console.log("beforeCreate: 实例化前"),t.$emit("beforeAppInit","app is about to be initialized")}),1e3)},created:function(){var t=this;setTimeout((function(){console.log("created: 实例已创建"),t.fetchUserData(),t.$emit("createdApp","app is now created")}),1e3)},beforeMount:function(){var t=this;setTimeout((function(){console.log("beforeMount: 渲染前"),t.checkUserCache()}),1e3)},mounted:function(){var t=this;setTimeout((function(){console.log("mounted: 组件挂载到 DOM 后"),t.initializeAvatarPreview()}),1e3)},beforeUpdate:function(){var t=this;setTimeout((function(){console.log("beforeUpdate: 数据即将更新"),t.cleanupBeforeUpdate()}),1e3)},updated:function(){var t=this;setTimeout((function(){console.log("updated: 数据更新后"),t.sendUserUpdateNotification()}),1e3)},methods:{fetchUserData:function(){var t,n=this;return(t=e().mark((function t(){var r;return e().wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return n.isLoading=!0,t.prev=1,t.next=4,n.simulateApiRequest();case 4:r=t.sent,n.user=r.data,console.log("User data fetched successfully",n.user),t.next=13;break;case 9:t.prev=9,t.t0=t.catch(1),n.error="Failed to fetch user data",console.error(t.t0);case 13:return t.prev=13,n.isLoading=!1,t.finish(13);case 16:case"end":return t.stop()}}),t,null,[[1,9,13,16]])})),function(){var e=this,n=arguments;return new Promise((function(o,a){var i=t.apply(e,n);function s(t){r(i,o,a,s,u,"next",t)}function u(t){r(i,o,a,s,u,"throw",t)}s(void 0)}))})()},simulateApiRequest:function(){return new Promise((function(t,e){setTimeout((function(){t({data:{name:"Jane Doe",email:"janedoe@example.com",avatar:"https://emoji.bj.bcebos.com/yige-aigc/index_aigc/final/toolspics/1.png"}})}),1e3)}))},updateUserData:function(){var t=this;if(console.log("updateUserData: 提交用户数据"),this.isLoading=!0,this.formErrors={},this.user.name||(this.formErrors.name="Name is required"),this.user.email&&this.isValidEmail(this.user.email)||(this.formErrors.email="A valid email is required"),this.avatarPreview||(this.formErrors.avatar="Avatar is required"),Object.keys(this.formErrors).length>0)this.isLoading=!1;else{var e={name:"John Smith",email:"johnsmith@example.com",avatar:"https://emoji.bj.bcebos.com/yige-aigc/index_aigc/final/toolspics/1.png"};setTimeout((function(){t.user=e,console.log("User updated successfully",t.user),alert("User data updated successfully!"),t.isLoading=!1}),1e3)}},isValidEmail:function(t){return/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/.test(t)},onAvatarChange:function(t){var e=this,r=t.target.files[0];if(r){if(!r.type.startsWith("image/"))return void(this.formErrors.avatar="Only image files are allowed");var n=new FileReader;n.onload=function(){e.avatarPreview=n.result},n.readAsDataURL(r)}},checkUserCache:function(){console.log("Checking if user data is cached")},initializeAvatarPreview:function(){!this.avatarPreview&&this.user&&this.user.avatar&&(this.avatarPreview=this.user.avatar)},cleanupBeforeUpdate:function(){console.log("Cleaning up before update")},sendUserUpdateNotification:function(){console.log("User data updated, notifying other components or systems")}},beforeDestroy:function(){var t=this;setTimeout((function(){console.log("beforeDestroy: 组件销毁前"),t.$emit("beforeComponentDestroy","cleaning up before destruction")}),1e3)},destroyed:function(){var t=this;setTimeout((function(){console.log("destroyed: 组件销毁后"),t.$emit("componentDestroyed","component has been destroyed")}),1e3)}},a=function(){var t=this,e=t._self._c;return t.user?e("div",[e("h1",[t._v("User Information")]),t.isLoading?e("div",{staticClass:"loading"},[t._v("Loading...")]):t._e(),t.error?e("div",{staticClass:"error"},[t._v(t._s(t.error))]):t._e(),t.isLoading||t.error?t._e():e("div",[e("form",{on:{submit:function(e){return e.preventDefault(),t.updateUserData.apply(null,arguments)}}},[e("label",[t._v(" Name: "),e("input",{directives:[{name:"model",rawName:"v-model",value:t.user.name,expression:"user.name"}],attrs:{type:"text"},domProps:{value:t.user.name},on:{input:function(e){e.target.composing||t.$set(t.user,"name",e.target.value)}}}),t.formErrors.name?e("span",{staticClass:"error-message"},[t._v(t._s(t.formErrors.name))]):t._e()]),e("br"),e("label",[t._v(" Email: "),e("input",{directives:[{name:"model",rawName:"v-model",value:t.user.email,expression:"user.email"}],attrs:{type:"email"},domProps:{value:t.user.email},on:{input:function(e){e.target.composing||t.$set(t.user,"email",e.target.value)}}}),t.formErrors.email?e("span",{staticClass:"error-message"},[t._v(t._s(t.formErrors.email))]):t._e()]),e("br"),e("label",[t._v(" Avatar: "),e("input",{attrs:{type:"file"},on:{change:t.onAvatarChange}}),t.formErrors.avatar?e("span",{staticClass:"error-message"},[t._v(t._s(t.formErrors.avatar))]):t._e()]),e("br"),e("button",{attrs:{type:"submit",disabled:t.isFormInvalid}},[t._v("Save")])]),t.avatarPreview?e("div",[e("h2",[t._v("Avatar Preview")]),e("img",{attrs:{src:t.avatarPreview,alt:"Avatar",width:"100"}})]):t._e()])]):t._e()};t("default",o(n,a,[],!1,null,"14d7dba6",null,null).exports)}}}))}();
