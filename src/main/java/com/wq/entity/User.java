package com.wq.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author Q.
 * @create 2022/11/4 0004 - 15:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName
@ToString
public class User  {
    
    @TableId(type = IdType.AUTO)
    private Integer id; //id
    private String username;//用户名
    private String password;//密码
    private String nickname;//昵称
    private String email;//邮箱
    private String phone;//电话
    private String address;//地址
    private Date create_time;
    private String avatar_url;//头像



}
