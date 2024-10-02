package com.bruce.test;


import com.bruce.dao.BrandMapper;
import com.bruce.dao.VideoDao;
import com.bruce.service.VideoOneService;
import com.bruce.service.VideoService;
import com.bruce.video.entity.Brand;
import com.bruce.video.entity.VideoOne;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.List;

@SpringBootTest
public class test {

@Autowired
private VideoOneService videoOneService;




@Test
public void testone() throws Exception {

    videoOneService.insertlianjie("ss");


}




@Test
    public  void test() {
        File file = new File("C:\\Users\\Administrator\\Desktop\\lianjie.txt");

        FileInputStream in;

        {
            try {

                StringBuilder line = new StringBuilder(); // 使用 StringBuilder

                FileReader fr = new FileReader(file);
                int b ;
                // 循环读取
                while ((b = fr.read())!=-1) {
                    char temp=(char)b;
                    System.out.println(temp);
                    line.append(temp);
                }

                System.out.println(line);
                System.out.println("*************************");


                String lineString=line.toString();

                String[] split = lineString.split("，");

                for(int i=0; i<split.length; i++)
                {
                    System.out.println("=====");
                    System.out.println(split[i]);
                    videoOneService.insertlianjie(split[i]);

                }



                // 关闭资源
                fr.close();


            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }




}
