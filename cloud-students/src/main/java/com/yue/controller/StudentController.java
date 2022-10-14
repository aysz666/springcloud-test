package com.yue.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yue.dao.TeacherDao;
import com.yue.entities.Borrows;
import com.yue.entities.Faclity;
import com.yue.entities.Student;
import com.yue.entities.Teacher;
import com.yue.service.impl.BorrowsServiceImpl;
import com.yue.service.impl.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private BorrowsServiceImpl borrowsServiceimpl;
    @Autowired
    private StudentServiceImpl studentServiceimpl;
    @Autowired
    private TeacherDao teacherdao;

//    个人信息
    @RequestMapping(value = "/person",method = RequestMethod.POST)
    public Student personal(@RequestBody Map<String,Object> map){
        int number = Integer.parseInt(String.valueOf(map.get("number"))) ;
        return studentServiceimpl.person_select(number);
    }
//    修改信息
    @RequestMapping(value = "/change",method = RequestMethod.POST)
    public Map<String, Object> person_change(@RequestBody Map<String,Object> map) {

        int number = Integer.parseInt(String.valueOf(map.get("number")));
        String name = String.valueOf(map.get("name"));
        String email = String.valueOf(map.get("email"));

        Map<String,Object> maps = new HashMap<>();

//        判断老师是否存在并获取老师id
        String teacher = String.valueOf(map.get("teacher"));
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        wrapper.eq("name",teacher);
        Teacher teacher1 = teacherdao.selectOne(wrapper);
        if (teacher1 == null){
            maps.put("result","老师不存在！");
            return maps;
        }
        int teacherid = teacher1.getId();

        byte[] photo = null;
//        图片转化
        if (!Objects.equals(String.valueOf(map.get("photo")), "")){
            photo = Base64.getDecoder().decode((String) map.get("photo"));
        }
        if (studentServiceimpl.person_change( teacherid,number,  name,  teacher1.getName(),  email,  photo )){
            maps.put("result","修改成功！");
        }
        else {
            maps.put("result","修改失败！");
        }
        return maps;
    }
//    修改密码
    @RequestMapping("/changepw")
    public Map<String, Object> change_password(@RequestBody Map<String,Object> maps){

        String password1 = String.valueOf(maps.get("password1"));
        String password2 = String.valueOf(maps.get("password2"));
        String number = String.valueOf(maps.get("number"));

        Map<String,Object> map = new HashMap<>();
        if (studentServiceimpl.person_changepw(number,password1,password2)){
            map.put("result","处理成功！");
        }else {
            map.put("result","旧密码错误！");
        }


        return map;
    }
//    所有设备
    @RequestMapping("/faclites")
    public List<Faclity> faclities(){
        return studentServiceimpl.faclitysall();
    }
//    价格范围
    @RequestMapping("/faclitiesbyprice")
    public List<Faclity> faclities_price(@RequestBody Map<String,Object> map){

        int minprice = Integer.parseInt(String.valueOf(map.get("minprice")));
        int maxprice = Integer.parseInt(String.valueOf(map.get("maxprice")));


        return studentServiceimpl.faclitysbyprice(minprice,maxprice);
    }
//    设备名字查询
    @RequestMapping("/faclitybyname")
    public Faclity faclity_name(@RequestBody Map<String,Object> map){

        String name = (String) map.get("name");
        return studentServiceimpl.faclitysbyname(name);
    }
//    设备借用
    @RequestMapping(value = "/borrow",method = RequestMethod.POST)
    public Map<String,Object> student_borrow(@RequestBody Map<String,Object> maps){
//        时间转化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time = (String) maps.get("mintime");
        LocalDateTime localDateTime = LocalDateTime.parse(time.replaceAll("T"," ")+":00",dateTimeFormatter);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Timestamp mintime = new Timestamp(date.getTime());

        String time1 = (String) maps.get("maxtime");
        LocalDateTime localDateTime1 = LocalDateTime.parse(time1.replaceAll("T"," ")+":00",dateTimeFormatter);
        Date date1 = Date.from(localDateTime1.atZone(ZoneId.systemDefault()).toInstant());
        Timestamp maxtime = new Timestamp(date1.getTime());


        String student = (String) maps.get("student");
        String reason = (String) maps.get("reason");
        String teacher = (String) maps.get("teacher");
        String faclityname = (String) maps.get("faclityname");
        String owner = (String) maps.get("owner");
        int id= Integer.parseInt(String.valueOf(maps.get("id")));


        Map<String,Object> map = new HashMap<>();
        if (borrowsServiceimpl.studentborrow(student,mintime,maxtime,reason,faclityname,teacher,owner,id)){
            map.put("result","借用成功！");
        }
        else {
            map.put("result","该设备已被借用！");
        }


        return map;
    }
    //查看记录
    @RequestMapping(value = "/borrows",method = RequestMethod.POST)
    public List<Borrows> borrows(@RequestBody Map<String,Object> maps){
        String name = (String) maps.get("name");
        return borrowsServiceimpl.borrorrecord(name);
    }

}
