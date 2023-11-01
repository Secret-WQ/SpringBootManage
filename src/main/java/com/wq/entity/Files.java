package com.wq.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("file")
public class Files {

    @TableId(type = IdType.AUTO)
    private Integer fid;
    private String fname;
    private String ftype;
    private Long size;
    private String url;
    private String md5;
    private Boolean is_delete;
    private Boolean enable;
}
