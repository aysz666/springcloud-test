package com.yue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yue.dao.FaclityDao;
import com.yue.dao.StudentDao;
import com.yue.entities.Faclity;
import com.yue.entities.Student;
import com.yue.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private FaclityDao faclitydao;

    @Autowired
    private StudentDao studentdao;
    @Override
//    通过名称查询
    public Faclity faclitysbyname(String name) {
        LambdaQueryWrapper<Faclity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Faclity::getName,name);
        wrapper.select(Faclity::getName,Faclity::getLocation,Faclity::getPrice,Faclity::getTeacher,Faclity::getUsable,Faclity::getPhoto1,Faclity::getPhoto2);
        return faclitydao.selectOne(wrapper);
    }

    @Override
//    所以设备
    public List<Faclity> faclitysall() {
        LambdaQueryWrapper<Faclity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Faclity::getName,Faclity::getLocation,Faclity::getPrice,Faclity::getTeacher,Faclity::getUsable,Faclity::getPhoto1,Faclity::getPhoto2);
        return faclitydao.selectList(wrapper);
    }

    @Override
//    通过价格查询设备
    public List<Faclity> faclitysbyprice(int minprice, int maxprice) {
        QueryWrapper<Faclity> wrapper = new QueryWrapper<>();
        wrapper.ge("price",minprice).le("price",maxprice);
        return faclitydao.selectList(wrapper);
    }

    @Override
//    学生个人信息
    public Student person_select(int number) {
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Student::getNumber,number);
        wrapper.select(Student::getNumber,Student::getName,Student::getTeacher,Student::getEmail,Student::getPhoto,Student::getId);
        return studentdao.selectOne(wrapper);
    }

    @Override
//    学生个人信息修改
    public Boolean person_change(int teacherid,int number, String name, String teacher, String email, byte[] photo) {
        try{
            UpdateWrapper<Student> wrapper = new UpdateWrapper<>();
            wrapper.eq("number",number);
            if (name != null){
                wrapper.set("name",name);
            }
            if (teacher != null){
//                判断老师是否存在
                    wrapper.set("teacher",teacher);
                    wrapper.set("teacherids",teacherid);
            }
            if (email != null){
                wrapper.set("email",email);
            }
            if (photo != null){
                wrapper.set("photo",photo);
            }
            studentdao.update(null,wrapper);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
//    学生修改密码
    public Boolean person_changepw(String number, String password1, String password2) {
        UpdateWrapper<Student> wrapper = new UpdateWrapper<>();
        wrapper.eq("password",password1).eq("number",number);
        if (studentdao.selectOne(wrapper) != null){
            wrapper.set("password",password2);
            studentdao.update(null,wrapper);
            return true;
        }
        return false;
    }
}
