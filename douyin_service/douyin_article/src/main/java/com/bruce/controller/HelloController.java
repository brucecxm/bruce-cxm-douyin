//package com.bruce.article.controller;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.bruce.article.entile.Student;
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//import freemarker.template.TemplateException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.io.*;
//import java.util.HashMap;
//import java.util.Map;
//
//@Controller
//public class HelloController {
//
//
//    @Autowired
//    private FileStorageService fileStorageService;
//
//    @GetMapping("/basic")
//    public String test(Model model) {
//        //1.纯文本形式的参数
//        model.addAttribute("name", "freemarker");
//        //2.实体类相关的参数
//        Student student = new Student();
//        student.setName("小明");
//        student.setAge(18);
//        model.addAttribute("stu", student);
//        return "01-basic";
//    }
//
//    @Autowired
//    private Configuration configuration;
//    @GetMapping("/hanshu")
//    public void hanshu() throws IOException, TemplateException {
//        //freemarker的模板对象，获取模板
//        Template template = configuration.getTemplate("01-basic.ftl");
//        Student student = new Student();
//        student.setName("小明");
//        student.setAge(18);
//        String params = ("stu");
//        //合成
//        //第一个参数 数据模型
//        //第二个参数  输出流
//        template.process(params, new FileWriter("d:/basic.html"));
//    }
//
//
//
//    @GetMapping("/ceshi")
//    public void testminio()  {
//
//        String params = "测试minio";
//
//     buildArticleToMinIO(params);
//    }
//
//
//
//
//
//    /**
//     * 生成静态文件上传到minIO中
//     * @param content
//     */
//    @Async
//    public void buildArticleToMinIO(String content) {
//        //已知文章的id
//        //4.1 获取文章内容
//        if(StringUtils.isNotBlank(content)){
//            //4.2 文章内容通过freemarker生成html文件
//            Template template = null;
//            StringWriter out = new StringWriter();
//            try {
//                template = configuration.getTemplate("article.ftl");
//                //数据模型
//                Map<String,Object> contentDataModel = new HashMap<>();
//                contentDataModel.put("content", JSONArray.parseArray(content));
//                //合成
//                template.process(contentDataModel,out);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//            //4.3 把html文件上传到minio中
//            InputStream in = new ByteArrayInputStream(out.toString().getBytes());
//            String path = fileStorageService.uploadHtmlFile("", "ceshi" + ".html", in);
//
//
//        }
//    }
//
//    }
//
