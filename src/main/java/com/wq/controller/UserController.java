package com.wq.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wq.common.Constants;
import com.wq.common.Result;
import com.wq.entity.DTO.UserDTO;
import com.wq.entity.User;
import com.wq.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/login")
    public Result Login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE.CODE_400, "参数错误");
        }
        UserDTO login = userService.Login(userDTO);
        return Result.success(login, "登录成功");
    }


    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        String name = user.getUsername();
        String pas = user.getPassword();
        if (StrUtil.isBlank(name) || StrUtil.isBlank(pas)) {
            return Result.error(Constants.CODE.CODE_400, "参数错误");
        }
        User register = userService.register(user);
        return Result.success(register, "注册成功");
    }


    //查询全部信息
    @GetMapping("/list")
    public Result findAll() {
        //userService.findAll().stream().forEach(System.err::println);
        List<User> userList = userService.findAll();
        return Result.success(userList);
    }

    //根据id查询信息
    @GetMapping("/list/{id}")
    public Result findById(@PathVariable("id") Integer id) {
        //userService.findAll().stream().forEach(System.err::println);
        User byId = userService.findById(id);
        return Result.success(byId);
    }


    //根据username查询信息
    @GetMapping("/username/{username}")
    public Result findByName(@PathVariable("username") String username) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        User one = userService.getOne(queryWrapper);
        return Result.success(one);
    }


    //分页查询
    //接口路径 user/page
    //@RequestParam  会接受参数映射到url中
    //sql语句中 limit 第一个参数（pageNum-1）* pageSize
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String username) {
        System.err.println(username);
        IPage<User> page = userService.findPage(pageNum, pageSize, username);
        return Result.success(page);
    }

    //添加，修改
    //@RequestBody主要用来接收前端传递给后端的json字符串中的数据
    @PostMapping("/save")
    public Result saveUser(@RequestBody User user) {
        boolean b = userService.saveUser(user);
        if (b) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    //删除
    @DeleteMapping("/delete/{id}")
    public Result deleteUser(@PathVariable int id) {
        boolean b = userService.deleteUser(id);
        if (b) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    //批量化删除
    @DeleteMapping("/deletelist")
    public Result deleteUserList(@RequestBody List<Integer> ids) {
        boolean b = userService.removeByIds(ids);
        if (b) {
            return Result.success();
        } else {
            return Result.error();
        }
    }


    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        userService.export(response);
    }

    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        boolean b = userService.imp(file);
        if (b) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

}
