//package com.bruce;
//import com.alibaba.excel.EasyExcel;
//import com.bruce.filepojo.UserData;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ExcelWriteDemo {
//
//    public static void main(String[] args) {
//        // 准备数据
//        List<UserData> dataList = new ArrayList<>();
//        dataList.add(new UserData("张三", 20));
//        dataList.add(new UserData("李四", 25));
//        dataList.add(new UserData("王五", 30));
//
//        // 写入 Excel 文件
//        String fileName = "example.xlsx"; // Excel 文件路径
//        EasyExcel.write(fileName, UserData.class).sheet("Sheet1").doWrite(dataList);
//    }
//}
