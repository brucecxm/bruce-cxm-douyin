package com.testall;

import java.io.*;

public class d {


    public static void main(String[] args) {
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
