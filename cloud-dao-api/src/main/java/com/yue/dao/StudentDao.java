package com.yue.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yue.entities.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentDao extends BaseMapper<Student> {
}
