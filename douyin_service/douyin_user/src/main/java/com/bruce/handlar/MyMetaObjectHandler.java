//package com.bruce.handlar;
//
//import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
//import org.apache.ibatis.reflection.MetaObject;
//import org.springframework.stereotype.Component;
//import java.time.LocalDateTime;
//
//@Component
//public class MyMetaObjectHandler implements MetaObjectHandler {
//
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        // 自动填充创建时间和更新时间
//        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
//        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        // 自动填充更新时间
//        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
//    }
//}
