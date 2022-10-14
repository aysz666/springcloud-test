package com.yue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yue.dao.AdminDao;
import com.yue.dao.StudentDao;
import com.yue.dao.TeacherDao;
import com.yue.entities.Admin;
import com.yue.entities.Student;
import com.yue.entities.Teacher;
import com.yue.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AdminSeviceImpl implements AdminService {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private AdminDao admindao;
    @Autowired
    private StudentDao studentdao;
    @Autowired
    private TeacherDao teacherdao;

    /*
    * 返回所有学生数据
    *
    * */
    @Override
    public List<?> students() {
        if (Boolean.TRUE.equals(redisTemplate.hasKey("all_students"))){
            return redisTemplate.opsForHash().values("all_students");
        }
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Student::getId,Student::getName,Student::getNumber,Student::getTeacher,Student::getEmail,Student::getPhoto);
        List<Student> students = studentdao.selectList(wrapper);
        for (Student s : students) {
            redisTemplate.opsForHash().put("all_students",String.valueOf(s.getNumber()),s);
        }
        return students;
    }
    /*
     * number:学号
     * 通过学号查找某一个学生
     *
     * */
    @Override
    @Cacheable(value = "all_students",key="#number")
    public Object student_select(int number) {
        Object b = redisTemplate.opsForHash().get("all_students",String.valueOf(number));
        if(b==null){
            LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Student::getNumber,number);
            wrapper.select(Student::getId,Student::getName,Student::getNumber,Student::getTeacher,Student::getEmail,Student::getPhoto);
            return studentdao.selectOne(wrapper);
        }
        return b;
    }
    /*
     * id:学生的id
     *通过id删除学生
     * */
    @Override
    public Boolean student_delete(int id) {
        try {
            int i = studentdao.deleteById(id);
            redisTemplate.opsForHash().delete("all_students",id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /*
     * 需要接受一个student对象
     * 插入学生
     *
     * */
    @Override
    public Boolean student_insert(Student student) {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("name",student.getName()).eq("number",student.getNumber());
        Student student1 = studentdao.selectOne(wrapper);
        if (student1 == null){
            studentdao.insert(student);
            return true;
        }
        return false;
    }
    /*
     * 返回所有老师数据
     *
     * */
    @Override
    public List<Teacher> teachers() {
        LambdaQueryWrapper<Teacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Teacher::getId,Teacher::getName,Teacher::getNumber,Teacher::getEmail,Teacher::getPhoto);
        return teacherdao.selectList(wrapper);
    }
    /*
     * 接受老师工号并查询返回
     *
     * */
    @Override
    public Teacher teacher_select(int number) {
        LambdaQueryWrapper<Teacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Teacher::getNumber,number);
        wrapper.select(Teacher::getId,Teacher::getName,Teacher::getNumber,Teacher::getEmail,Teacher::getPhoto);
        return teacherdao.selectOne(wrapper);
    }
    /*
     * 接受老师id并删除
     *
     * */
    @Override
    public Boolean teacher_delete(int id) {
        try{
            teacherdao.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    /*
     * 接受老师对象并添加到数据库
     *
     * */
    @Override
    public Boolean teacher_insert(Teacher teacher) {
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        wrapper.eq("name",teacher.getName()).eq("number",teacher.getNumber());
        Teacher teacher1 = teacherdao.selectOne(wrapper);
        if (teacher1 == null){
            teacherdao.insert(teacher);
            return true;
        }
        return false;

    }
    /*
     * 查询个人信息
     *
     * */
    @Override
    public Admin person_select(String number) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getNumber,number);
        wrapper.select(Admin::getNumber,Admin::getEmail,Admin::getPhoto);
        return admindao.selectOne(wrapper);
    }
    /*
     * 修改个人信息
     *
     * */
    @Override
    public Boolean person_change(String number, String email, byte[] photo) {
        UpdateWrapper<Admin> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",1);
        if (number != null){
            wrapper.set("number",number);
        }
        if (email != null){
            wrapper.set("email",email);
        }
        if (photo != null){
            wrapper.set("photo",photo);
        }
        try {
            admindao.update(null,wrapper);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /*
     * 修改密码
     *
     * */
    @Override
    public Boolean person_changepw(String number, String password1, String password2) {
        UpdateWrapper<Admin> wrapper = new UpdateWrapper<>();
        wrapper.eq("password",password1).eq("number",number);
        wrapper.set("password",password2);
        if (admindao.selectOne(wrapper) == null){
            return false;
        }else {
            try {
                admindao.update(null,wrapper);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

    }
}
