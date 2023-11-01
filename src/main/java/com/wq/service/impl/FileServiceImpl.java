package com.wq.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.deploy.net.URLEncoder;
import com.wq.entity.Files;
import com.wq.mapper.FileMapper;
import com.wq.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, Files> implements FileService {


    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    FileMapper fileMapper;

    //分页查询
    public IPage<Files> findPage(Integer pageNum,
                                 Integer pageSize,
                                 String fname) {
        IPage<Files> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        // 查询未删除的记录
        queryWrapper.eq("is_delete", false);
        queryWrapper.orderByDesc("fid");
        if (!"".equals(fname)) {
            queryWrapper.like("fname", fname);
        }
        return page(page, queryWrapper);
    }

    //上传
    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        // 定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;

        File uploadFile = new File(fileUploadPath + fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String url;
        // 获取文件的md5
        String md5 = SecureUtil.md5(file.getInputStream());
        // 从数据库查询是否存在相同的记录
        Files dbFiles = getFileByMd5(md5);
        System.out.println(dbFiles);
        if (dbFiles != null) { // 文件已存在
            url = dbFiles.getUrl();
            System.out.println(url);
            // 存储数据库
            return url;

        } else {
            // 上传文件到磁盘
            file.transferTo(uploadFile);
            // 数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://localhost:9090/file/downlod/" + fileUUID;

            // 存储数据库
            Files saveFile = new Files();
            saveFile.setFname(originalFilename);
            saveFile.setFtype(type);
            saveFile.setSize(size / 1024);
            saveFile.setUrl(url);
            saveFile.setMd5(md5);
            fileMapper.insert(saveFile);
            return url;
        }
    }

    //下载
    public void downlod(String fileUuid, HttpServletResponse response) throws Exception {
        //根据文件的唯一标识获取文件
        File file = new File(fileUploadPath + fileUuid);
        ServletOutputStream outputStream = response.getOutputStream();

        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUuid, "UTF-8"));
        response.setContentType("application/octet-stream");

        //
        outputStream.write(FileUtil.readBytes(file));
        outputStream.flush();
        outputStream.close();
    }

    //根绝md5找到对应文件
    public Files getFileByMd5(String md5) {
        QueryWrapper<Files> wrapper = new QueryWrapper();
        wrapper.eq("md5", md5);
        List<Files> filesList = fileMapper.selectList(wrapper);
       //filesList.stream().forEach(System.err::println);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    //修改
    public boolean updateById(Files files) {
        int i = fileMapper.updateById(files);
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    //删除
    public boolean delete(Integer id) {
        Files files = fileMapper.selectById(id);
        files.setIs_delete(true);
        int i = fileMapper.updateById(files);
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    //批量删除
    public boolean deleteBatch(List<Integer> ids) {
        // select * from sys_file where id in (id,id,id...)
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("fid", ids);
        List<Files> files = fileMapper.selectList(queryWrapper);
        int i = 0;
        for (Files file : files) {
            file.setIs_delete(true);
            i += fileMapper.updateById(file);
        }
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }


}
