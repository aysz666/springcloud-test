package com.yue.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yue.entities.Teacher;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeacherDao extends BaseMapper<Teacher> {
}
