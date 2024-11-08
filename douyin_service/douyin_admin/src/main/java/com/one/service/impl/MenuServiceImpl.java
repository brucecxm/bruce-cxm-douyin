package com.one.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.one.dao.MenuDao;
import com.one.entity.Menu;
import com.one.service.MenuService;
import org.springframework.stereotype.Service;

/**
 * (Menu)表服务实现类
 *
 * @author makejava
 * @since 2024-11-05 23:56:53
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuDao, Menu> implements MenuService {

}

