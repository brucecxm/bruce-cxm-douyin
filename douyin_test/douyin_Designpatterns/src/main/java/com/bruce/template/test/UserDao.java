//package com.bruce.template.test;
//import org.apache.tomcat.jni.User;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//
//import javax.sql.DataSource;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;
//
//public class UserDao {
//    private JdbcTemplate jdbcTemplate;
//
//    public UserDao(DataSource dataSource) {
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }
//
//    // 查询所有用户
//    public List<User> getAllUsers() {
//        String sql = "SELECT * FROM users";
//        return jdbcTemplate.query(sql, new UserRowMapper());
//    }
//
//    // 查询单个用户
//    public User getUserById(int id) {
//        String sql = "SELECT * FROM users WHERE id = ?";
//        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new UserRowMapper());
//    }
//
//    // 行映射器
//    private static class UserRowMapper implements RowMapper<User> {
//        @Override
//        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//            User user = new User();
//            user.setId(rs.getInt("id"));
//            user.setName(rs.getString("name"));
//            user.setEmail(rs.getString("email"));
//            return user;
//        }
//    }
//}
