package com.yue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yue.dao.FaclityDao;
import com.yue.dao.StudentDao;
import com.yue.dao.TeacherDao;
import com.yue.entities.Faclity;
import com.yue.entities.Student;
import com.yue.entities.Teacher;
import com.yue.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private StudentDao studentdao;
    @Autowired
    private TeacherDao teacherdao;
    @Autowired
    private FaclityDao faclitydao;

//    所有自己的学生
    @Override
    public List<Student> students(String teacher) {
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Student::getTeacher,teacher);
        wrapper.select(Student::getId,Student::getName,Student::getNumber,Student::getEmail,Student::getPhoto);
        return studentdao.selectList(wrapper);
    }
//通过学号查找自己的学生
    @Override
    public Student student_select(int number,String teacher) {
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Student::getNumber,number).eq(Student::getTeacher,teacher);
        wrapper.select(Student::getId,Student::getName,Student::getNumber,Student::getTeacher,Student::getEmail,Student::getPhoto);
        return studentdao.selectOne(wrapper);
    }

//    通过id删除学生
    @Override
    public Boolean student_delete(int id) {
        try{
            studentdao.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }

    }

//    通过id删除设备
    @Override
    public Boolean faclity_delete(int id) {
        try{
            faclitydao.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

//    添加学生
    @Override
    public Boolean student_insert(Student student) {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("number",student.getNumber());
        if(studentdao.selectList(wrapper).size() != 0){
            return false;
        }
        else {
            try{
                studentdao.insert(student);
                return true;
            }catch (Exception e){
                System.out.println(e);
                return false;
            }
        }
    }
//添加设
    @Override
    public Boolean faclity_insert(Faclity faclity) {
        QueryWrapper<Faclity> wrapper = new QueryWrapper<>();
        wrapper.eq("name",faclity.getName());
        if(faclitydao.selectList(wrapper).size() == 0){
            faclitydao.insert(faclity);
            return true;
        }
        else {
                return false;
        }
    }

//    通过名字查询设备
    @Override
    public Faclity faclitysbyname(String name) {
        LambdaQueryWrapper<Faclity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Faclity::getName,name);
        wrapper.select(Faclity::getName,Faclity::getLocation,Faclity::getPrice,Faclity::getTeacher,Faclity::getUsable,Faclity::getPhoto1,Faclity::getPhoto2);
        return faclitydao.selectOne(wrapper);
    }
//    通过名字查询自己的设备
    public Faclity faclitysbyname(String name,String teacher) {
        LambdaQueryWrapper<Faclity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Faclity::getName,name).eq(Faclity::getTeacher,teacher);
        wrapper.select(Faclity::getName,Faclity::getLocation,Faclity::getPrice,Faclity::getTeacher,Faclity::getUsable,Faclity::getPhoto1,Faclity::getPhoto2);
        return faclitydao.selectOne(wrapper);
    }
//    所有自己的设备
    @Override
    public List<Faclity> faclitysperson(String teacher) {
        LambdaQueryWrapper<Faclity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Faclity::getTeacher,teacher);
        wrapper.select(Faclity::getId,Faclity::getName,Faclity::getLocation,Faclity::getPrice,Faclity::getBorrower,Faclity::getUsable,Faclity::getPhoto1,Faclity::getPhoto2);
        return faclitydao.selectList(wrapper);
    }
//所有设备
    @Override
    public List<Faclity> faclitysall() {
        LambdaQueryWrapper<Faclity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Faclity::getName,Faclity::getLocation,Faclity::getPrice,Faclity::getTeacher,Faclity::getUsable,Faclity::getPhoto1,Faclity::getPhoto2);
        return faclitydao.selectList(wrapper);
    }
//通过价格查询
    @Override
    public List<Faclity> faclitysbyprice(int minprice, int maxprice) {
        QueryWrapper<Faclity> wrapper = new QueryWrapper<>();
        wrapper.ge("price",minprice).le("price",maxprice);
        return faclitydao.selectList(wrapper);
    }
//    通过价格自己的设备
    public List<Faclity> faclitysbyprice(int minprice, int maxprice,String teacher) {
        QueryWrapper<Faclity> wrapper = new QueryWrapper<>();
        wrapper.ge("price",minprice).le("price",maxprice).eq("teacher",teacher);
        return faclitydao.selectList(wrapper);
    }
//修改设备信息
    @Override
    public Boolean faclitychange(int id, Faclity faclity) {
        UpdateWrapper<Faclity> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",id);
        if (faclity.getName()!=null){
            wrapper.set("name",faclity.getName());
        }
        if (faclity.getPrice()!=0){
            wrapper.set("price",faclity.getPrice());
        }
        if (faclity.getLocation()!=null){
            wrapper.set("location",faclity.getLocation());
        }
        if (faclity.getPhoto1()!=null){
            wrapper.set("photo1",faclity.getPhoto1());
        }
        if (faclity.getPhoto2()!=null){
            wrapper.set("photo2",faclity.getPhoto2());
        }
        try{
            faclitydao.update(null,wrapper);
            return true;
        }catch (Exception e){
            return false;
        }
    }

//    个人信息
    @Override
    public Teacher person_select(int number) {
        LambdaQueryWrapper<Teacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Teacher::getNumber,number);
        wrapper.select(Teacher::getNumber,Teacher::getName,Teacher::getEmail,Teacher::getPhoto,Teacher::getId);
        return teacherdao.selectOne(wrapper);
    }

//    修改个人信息
    @Override
    public Boolean person_change(int number, String name, String email, byte[] photo) {
        UpdateWrapper<Teacher> wrapper = new UpdateWrapper<>();
        wrapper.eq("number",number);
        if (name != null){
            wrapper.set("name",name);
        }
        if (email != null){
            wrapper.set("email",email);
        }
        if (photo != null){
            wrapper.set("photo",photo);
        }
        try {
            teacherdao.update(null,wrapper);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    /*
    * 修改密码
    * */
    @Override
//    修改密码
    public Boolean person_changepw(int number, String password1, String password2) {
        UpdateWrapper<Teacher> wrapper = new UpdateWrapper<>();
        wrapper.eq("password",password1).eq("number",number);
        wrapper.set("password",password2);
        if (teacherdao.selectOne(wrapper) != null){
            teacherdao.update(null,wrapper);
            return true;
        }else {
            return false;
        }

    }
}
