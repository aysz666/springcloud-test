package com.yue.service;

import com.yue.entities.Admin;
import com.yue.entities.Student;
import com.yue.entities.Teacher;

import java.util.List;

public interface AdminService {
//    所以学生
     List<? extends Object> students();
//     学号查找
     Object student_select(int number);
//     删除
     Boolean student_delete(int id);
//     添加
     Boolean student_insert(Student student);

//所有老师
     List<Teacher> teachers();
//工号查询
    Teacher teacher_select(int number);
//删除
    Boolean teacher_delete(int number);

//添加老师
    Boolean teacher_insert(Teacher teacher);
//个人信息
    Admin person_select(String number);
//修改个人信息
    Boolean person_change(String number, String email, byte[] photo);

    Boolean person_changepw(String number, String password1, String password2);
}
