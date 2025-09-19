package com.bruce.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bruce.common.R;
import com.bruce.entity.ShoppingCart;
import com.bruce.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 购物车
 */
@Slf4j
@RestController
@RequestMapping("/shop/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车数据:{}",shoppingCart);
        String loginId = StpUtil.getLoginId().toString();  // 转换为字符串
        Long userId = Long.parseLong(loginId);  // 将字符串转为 Long 类型
        System.out.println("当前用户的ID: " + userId);
//        //设置用户id，指定当前是哪个用户的购物车数据
//        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);

        if(dishId != null){
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);

        }else{
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        //查询当前菜品或者套餐是否在购物车中
        //SQL:select * from shopping_cart where user_id = ? and dish_id/setmeal_id = ?
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        if(cartServiceOne != null){
            //如果已经存在，就在原来数量基础上加一
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        }else{
            //如果不存在，则添加到购物车，数量默认就是一
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }

        return R.success(cartServiceOne);
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车...");
        String loginId = StpUtil.getLoginId().toString();  // 转换为字符串
        Long userId = Long.parseLong(loginId);  // 将字符串转为 Long 类型
        System.out.println("当前用户的ID: " + userId);
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * 更新购物车商品数量
     * @param shoppingCart 购物车信息
     * @return 更新后的购物车商品
     */
    @PostMapping("/updateNumber")
    public R<ShoppingCart> updateNumber(@RequestBody ShoppingCart shoppingCart){
        log.info("更新购物车商品数量: {}", shoppingCart);
        
        String loginId = StpUtil.getLoginId().toString();  // 转换为字符串
        Long userId = Long.parseLong(loginId);  // 将字符串转为 Long 类型
        
        // 验证购物车项是否属于当前用户
        ShoppingCart existingItem = shoppingCartService.getById(shoppingCart.getId());
        if (existingItem == null || !existingItem.getUserId().equals(userId)) {
            return R.error("无权操作该购物车项");
        }
        
        // 更新数量
        existingItem.setNumber(shoppingCart.getNumber());
        shoppingCartService.updateById(existingItem);
        
        return R.success(existingItem);
    }
    
    /**
     * 删除单个购物车商品
     * @param id 购物车项ID
     * @return 操作结果
     */
    @DeleteMapping("/delete/{id}")
    public R<String> delete(@PathVariable Long id){
        log.info("删除购物车商品, id: {}", id);
        
        String loginId = StpUtil.getLoginId().toString();  // 转换为字符串
        Long userId = Long.parseLong(loginId);  // 将字符串转为 Long 类型
        
        // 验证购物车项是否属于当前用户
        ShoppingCart existingItem = shoppingCartService.getById(id);
        if (existingItem == null || !existingItem.getUserId().equals(userId)) {
            return R.error("无权操作该购物车项");
        }
        
        shoppingCartService.removeById(id);
        
        return R.success("删除成功");
    }
    
    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        //SQL:delete from shopping_cart where user_id = ?
        String loginId = StpUtil.getLoginId().toString();  // 转换为字符串
        Long userId = Long.parseLong(loginId);  // 将字符串转为 Long 类型
        System.out.println("当前用户的ID: " + userId);
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);

        shoppingCartService.remove(queryWrapper);

        return R.success("清空购物车成功");
    }
}