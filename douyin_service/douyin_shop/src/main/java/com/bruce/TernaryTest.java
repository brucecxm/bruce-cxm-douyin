package com.bruce;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TernaryTest {
    // 生成指定长度的随机字母串
    public static String randomString(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Random random = new Random();
        List<String> list = new ArrayList<>();

        for(int i = 0; i < 100; i++) {
            int randomLength = 3 + random.nextInt(8); // 3~10
            list.add(randomString(randomLength));
        }



        list.stream().filter(new Predicate<String>(){

            @Override
            public boolean test(String s) {
                return s.startsWith("a")&&s.length()>3;
            }
        }).forEach(
                new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        System.out.println(s);
                    }
                }
        );


        System.out.println("_----------------");

        list.forEach(s-> System.out.println(s));
        System.out.println("_----------------");


        System.out.println(

                list.stream().filter(new Predicate<String>(){

                    @Override
                    public boolean test(String s) {
                        return s.startsWith("a")&&s.length()>3;
                    }
                }).count()
        );

        System.out.println("_----------------");

List one=        list.stream().filter(new Predicate<String>(){

            @Override
            public boolean test(String s) {
                return s.startsWith("a")&&s.length()>3;
            }
        }).collect(Collectors.toList());

one.forEach(s-> System.out.println(s));


    }
}
