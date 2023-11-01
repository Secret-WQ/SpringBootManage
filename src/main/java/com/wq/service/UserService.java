package com.wq.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wq.entity.User;

import java.util.List;

/**
 * @author Q.
 * @create 2022/11/4 0004 - 16:52
 */

public interface UserService  {
    List<User> findAll();

    //Map<String,Object> findPage(Integer pageNum, Integer pageSize,String username);

    IPage<User> findPage(Integer pageNum, Integer pageSize,String username);

    User findById(Integer id);

    boolean saveUser(User user);

    Boolean deleteUser(int id);




}
