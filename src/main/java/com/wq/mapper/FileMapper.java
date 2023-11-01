package com.wq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wq.entity.Files;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper  extends BaseMapper<Files> {
}
