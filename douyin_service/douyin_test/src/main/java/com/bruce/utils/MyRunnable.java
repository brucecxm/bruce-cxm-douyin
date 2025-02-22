package com.bruce.utils;
//一般简单的就用这个thread类  但是java只能单继承 如果类已经继承了一个类 不能使用此方法
//解决了java只能单继承的缺点  但是没有返回值 无法获取返回值
public class MyRunnable implements Runnable {
    @Override
    public void run() {
        // 需要执行的任务
        System.out.println("Runnable thread is running");
    }

}
