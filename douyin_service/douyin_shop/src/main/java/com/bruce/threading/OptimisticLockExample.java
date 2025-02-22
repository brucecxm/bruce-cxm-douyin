package com.bruce.threading;

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*
 * 在电商业务中，尤其是秒杀或抢购等高并发场景下，确保库存的准确性至关重要。我们可以使用 乐观锁 和 分布式锁 来控制库存的并发更新。
 * */
/*
 * 在电商业务中，库存是一个需要频繁更新的资源。尤其是在秒杀、抢购等场景下，
 * 商品库存会因为大量用户并发操作而发生变化。为了确保库存更新的准确性和防止超卖，多线程和并发控制就显得尤为重要。
 * */

/*
* 乐观锁实现思路：
在数据库中添加一个 version 字段，每次更新库存时，检查该字段是否与当前库存版本一致。
如果版本号一致，更新库存并增加版本号；如果版本号不一致，说明其他线程已更新库存，需要重试或失败。
* */


public class OptimisticLockExample {

    // 数据库连接
    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/ecommerce";
        String username = "root";
        String password = "password";
        return DriverManager.getConnection(url, username, password);
    }

    // 模拟商品库存更新（乐观锁）
    public void updateStockOptimistic(int productId, int quantity) {
        String sql = "SELECT stock, version FROM product WHERE product_id = ?";
        String updateSql = "UPDATE product SET stock = ?, version = version + 1 WHERE product_id = ? AND version = ?";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement selectStmt = conn.prepareStatement(sql);
            selectStmt.setInt(1, productId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int stock = rs.getInt("stock");
                int version = rs.getInt("version");

                // 如果库存大于请求扣除数量，则进行库存更新
                if (stock >= quantity) {
                    // 尝试更新库存
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                    updateStmt.setInt(1, stock - quantity);
                    updateStmt.setInt(2, productId);
                    updateStmt.setInt(3, version);

                    int affectedRows = updateStmt.executeUpdate();

                    // 如果更新失败，说明其他线程已经修改了库存，返回错误或重试
                    if (affectedRows > 0) {
                        System.out.println("库存更新成功，扣除数量：" + quantity);
                    } else {
                        System.out.println("库存更新失败，版本冲突，可能是并发修改导致的");
                    }
                } else {
                    System.out.println("库存不足，无法更新");
                }
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        OptimisticLockExample example = new OptimisticLockExample();

        // 模拟多个线程并发扣减库存
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            final int quantity = 1;  // 每次扣减 1 个库存
            executor.submit(() -> example.updateStockOptimistic(1, quantity));
        }

        executor.shutdown();
    }
}
