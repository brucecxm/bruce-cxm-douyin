package com.he;

import cn.dev33.satoken.annotation.*;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
public class controller {
    @GetMapping("/test")
   public void test(){
        StpUtil.login(10);
// 校验：当前账号是否含有指定角色标识, 如果验证未通过，则抛出异常: NotRoleException
//        StpUtil.checkRole("super-admissssn");

        // 在登录时缓存 user 对象
        StpUtil.getSession().set("user", "session对象测试");
// 然后我们就可以在任意处使用这个 user 对象

        System.out.println(// 然后我们就可以在任意处使用这个 user 对象
                StpUtil.getSession().get("user"));


        // 获取当前账号 id 的 Account-Session (必须是登录后才能调用)
        StpUtil.getSession();

// 获取当前账号 id 的 Account-Session, 并决定在 Session 尚未创建时，是否新建并返回
        StpUtil.getSession(true);

// 获取账号 id 为 10001 的 Account-Session
        StpUtil.getSessionByLoginId(10001);

// 获取账号 id 为 10001 的 Account-Session, 并决定在 Session 尚未创建时，是否新建并返回
        StpUtil.getSessionByLoginId(10001, true);

// 获取 SessionId 为 xxxx-xxxx 的 Account-Session, 在 Session 尚未创建时, 返回 null
        StpUtil.getSessionBySessionId("xxxx-xxxx");



    }

    @GetMapping("/testone")
    public void testone(){

        StpUtil.logout(10);                    // 强制指定账号注销下线
    }


    // 登录校验：只有登录之后才能进入该方法
    @SaCheckLogin
    @RequestMapping("info")
    public String info() {
        return "查询用户信息";
    }

    // 角色校验：必须具有指定角色才能进入该方法
    @SaCheckRole("super-admin")
    @RequestMapping("add1")
    public String add1() {
        return "用户增加";
    }

    // 权限校验：必须具有指定权限才能进入该方法
    @SaCheckPermission("user-add")
    @RequestMapping("add2")
    public String add2() {
        return "用户增加";
    }

    // 二级认证校验：必须二级认证之后才能进入该方法
    @SaCheckSafe()
    @RequestMapping("add3")
    public String add3() {
        return "用户增加";
    }

    // Http Basic 校验：只有通过 Http Basic 认证后才能进入该方法
    @SaCheckHttpBasic(account = "sa:123456")
    @RequestMapping("add4")
    public String add4() {
        return "用户增加";
    }

    // Http Digest 校验：只有通过 Http Digest 认证后才能进入该方法
    @SaCheckHttpDigest(value = "sa:123456")
    @RequestMapping("add5")
    public String add5() {
        return "用户增加";
    }

    // 校验当前账号是否被封禁 comment 服务，如果已被封禁会抛出异常，无法进入方法
    @SaCheckDisable("comment")
    @RequestMapping("send")
    public String send() {
        return "查询用户信息";
    }

//    请求将会跳过拦截器的校验，直接进入 Controller 的方法中。
//    请求将会跳过拦截器的校验，直接进入 Controller 的方法中。
//    请求将会跳过拦截器的校验，直接进入 Controller 的方法中。
    @SaIgnore
    @RequestMapping("/user/getList")
    public SaResult getList() {
        System.out.println("------------ 访问进来方法");
        return SaResult.ok();
    }


}

