//package com.config;
//
//import cn.dev33.satoken.interceptor.SaInterceptor;
//import cn.dev33.satoken.router.SaHttpMethod;
//import cn.dev33.satoken.router.SaRouter;
//import cn.dev33.satoken.stp.StpUtil;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class SaTokenConfigure implements WebMvcConfigurer {
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//
//        registry.addInterceptor(new SaInterceptor(handler -> {
//            SaRouter.match("/**").check(r -> System.out.println("进入1"));
//            SaRouter.match("/**").check(r -> System.out.println("进入2")).stop();
//            SaRouter.match("/**").check(r -> System.out.println("进入3"));
//            SaRouter.match("/**").check(r -> System.out.println("进入4"));
//            // 执行back函数后将停止匹配，也不会进入Controller，而是直接将 back参数 作为返回值输出到前端
//            SaRouter.match("/user/back").back("要返回到前端的内容");
//
//            SaRouter.match("/**").check(r -> System.out.println("进入5"));
//            //*************************************************************************************
//            //*************************************************************************************
//            //*************************************************************************************
//
//            // 进入 free 独立作用域
////            SaRouter.match("/**").free(r -> {
////                SaRouter.match("/a/**").check(/* --- */);
////                SaRouter.match("/b/**").check(/* --- */).stop();
////                SaRouter.match("/c/**").check(/* --- */);
////            });
////// 执行 stop() 函数跳出 free 后继续执行下面的 match 匹配
////            SaRouter.match("/**").check(/* --- */);
//
//        })).addPathPatterns("/**");
//
//        //*************************************************************************************
//        //*************************************************************************************
//        //*************************************************************************************
//
//
//
//
//        // 注册 Sa-Token 拦截器，定义详细认证规则
//        registry.addInterceptor(new SaInterceptor(handler -> {
//            // 指定一条 match 规则
//            SaRouter
//                    .match("/**")    // 拦截的 path 列表，可以写多个 */
//                    .notMatch("/user/doLogin")        // 排除掉的 path 列表，可以写多个
//                    .check(r -> StpUtil.checkLogin());        // 要执行的校验动作，可以写完整的 lambda 表达式
//
//            // 根据路由划分模块，不同模块不同鉴权
//            SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
//
//
////            在校验函数内不只可以使用 StpUtil.checkPermission("xxx") 进行权限校验，你还可以写任意代码，例如：
//            // 甚至你可以随意的写一个打印语句
//            SaRouter.match("/**", r -> System.out.println("----啦啦啦----"));
//
//
//
////******************************************************************************************************
////******************************************************************************************************
////******************************************************************************************************
//
//            // 基础写法样例：匹配一个path，执行一个校验函数
//            SaRouter.match("/user/**").check(r -> StpUtil.checkLogin());
//
//// 根据 path 路由匹配   ——— 支持写多个path，支持写 restful 风格路由
//// 功能说明: 使用 /user , /goods 或者 /art/get 开头的任意路由都将进入 check 方法
////            SaRouter.match("/user/**", "/goods/**", "/art/get/{id}").check( /* 要执行的校验函数 */ );
////
////// 根据 path 路由排除匹配
////// 功能说明: 使用 .html , .css 或者 .js 结尾的任意路由都将跳过, 不会进入 check 方法
////            SaRouter.match("/**").notMatch("*.html", "*.css", "*.js").check( /* 要执行的校验函数 */ );
////
////// 根据请求类型匹配
////            SaRouter.match(SaHttpMethod.GET).check( /* 要执行的校验函数 */ );
////
////// 根据一个 boolean 条件进行匹配
////            SaRouter.match( StpUtil.isLogin() ).check( /* 要执行的校验函数 */ );
////
////// 根据一个返回 boolean 结果的lambda表达式匹配
////            SaRouter.match( r -> StpUtil.isLogin() ).check( /* 要执行的校验函数 */ );
////
////// 多个条件一起使用
////// 功能说明: 必须是 Get 请求 并且 请求路径以 `/user/` 开头
////            SaRouter.match(SaHttpMethod.GET).match("/user/**").check( /* 要执行的校验函数 */ );
////
////// 可以无限连缀下去
//// 功能说明: 同时满足 Get 方式请求, 且路由以 /admin 开头, 路由中间带有 /send/ 字符串, 路由结尾不能是 .js 和 .css
////            SaRouter
////                    .match(SaHttpMethod.GET)
////                    .match("/admin/**")
////                    .match("/**/send/**")
////                    .notMatch("/**/*.js")
////                    .notMatch("/**/*.css")
////                    // ....
////                    .check( /* 只有上述所有条件都匹配成功，才会执行最后的check校验函数 */ );
//
//
//
////******************************************************************************************************
////******************************************************************************************************
////******************************************************************************************************
//
//
//
//
//        })).addPathPatterns("/**");
//    }
//}
