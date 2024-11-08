var spaceCounter = 2; // 从2开始，因为已经有两个空
// 数字转中文函数
function numberToChinese(num) {
  var chineseNumbers = [
    "一",
    "二",
    "三",
    "四",
    "五",
    "六",
    "七",
    "八",
    "九",
    "十",
  ];
  if (num <= 10) {
    return chineseNumbers[num - 1];
  } else {
    return "十" + (num > 10 ? chineseNumbers[num - 11] : "");
  }
}
//填空题事件
function calculationEvent() {
  //给多选框绑定点击事件，给答案设值
  jQ("input[name=calculation_val]").click(function (ele) {
    var getAllVal = "";

    jQ("input[name=calculation_val]:checked").each(function (index, ele) {
      var getVal = jQ(ele).val() + ",";
      getAllVal = getAllVal + getVal;
    });
    jQ("#question_answer").val(getAllVal.substr(0, getAllVal.length - 1));
    jQ(".AnserText").text(getAllVal.substr(0, getAllVal.length - 1));
  });
  // 在函数外部或适当的作用域内定义一个计数器
  // 填空题选项新增事件
  jQ("#calculationAdd").click(function () {
    var addNum = jQ("li[name='calculationNewAdd']").length;
    if (addNum == 8) {
      commAlert.alert("选项数量最多为10项", "warn");
      return false;
    }
    // 增加计数器
    spaceCounter++;
    var html = "";
    html += '<li name="calculationNewAdd">';
    html += '<label class="zte-checkbox">';
    html +=
      '<span class="zte-checkbox-label" style="font-size: 14px;padding: 0 5px 0 0">第' +
      numberToChinese(spaceCounter) +
      "空</span>";
    html += "</label>";
    html +=
      '<input name="calculation_content" style="width: 600px; margin: 0 0 0 15px;" type="text" placeholder="" class="zte-form-control">';
    html +=
      '<i class="zte-iconfont icon-minus fs30" onclick="delecalculationItem(this)"></i>';
    html += "</li>";
    jQ("#calculationUl").append(html);

    // 更新所有选项的顺序
    updateOptionOrder();
  });
}
// 更新选项顺序的函数
function updateOptionOrder() {
  jQ("li[name='calculationNewAdd']").each(function (index) {
    var orderNumber = index + 3; // 从3开始
    jQ(this)
      .find(".zte-checkbox-label")
      .text("第" + numberToChinese(orderNumber) + "空");
  });
}

//移除填空题题选项
function delecalculationItem(ele) {
  jQ(ele).parents("li").remove();
  spaceCounter--;

  //重新给答案设值
  var getAllVal = "";
  jQ("input[name=calculation_val]:checked").each(function (index, ele) {
    var getVal = jQ(ele).val() + ",";
    getAllVal = getAllVal + getVal;
  });
  jQ("#question_answer").val(getAllVal.substr(0, getAllVal.length - 1));
  jQ(".AnserText").text(getAllVal.substr(0, getAllVal.length - 1));
  updateOptionOrder();
}
