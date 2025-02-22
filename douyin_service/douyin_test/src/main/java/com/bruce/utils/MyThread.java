package com.bruce.utils;

//一般简单的就用这个thread类  但是java只能单继承 如果类已经继承了一个类 不能使用此方法
public class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread is running");
    }
}
