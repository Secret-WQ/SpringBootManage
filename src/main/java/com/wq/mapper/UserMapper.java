package com.wq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wq.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Q.
 * @create 2022/11/4 0004 - 15:56
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    //查询所有信息
    List<User> findAll();

    //分页查询
    @Select("select * from user where username like #{username} limit #{pageNum},#{pageSize}")
    List<User> findPage(@Param("pageNum")Integer pageNum,@Param("pageSize") Integer pageSize,@Param("username") String username);

    //查询总条数
    @Select("select COUNT(id) FROM user where username like #{username}")
    int findTotal(String username);

    //通过id得到部门
    User findById(Integer id);

    //添加
    int addUser(User user);

    //修改
    int updateUser(User user);

    //删除
    @Delete("delete from user where id = #{id};")
    int deleteUser(@Param("id") int id);


}
