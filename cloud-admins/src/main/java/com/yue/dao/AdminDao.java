package com.yue.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yue.entities.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminDao extends BaseMapper<Admin> {
}
