System.register(["./index-legacy.7979773c.js"],(function(t,e){"use strict";var i,a=document.createElement("style");return a.textContent=".cart-container[data-v-c674c662]{width:100%;height:100%;display:flex;flex-direction:column}.header[data-v-c674c662],.shipping-info[data-v-c674c662]{display:flex;justify-content:space-between;padding:10px}.cart-item[data-v-c674c662]{display:flex;align-items:center;padding:10px;border-bottom:1px solid #eee}.item-image[data-v-c674c662]{width:50px;height:50px}.item-image img[data-v-c674c662]{width:100%;height:100%}.item-details[data-v-c674c662]{margin-left:10px}.quantity-control[data-v-c674c662]{margin-left:auto}.footer[data-v-c674c662]{position:fixed;bottom:0;left:0;width:100%;background-color:#f0f0f0;padding:10px;display:flex;justify-content:space-between}.checkout-button[data-v-c674c662]{background-color:#4885ed;color:#fff;padding:10px 20px;border:none}.select-circle[data-v-c674c662]{width:16px;height:16px;border-radius:50%;border:1px solid #ccc;margin-right:10px}\n",document.head.appendChild(a),{setters:[function(t){i=t.n}],execute:function(){t("default",i({data:function(){return{cartItems:[{id:1,image:"../../assets/op.jpg",price:666,name:"商品名称写这里",specs:"粉丝, 22码",quantity:1},{id:2,image:"../../assets/op.jpg",price:666,name:"商品名称写这里",specs:"粉丝, 22码",quantity:1},{id:3,image:"../../assets/op.jpg",price:666,name:"商品名称写这里",specs:"粉丝, 22码",quantity:1},{id:4,image:"../../assets/op.jpg",price:666,name:"商品名称写这里",specs:"粉丝, 22码",quantity:1}]}},computed:{selectedCount:function(){return this.cartItems.length},totalPrice:function(){return this.cartItems.reduce((function(t,e){return t+e.price*e.quantity}),0)}},methods:{incrementQuantity:function(t){t.quantity++},decrementQuantity:function(t){t.quantity>1&&t.quantity--}}},(function(){var t=this,e=t._self._c;return e("div",{staticClass:"cart-container"},[t._m(0),t._m(1),t._l(t.cartItems,(function(i){return e("div",{key:i.id,staticClass:"cart-item",staticStyle:{"font-size":"0.5rem"}},[e("div",{staticClass:"select-circle"}),e("div",{staticClass:"item-image"},[e("img",{attrs:{src:i.image,alt:"Product Image"}})]),e("div",{staticClass:"item-details"},[e("div",{staticClass:"item-name"},[t._v(t._s(i.name))]),e("div",{staticClass:"item-specs"},[t._v(t._s(i.specs))]),e("div",{staticClass:"item-price"},[t._v("¥ "+t._s(i.price))])]),e("div",{staticClass:"quantity-control"},[e("button",{on:{click:function(e){return t.decrementQuantity(i)}}},[t._v("-")]),e("span",[t._v(t._s(i.quantity))]),e("button",{on:{click:function(e){return t.incrementQuantity(i)}}},[t._v("+")])])])})),e("div",{staticClass:"footer",staticStyle:{"font-size":"0.8rem"}},[e("div",{staticClass:"selected-items"},[e("div",{staticClass:"select-circle"}),e("span",[t._v("已选 ("+t._s(t.selectedCount)+")")])]),e("div",{staticClass:"total-price"},[t._v("合计: ¥ "+t._s(t.totalPrice))]),e("button",{staticClass:"checkout-button"},[t._v("下单")])])],2)}),[function(){var t=this,e=t._self._c;return e("div",{staticClass:"header",staticStyle:{"font-size":"0.7rem"}},[e("span",{staticClass:"title"},[t._v("购物车")]),e("span",{staticClass:"edit"},[t._v("编辑")])])},function(){var t=this,e=t._self._c;return e("div",{staticClass:"shipping-info",staticStyle:{"font-size":"0.5rem"}},[e("span",[t._v("再购 ¥ 66 可免邮费哦~")]),e("span",{staticClass:"make-up"},[t._v("凑单 >")])])}],!1,null,"c674c662",null,null).exports)}}}));
