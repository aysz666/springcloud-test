package com.yue.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yue.entities.Borrows;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BorrowsDao extends BaseMapper<Borrows> {
}
