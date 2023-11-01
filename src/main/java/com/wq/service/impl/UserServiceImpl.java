package com.wq.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.deploy.net.URLEncoder;
import com.wq.common.Constants;
import com.wq.entity.DTO.UserDTO;
import com.wq.entity.User;
import com.wq.exception.ServiceExcetion;
import com.wq.mapper.UserMapper;
import com.wq.service.UserService;
import com.wq.uitls.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

/**
 * @author Q.
 * @create 2022/11/4 0004 - 21:13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    UserMapper userMapper;

    private static final Log LOG = Log.get();


    private User getUserInfo(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        User one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
            System.err.println(one);
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceExcetion(Constants.CODE.CODE_500, "系统错误");
        }
        return one;
    }


    public UserDTO Login(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if (one != null) {
            BeanUtil.copyProperties(one, userDTO, true);
            //设置token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDTO.setToken(token);
            return userDTO;
        } else {
            throw new ServiceExcetion(Constants.CODE.CODE_600, "用户名或密码错误");
        }
    }

    public User register(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        User one = getOne(queryWrapper);
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(user, one, true);
            save(one);  // 把 copy完之后的用户对象存储到数据库
        } else {
            throw new ServiceExcetion(Constants.CODE.CODE_600, "用户已经存在");
        }
        return one;
    }

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

//    @Override
//    public Map<String, Object> findPage(Integer pageNum, Integer pageSize, String username) {
//        pageNum = (pageNum - 1) * pageSize;
//        username = "%" + username + "%";
//        int total = userMapper.findTotal(username);//总记录数
//        List<User> data = userMapper.findPage(pageNum, pageSize, username);
//        Map<String, Object> map = new HashMap<>();
//        map.put("data", data);
//        map.put("total", total);
//        return map;
//    }


    public IPage<User> findPage(Integer pageNum, Integer pageSize, String username) {
        IPage<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", username);
        return page(page, queryWrapper);
    }


    @Override
    public User findById(Integer id) {
        return getById(id);
    }


    @Override
    public boolean saveUser(User user) {
        if (user.getId() == null) {
            return save(user);
        } else {
            return updateById(user);
        }
    }


    //删除
    @Override
    public Boolean deleteUser(int id) {
        return removeById(id);
    }


    public void export(HttpServletResponse response) throws Exception {
        //从数据库查出所有数据
        List<User> userList = findAll();
        // 通过工具类创建writer 写出到磁盘路径
        // ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        //在内存操作写到浏览器
        ExcelWriter writer = ExcelUtil.getWriter();
        //自定义标题别名
//        writer.addHeaderAlias("username", "username");
//        writer.addHeaderAlias("password", "password");
//        writer.addHeaderAlias("nickname", "nickname");
//        writer.addHeaderAlias("email", "email");
//        writer.addHeaderAlias("phone", "phone");
//        writer.addHeaderAlias("address", "address");
//        writer.addHeaderAlias("create_time", "create_time");
//        writer.addHeaderAlias("avatar_url", "avatar_url");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(userList, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }


    public Boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<User> users = reader.readAll(User.class);

//        InputStream inputStream = file.getInputStream();
//        ExcelReader reader = ExcelUtil.getReader(inputStream);
//        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);

        // 方式2：忽略表头的中文，直接读取表的内容
       /* List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        for (List<Object> row : list) {
            User user = new User();
            user.setUsername(row.get(0).toString());
            user.setPassword(row.get(1).toString());
            user.setNickname(row.get(2).toString());
            user.setEmail(row.get(3).toString());
            user.setPhone(row.get(4).toString());
            user.setAddress(row.get(5).toString());
            user.setAvatar_url(row.get(6).toString());
            users.add(user);
        }*/
        return saveBatch(users);
    }


}
