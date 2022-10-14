package com.yue.service;

import com.yue.entities.Borrows;

import java.sql.Timestamp;
import java.util.List;

public interface BorrowsService {
//    老师借用
    Boolean teacherborrow(String name, Timestamp time1, Timestamp time2, String reason, String faclityname, String owner, int id);

    //学生借用

    Boolean studentborrow(String name, Timestamp time1, Timestamp time2, String reason, String faclityname, String teacher, String owner, int id);

    //老师审核
    Boolean teacherresult(String advice, String refuser, String refusereason, String name, String faclityname, String teacher, int id);
//管理员审核
    Boolean adminresult(int id, String advice, String refuser, String refusereason, String name, String faclityname);
//审核记录
    List<Borrows> checkrecord(String name);
//    借用记录
    List<Borrows> borrorrecord(String name);
//    代审核
    List<Borrows> checking(String name);
//管理员代审核
    List<Borrows> adminchecking();

//审核记录
    List<Borrows> admincheckrecord();
}
