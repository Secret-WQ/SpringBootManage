package com.wq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wq.entity.Menu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Q
 * @since 2022-12-21
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
