package com.wq.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wq.common.Result;
import com.wq.entity.Files;
import com.wq.service.impl.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/*
 *
 * 文件上传
 * */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileServiceImpl fileService;


    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String upload = fileService.upload(file);
        return upload;
    }

    @GetMapping("/downlod/{fileUuid}")
    public void downlod(@PathVariable String fileUuid, HttpServletResponse response) throws Exception {
        fileService.downlod(fileUuid, response);
    }


    @PostMapping("/update")
    public Result update(@RequestBody Files files) {
        Boolean aBoolean = fileService.updateById(files);
        return Result.success(aBoolean);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        boolean delete = fileService.delete(id);
        return Result.success(delete);
    }

    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        boolean b = fileService.deleteBatch(ids);
        return Result.success(b);
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String fname) {

        IPage<Files> page = fileService.findPage(pageNum, pageSize, fname);
        return Result.success(page);
    }


}
