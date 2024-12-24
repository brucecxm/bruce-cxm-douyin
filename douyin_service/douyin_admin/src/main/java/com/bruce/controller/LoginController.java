package com.bruce.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.entity.*;
import com.bruce.service.UserService;
import com.bruce.util.JwtUtil;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin/user")
public class LoginController extends ApiController {

    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    /**
     * 分页查询所有数据
     * @param user 查询实体
     * @return 所有数据
     */
    @PostMapping("/admin/login")
    public Result login(@RequestBody UserVo user, HttpServletResponse response, HttpSession session) {
        Assert.notNull(user.getUsername(), "用户名不能为空");
        Assert.notNull(user.getPassword(), "密码不能为空");
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
            lqw.eq(User::getUsername,user.getUsername() );
        User userBean = userService.getOne(lqw);

        if (userBean == null) {
            return new Result(false, "用户不存在", null, Constants.PASSWORD_CHECK_INVALID);
        }

//        //ERP账号直接提示账号不存在
//        if ("1".equals(userBean.getErpFlag())) {
//            return new Result(false, "账号不存在", null, Constants.PASSWORD_CHECK_INVALID);
//        }

//        String encodePassword = ShiroKit.md5(user.getPassword(), SecurityConsts.LOGIN_SALT);
//        if (!encodePassword.equals(userBean.getPassword())) {
//            return new Result(false, "用户名或密码错误", null, Constants.PASSWORD_CHECK_INVALID);
//        }

//        //账号是否锁定
//        if ("0".equals(userBean.getStatus())) {
//            return new Result(false, "该账号已被锁定", null, Constants.PASSWORD_CHECK_INVALID);
//        }

        String strToken= this.loginSuccess(userBean.getUsername(), response);

//        Subject subject = SecurityUtils.getSubject();
//        AuthenticationToken token= new JwtToken(strToken);
//        subject.login(token);
// 登录成功后，将用户信息保存到 Session
        session.setAttribute("user", userBean);
        //登录成功
        return new Result(true, "登录成功", strToken, Constants.TOKEN_CHECK_SUCCESS);
    }

    /**
     * 登录后更新缓存，生成token，设置响应头部信息
     *
     * @param account
     * @param response
     */
    private String loginSuccess(String account, HttpServletResponse response) {

        String currentTimeMillis = String.valueOf(System.currentTimeMillis());

        //生成token
        JSONObject json = new JSONObject();
        String token = JwtUtil.sign(account, currentTimeMillis);
        json.put("token", token);

//        //更新RefreshToken缓存的时间戳
//        String refreshTokenKey= SecurityConsts.PREFIX_SHIRO_REFRESH_TOKEN + account;
//        jedisUtils.saveString(refreshTokenKey, currentTimeMillis, jwtProperties.getTokenExpireTime()*60);

//        //记录登录日志
//        LoginLog loginLog= new LoginLog();
//        loginLog.setAccount(account);
//        loginLog.setLoginTime(Date.from(Instant.now()));
//        loginLog.setContent("登录成功");
//        loginLog.setYnFlag(YNFlagStatusEnum.VALID.getCode());
//        loginLog.setCreator(account);
//        loginLog.setEditor(account);
//        loginLog.setCreatedTime(loginLog.getLoginTime());
//        loginLogService.save(loginLog);

        //写入header
        response.setHeader(SecurityConsts.REQUEST_AUTH_HEADER, token);
        response.setHeader("Access-Control-Expose-Headers", SecurityConsts.REQUEST_AUTH_HEADER);

        return token;
    }

    /**
     * 获取登录用户基础信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/info",method = {RequestMethod.POST,RequestMethod.GET})
    public Result info(HttpSession session){
        Result result = new Result();
        result.setResult(true);
        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
        JSONObject json = new JSONObject();


        // 从 Session 中获取用户 ID
        User user = (User) session.getAttribute("user");

        json.put("name", user.getUsername());
        json.put("erp", "");

        json.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        json.put("roles",new String[]{"admin"});

//        //查询菜单
//        List<ResourceNode> menus = resourceService.findByUserId(user.getId());
//
//        //查询权限
//        List<Object> authorityList = authorityService.findByUserId(user.getId());

//        json.put("menus",menus);
//        json.put("auth",authorityList);

        result.setData(json);
        return result;
    }

}
