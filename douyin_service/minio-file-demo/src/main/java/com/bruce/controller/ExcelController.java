package com.bruce.controller;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.bruce.entity.UserData;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), UserData.class, new AnalysisEventListener<UserData>() {
            @Override
            public void invoke(UserData userData, AnalysisContext analysisContext) {
                System.out.println("读取到数据: " + userData);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                // 读取完成后的处理
            }
        }).sheet().doRead();
        return "文件上传成功";
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        String fileName = "user_data.xlsx";
        List<UserData> data = new ArrayList<>();
        data.add(new UserData("张三", 20));
        data.add(new UserData("李四", 25));

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        EasyExcel.write(response.getOutputStream(), UserData.class).sheet("用户数据").doWrite(data);
    }
}
