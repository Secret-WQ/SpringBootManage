package com.wq.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wq.common.Result;

import com.wq.service.IDictService;
import com.wq.entity.Dict;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/dict")
    public class DictController {

@Resource
private IDictService dictService;

// 新增或者更新
@PostMapping("/save")
public Result save(@RequestBody Dict dict) {
    dictService.saveOrUpdate(dict);
        return Result.success();
        }

@DeleteMapping("/delete/{id}")
public Result delete(@PathVariable Integer id) {
    dictService.removeById(id);
        return Result.success();
        }

@DeleteMapping("/deletelist")
public Result deleteBatch(@RequestBody List<Integer> ids) {
    dictService.removeByIds(ids);
        return Result.success();
        }

@GetMapping("/list")
public Result findAll() {
        return Result.success(dictService.list());
        }

@GetMapping("/list/{id}")
public Result findOne(@PathVariable Integer id) {
        return Result.success(dictService.getById(id));
        }

@GetMapping("/page")
public Result findPage(@RequestParam Integer pageNum,
@RequestParam Integer pageSize,
@RequestParam String name) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("", name);
        return Result.success(dictService.page(new Page<>(pageNum, pageSize), queryWrapper));
        }

        }

