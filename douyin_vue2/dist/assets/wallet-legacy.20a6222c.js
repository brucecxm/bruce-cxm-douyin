System.register(["./index-legacy.d81978a6.js"],(function(t,a){"use strict";var e,i=document.createElement("style");return i.textContent=".wallet-container[data-v-53850aa8]{width:100%;height:100%;overflow:auto;font-family:Arial,sans-serif}.header[data-v-53850aa8]{background-color:#007bff;color:#fff;padding:10px;display:flex;justify-content:space-between;align-items:center}.balance[data-v-53850aa8]{font-size:18px;font-weight:700}.actions button[data-v-53850aa8]{margin-left:10px;padding:5px 10px;background-color:#28a745;color:#fff;border:none;cursor:pointer}.actions button[data-v-53850aa8]:nth-child(2){background-color:#dc3545}.dialog[data-v-53850aa8]{position:fixed;top:0;left:0;width:100%;height:100%;background-color:rgba(0,0,0,.5);display:flex;justify-content:center;align-items:center}.dialog-content[data-v-53850aa8]{background-color:#fff;padding:20px;border-radius:8px;text-align:center}.dialog input[data-v-53850aa8]{padding:8px;margin:10px 0;width:200px}.dialog button[data-v-53850aa8]{margin-top:10px;padding:5px 10px;background-color:#007bff;color:#fff;border:none;cursor:pointer}.dialog button[data-v-53850aa8]:nth-child(2){background-color:#dc3545}.expenditure[data-v-53850aa8]{margin-top:20px}.expenditure .item[data-v-53850aa8]{margin-bottom:10px;padding:10px;border:1px solid #ccc;display:flex;justify-content:space-between}\n",document.head.appendChild(i),{setters:[function(t){e=t.n}],execute:function(){var a={data:function(){return{balance:500,showRecharge:!1,showWithdraw:!1,rechargeAmount:0,withdrawAmount:0,expenditures:[{id:1,date:"2025-01-20",description:"购买商品 A",amount:100},{id:2,date:"2025-01-18",description:"购买商品 B",amount:50}]}},mounted:function(){},methods:{showRechargeDialog:function(){this.showRecharge=!0},showWithdrawDialog:function(){this.showWithdraw=!0},closeDialog:function(){this.showRecharge=!1,this.showWithdraw=!1},recharge:function(){this.rechargeAmount>0&&(this.balance+=this.rechargeAmount,this.rechargeAmount=0,this.closeDialog())},withdraw:function(){this.withdrawAmount>0&&this.withdrawAmount<=this.balance?(this.balance-=this.withdrawAmount,this.withdrawAmount=0,this.closeDialog()):alert("提现金额不足或无效")}}},i=function(){var t=this,a=t._self._c;return a("div",{staticClass:"wallet-container"},[a("div",{staticClass:"header"},[a("div",{staticClass:"balance"},[t._v("余额: ¥"+t._s(t.balance))]),a("div",{staticClass:"actions"},[a("button",{on:{click:t.showRechargeDialog}},[t._v("充值")]),a("button",{on:{click:t.showWithdrawDialog}},[t._v("提现")])])]),t.showRecharge?a("div",{staticClass:"dialog"},[a("div",{staticClass:"dialog-content"},[a("h3",[t._v("充值")]),a("input",{directives:[{name:"model",rawName:"v-model",value:t.rechargeAmount,expression:"rechargeAmount"}],attrs:{type:"number",placeholder:"输入充值金额"},domProps:{value:t.rechargeAmount},on:{input:function(a){a.target.composing||(t.rechargeAmount=a.target.value)}}}),a("button",{on:{click:t.recharge}},[t._v("确认充值")]),a("button",{on:{click:t.closeDialog}},[t._v("取消")])])]):t._e(),t.showWithdraw?a("div",{staticClass:"dialog"},[a("div",{staticClass:"dialog-content"},[a("h3",[t._v("提现")]),a("input",{directives:[{name:"model",rawName:"v-model",value:t.withdrawAmount,expression:"withdrawAmount"}],attrs:{type:"number",placeholder:"输入提现金额"},domProps:{value:t.withdrawAmount},on:{input:function(a){a.target.composing||(t.withdrawAmount=a.target.value)}}}),a("button",{on:{click:t.withdraw}},[t._v("确认提现")]),a("button",{on:{click:t.closeDialog}},[t._v("取消")])])]):t._e(),a("div",{staticClass:"expenditure"},[a("h3",[t._v("支出明细")]),t._l(t.expenditures,(function(e){return a("div",{key:e.id,staticClass:"item"},[a("div",[t._v(t._s(e.date)+" - "+t._s(e.description))]),a("div",[t._v("¥"+t._s(e.amount))])])}))],2)])},o=[];i._withStripped=!0;var n=e(a,i,o,!1,null,"53850aa8",null,null);n.options.__file="F:/GitHub/bruce-cxm-douyin/douyin_vue2/src/views/shop/wallet.vue",t("default",n.exports)}}}));
