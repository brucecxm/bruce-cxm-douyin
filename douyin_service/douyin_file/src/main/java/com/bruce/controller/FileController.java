//package com.bruce.controller;
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.io.FileUtil;
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.excel.EasyExcel;
//import com.alibaba.excel.ExcelWriter;
//import com.alibaba.excel.context.AnalysisContext;
//import com.alibaba.excel.event.AnalysisEventListener;
//import com.alibaba.excel.write.metadata.WriteSheet;
//import com.bruce.filepojo.UserData;
//import com.bruce.service.ExcelService;
//import com.bruce.service.MarkdownService;
//import com.bruce.service.MinioService;
//import com.bruce.util.JwtUtils;
//import com.google.gson.Gson;
//import io.minio.errors.MinioException;
//import org.lionsoul.ip2region.DataBlock;
//import org.lionsoul.ip2region.DbConfig;
//import org.lionsoul.ip2region.DbSearcher;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import oshi.SystemInfo;
//import oshi.hardware.CentralProcessor;
//import oshi.hardware.HardwareAbstractionLayer;
//import oshi.software.os.OperatingSystem;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//
//@RestController
//@RequestMapping("/files")
//public class FileController {
//
//@Autowired
//private FileStorageService fileStorageService;
//
//
//    @GetMapping("/test")
//    public void test() throws FileNotFoundException {
//        FileInputStream fileInputStream=new FileInputStream("D:\\img\\one.jpg");
//fileStorageService.uploadImgFile("","one.jpg",fileInputStream);
//System.out.println("coming");
//    }
//
//
//
//    @GetMapping("/excel")
//    public void excel() throws FileNotFoundException {
//        // 准备数据
//        List<UserData> dataList = new ArrayList<>();
//        dataList.add(new UserData("张三", 20));
//        dataList.add(new UserData("李四", 25));
//        dataList.add(new UserData("王五", 30));
//
//        // 写入 Excel 文件
//        String fileName = "example.xlsx"; // Excel 文件路径
//        EasyExcel.write(fileName, UserData.class).sheet("Sheet1").doWrite(dataList);
//
//
//
//
//
//
//
//
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    // 模拟数据
//    private List<UserData> dataList = new ArrayList<>();
//
//    // 接收前端上传的 Excel 文件
//    @PostMapping("/upload")
//    public String uploadExcel(@RequestParam("file") MultipartFile file) throws IOException {
//        // 使用 EasyExcel 读取上传的 Excel 文件数据
//        EasyExcel.read(file.getInputStream(), UserData.class, new ExcelDataListener()).sheet().doRead();
//        return "Upload successful!";
//    }
//
//    // 处理下载请求，将 Excel 文件返回给前端进行下载
//    @GetMapping("/download")
//    public void downloadExcel(HttpServletResponse response) throws IOException {
//        // 准备数据
//        dataList.add(new UserData("张三", 255550));
//        dataList.add(new UserData("李四", 25));
//        dataList.add(new UserData("王五", 30));
//
//        // 设置响应头
//        response.setContentType("application/vnd.ms-excel");
//        response.setCharacterEncoding("utf-8");
//        response.setHeader("Content-disposition", "attachment;filename=example.xlsx");
//
//        // 使用 EasyExcel 写入 Excel 文件并返回给前端
//        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), UserData.class).build();
//        WriteSheet writeSheet = EasyExcel.writerSheet("Sheet1").build();
//        excelWriter.write(dataList, writeSheet);
//        excelWriter.finish();
//    }
//
//    // 数据监听器类，用于处理 Excel 文件中的数据
//    public static class ExcelDataListener extends AnalysisEventListener<UserData> {
//
//        @Override
//        public void invoke(UserData data, AnalysisContext context) {
//            // 这里可以处理从 Excel 中读取到的每一行数据，可以存入数据库或进行其他业务逻辑处理
//            System.out.println("读取到数据：" + data);
//        }
//
//        @Override
//        public void doAfterAllAnalysed(AnalysisContext context) {
//            // 数据读取完成后执行的操作，如关闭资源等
//        }
//    }
//
//
//
//
//    @Autowired
//    private ExcelService excelService;
//
//    @GetMapping("/createExcel")
//    public String createExcel() {
//        try {
//            excelService.createExcel();
//            return "Excel created successfully!";
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "Failed to create Excel: " + e.getMessage();
//        }
//    }
//
//
//
//    @GetMapping("/hutooltest")
//    public void hutooltest() {
//        // 使用字符串工具类
//        String str = "Hello, Hutool!";
//        String result = StrUtil.upperFirst(str);
//        System.out.println(result); // 输出：Hello, Hutool!
//
//        // 使用日期工具类
//        String dateStr = "2024-07-16 12:00:00";
//        Date date = DateUtil.parse(dateStr);
//        System.out.println(DateUtil.formatDateTime(date)); // 输出：2024-07-16 12:00:00
//
//
//        // 使用字符串工具类
//        String str1 = "hello, hutool!";
//
//        // 首字母大写
//        String capitalized = StrUtil.upperFirst(str1);
//        System.out.println("首字母大写：" + capitalized); // 输出：Hello, hutool!
//
//        // 判断是否为空白字符（包括null）
//        boolean isEmptyOrBlank = StrUtil.isBlank(str1);
//        System.out.println("是否为空白字符：" + isEmptyOrBlank); // 输出：false
//
//        // 字符串格式化
//        String template = "This is {} example {}.";
//        String result2 = StrUtil.format(template, "Hutool", "using");
//        System.out.println("格式化后的字符串：" + result2); // 输出：This is Hutool example using.
//
//
//        // 使用日期工具类
//        String dateStr1 = "2024-07-16 12:00:00";
//
//        // 字符串转换为日期对象
//        Date date1 = DateUtil.parse(dateStr1);
//        System.out.println("日期对象：" + date1); // 输出：Mon Jul 16 12:00:00 CST 2024
//
//        // 格式化日期对象为字符串
//        String formattedDate = DateUtil.formatDateTime(date1);
//        System.out.println("格式化后的日期字符串：" + formattedDate); // 输出：2024-07-16 12:00:00
//
//        // 使用文件工具类
//        String filePath = "path/to/your/file.txt";
//
//        // 判断文件是否存在
//        boolean exists = FileUtil.exist(filePath);
//        System.out.println("文件是否存在：" + exists); // 输出：true 或者 false
//
//        // 创建文件
//        File file = FileUtil.touch(filePath);
//        System.out.println("文件创建成功：" + file.getAbsolutePath());
//
//        // 复制文件
//        String targetFilePath = "path/to/your/target/file.txt";
//        File targetFile = FileUtil.copy(file, new File(targetFilePath), true);
//        System.out.println("文件复制成功：" + targetFile.getAbsolutePath());
//
//
//
//    }
//
//
//
//    @GetMapping("/systeminfo")
//    public void systeminfo() {
//
//        SystemInfo systemInfo = new SystemInfo();
//        // 获取操作系统信息
//        OperatingSystem os = systemInfo.getOperatingSystem();
//        // 获取硬件抽象层信息
//        HardwareAbstractionLayer hardware = systemInfo.getHardware();
//        // 获取中央处理器信息
//        CentralProcessor processor = hardware.getProcessor();
//
//        // 打印操作系统信息
//        System.out.println("操作系统: " + os);
//        // 打印处理器信息
//        System.out.println("处理器: " + processor);
//    }
//
//
//
//
//    @GetMapping("/ipinfo")
//    public void ipinfo(String ip) {
//
//        try {
//            // 注意替换为你自己的 IP 数据库文件路径
//            String dbPath = "/path/to/ip2region.db";
//            File file = new File(dbPath);
//            if (!file.exists()) {
//                System.err.println("Error: Invalid ip2region.db file path");
//            }
//            // 加载 IP 数据库文件
//            DbConfig config = new DbConfig();
//            DbSearcher searcher = new DbSearcher(config, dbPath);
//
//            // 查询 IP 地址的信息
//            DataBlock dataBlock = searcher.memorySearch(ip);
//System.out.println(        dataBlock.getRegion());
//            // 获取查询结果
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//
//    @Autowired
//    private MarkdownService markdownService;
//
//    @GetMapping("/markdown")
//    public String renderMarkdown(@RequestParam("text") String text) {
//        return markdownService.renderMarkdownToHtml(text);
//    }
//
//
//
//
//    @Autowired
//    private JwtUtils jwtUtils;
//
//    // 模拟用户登录，生成 JWT Token
//    @PostMapping("/login")
//    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
//        // 在实际应用中，这里应该进行用户名密码校验，验证通过后生成 JWT Token
//        String token = jwtUtils.generateJwtToken(username);
//        return token;
//    }
//
//    // 根据 JWT Token 获取用户名
//    @GetMapping("/username")
//    public String getUsername(@RequestHeader("Authorization") String authToken) {
//        String token = authToken.substring(7); // 去掉 "Bearer " 前缀
//        String username = jwtUtils.getUsernameFromJwtToken(token);
//        return username;
//    }
//
//    // 验证 JWT Token 是否有效
//    @GetMapping("/validate")
//    public boolean validateToken(@RequestHeader("Authorization") String authToken) {
//        String token = authToken.substring(7); // 去掉 "Bearer " 前缀
//        return jwtUtils.validateJwtToken(token);
//    }
//
//
//
//    @PostMapping("/test")
//    public void Gson (@RequestBody UserData userData) {
//        // 在需要使用的类中
//        Gson gson = new Gson();
//// 将Java对象转换为JSON字符串
//        String json = gson.toJson(userData);
//        System.out.println(json);
//// 将JSON字符串转换为Java对象
//        UserData one    = gson.fromJson(json, UserData.class);
//        System.out.println(one.toString());
//
//    }
//
//
//
//
//    @Autowired
//    private MinioService minioService;
//
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//
//
//
//        try {
//            String fileName = minioService.uploadFile(file);
//            return ResponseEntity.ok("File uploaded successfully: " + fileName);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
//        }
//    }
//
//
//}
