package com.wq.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wq.common.Result;
import com.wq.entity.Role;
import com.wq.service.impl.RoleServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleServiceImpl roleService;


    // 新增或者更新
    @PostMapping("/save")
    public Result save(@RequestBody Role role) {
        roleService.saveOrUpdate(role);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        roleService.removeById(id);
        return Result.success();
    }

    @DeleteMapping("/deletelist")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        boolean b = roleService.removeByIds(ids);
        return Result.success(b);
    }

    @GetMapping("/list")
    public Result findAll() {
        return Result.success(roleService.list());
    }

    @GetMapping("/list/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(roleService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name
    ) {
        IPage<Role> page = roleService.findPage(pageNum, pageSize, name);
        return Result.success(page);
    }

}

