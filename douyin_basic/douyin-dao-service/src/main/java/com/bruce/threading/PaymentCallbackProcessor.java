//package com.bruce.threading;
//
//
///*
//* 电商支付过程中需要确保每一笔交易都能顺利处理，避免并发导致的数据冲突或者重复支付等问题。常见的多线程应用场景有：
//
//支付回调处理：支付完成后，支付网关会回调电商系统，通知支付结果。为了防止因并发请求导致多次处理相同的支付结果，
//* 可以使用多线程并发处理支付回调，并通过去重机制确保每笔支付只被处理一次。
//支付状态更新：多个线程可以并行更新用户的支付状态（如支付成功、支付失败、退款等）。  用代码举例子
//* */
//
//
//
///*
//*
//* 1. 支付回调处理（去重机制）
//支付回调处理需要确保每笔支付只被处理一次，避免并发请求导致重复支付。常用的方式是通过 去重机制，例如使用支付交易的唯一 ID 来判断该支付是否已处理过。
//
//方案：使用 Redis 存储已处理的支付回调 ID
//* */
//
//
///*
//* 代码解析：
//去重机制：
//
//每个支付回调都有一个唯一的 paymentId，在 Redis 中使用 setnx（Set if Not Exists）命令来保证支付回调只被处理一次。
//如果 paymentId 对应的回调未处理，则返回 1，然后将其标记为已处理。如果已经处理，则返回 0，跳过当前的支付回调处理。
//回调信息会存储在 Redis 中，避免重复处理。
//并发控制：
//
//多个线程模拟并发处理支付回调，通过 Redis 保证每笔支付回调只处理一次。
//* */
//
//import redis.clients.jedis.Jedis;
//
//
//public class PaymentCallbackProcessor {
//
//    private static final String PAYMENT_CALLBACK_KEY = "payment:callback:";
//
//    private Jedis jedis;
//
//    public PaymentCallbackProcessor(Jedis jedis) {
//        this.jedis = jedis;
//    }
//
//    // 处理支付回调
//    public void processPaymentCallback(String paymentId, String paymentResult) {
//        String callbackKey = PAYMENT_CALLBACK_KEY + paymentId;
//
//        // 使用 Redis 的 setnx 命令来确保支付回调处理的去重
//        // 如果 callbackKey 不存在，设置为已处理状态
//        if (jedis.setnx(callbackKey, "processed") == 1) {
//            jedis.expire(callbackKey, 3600);  // 设置过期时间1小时
//            System.out.println("处理支付回调，支付ID: " + paymentId + ", 支付结果: " + paymentResult);
//            // 在这里执行支付回调的具体处理逻辑，如更新订单状态、发送通知等
//        } else {
//            System.out.println("支付回调已处理，支付ID: " + paymentId);
//            // 如果回调已经处理过，跳过或记录日志
//        }
//    }
//
//    public static void main(String[] args) {
//        Jedis jedis = new Jedis("localhost");  // 连接本地 Redis 实例
//        PaymentCallbackProcessor processor = new PaymentCallbackProcessor(jedis);
//
//        // 模拟多个线程并发处理支付回调
//        for (int i = 0; i < 10; i++) {
//            final String paymentId = "payment_" + i;  // 模拟支付 ID
//            final String paymentResult = "SUCCESS";   // 模拟支付结果
//            new Thread(() -> processor.processPaymentCallback(paymentId, paymentResult)).start();
//        }
//    }
//}
