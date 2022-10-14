package com.yue.controller;

import com.yue.entities.Borrows;
import com.yue.entities.Faclity;
import com.yue.entities.Student;
import com.yue.entities.Teacher;
import com.yue.service.impl.BorrowsServiceImpl;
import com.yue.service.impl.TeacherServiceImpl;
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
@RequestMapping("/teachers")
public class TeacherController {
    @Autowired
    private BorrowsServiceImpl borrowsServiceimpl;
    @Autowired
    private TeacherServiceImpl teacherServiceimpl;

    //    个人信息
    @RequestMapping(value = "/person")
    public Teacher personall(@RequestBody Map<String,Object> maps){
        int number = Integer.parseInt(String.valueOf(maps.get("number"))) ;
        return teacherServiceimpl.person_select(number);
    }
    //    修改信息
    @RequestMapping(value = "/change",method = RequestMethod.POST)
    public Map<String, Object> person_change(@RequestBody Map<String,Object> maps){
//        获取参数
        int number = Integer.parseInt(String.valueOf(maps.get("number")));
        String name = String.valueOf(maps.get("name"));
        String email = String.valueOf(maps.get("email"));

        byte[] photo = null;
//        获取照片并转化
        if (maps.get("photo") != ""){
            photo = Base64.getDecoder().decode(String.valueOf(maps.get("photo")));
        }

        Map<String,Object> map = new HashMap<>();

//        修改操作
        if (teacherServiceimpl.person_change( number,  name,  email,  photo )){
            map.put("result","信息修改成功！");
        }
        else {
            map.put("result","信息修改失败！");
        }

        return map;
    }
    //    修改密码
    @RequestMapping(value = "/changepw",method = RequestMethod.POST)
    public Map<String,Object> change_password(@RequestBody Map<String,Object> maps){

        String password1 = String.valueOf(maps.get("password1"));
        String password2 = String.valueOf(maps.get("password2"));
        int number = Integer.parseInt(String.valueOf(maps.get("number")));

        Map<String,Object> map = new HashMap<>();

        if (teacherServiceimpl.person_changepw(number,password1,password2)){
            map.put("result","密码修改成功！");
        }else {
            map.put("result","密码修改失败！旧密码错误！");
        }

        return map;
    }
    //    所有设备
    @RequestMapping("/faclites")
    public List<Faclity> faclities(){
        return teacherServiceimpl.faclitysall();
    }
    //    价格范围
    @RequestMapping(value = "/faclitiesbyprice",method = RequestMethod.POST)
    public List<Faclity> faclitiesbyprice(@RequestBody Map<String,Object> maps){

        int minprice = Integer.parseInt(String.valueOf(maps.get("minprice")));
        int maxprice = Integer.parseInt(String.valueOf(maps.get("maxprice")));
        String teacher = String.valueOf(maps.get("teacher"));


        if (Objects.equals(teacher, "")){
            return teacherServiceimpl.faclitysbyprice(minprice,maxprice);
        }
        else {
            return teacherServiceimpl.faclitysbyprice(minprice,maxprice,teacher);
        }

    }
    //    设备名字查询
    @RequestMapping(value = "/faclitybyname",method = RequestMethod.POST)
    public Faclity faclitybyname(@RequestBody Map<String,Object> maps){

        String name = String.valueOf(maps.get("name"));
        String teacher = String.valueOf(maps.get("teacher"));


        if (Objects.equals(teacher, "")){
            return teacherServiceimpl.faclitysbyname(name);
        }else {
            return teacherServiceimpl.faclitysbyname(name,teacher);
        }

    }
    //    自己的设备
@RequestMapping(value = "/myfaclites",method = RequestMethod.POST)
    public List<Faclity> myfaclites(@RequestBody Map<String,Object> maps){

        String name = String.valueOf(maps.get("name"));

        return teacherServiceimpl.faclitysperson(name);
    }
//    修改自己的设备
    @RequestMapping("/changemyfaclity")
    public Map<String,Object> changefaclity(@RequestBody Map<String,Object> maps){

//        获取参数
        int id=Integer.parseInt(String.valueOf(maps.get("id")));
        int price = Integer.parseInt(String.valueOf(maps.get("price")));
        String name = String.valueOf(maps.get("name"));
        String location = String.valueOf(maps.get("location"));

        byte[] photo1 = null;
        byte[] photo2 = null;

//        照片
        if (maps.get("photo1")!=""){
            photo1 = Base64.getDecoder().decode((String) maps.get("photo1"));

        }
        if (maps.get("photo2")!=""){
            photo2 = Base64.getDecoder().decode((String) maps.get("photo2"));
        }

//        创造对象
        Faclity faclity = new Faclity();
        faclity.setName(name);
        faclity.setLocation(location);
        faclity.setPhoto1(photo1);
        faclity.setPhoto2(photo2);
        faclity.setPrice(price);

        Map<String,Object> map = new HashMap<>();
//        修改操作
        if (teacherServiceimpl.faclitychange(id,faclity)){
            map.put("result","设备修改成功！");
        }else {
            map.put("result","设备修改失败！");
        }

        return map;
    }
    //    删除自己的设备
    @RequestMapping(value = "/deletemyfaclity",method = RequestMethod.POST)
    public Map<String, Object> deletefaclity(@RequestBody Map<String,Object> maps){
//        获取id
        int id = Integer.parseInt(String.valueOf(maps.get("id")));

        Map<String,Object> map = new HashMap<>();

//        删除操作
        if (teacherServiceimpl.faclity_delete(id)){
            map.put("result","删除成功！");
        }else {
            map.put("result","出现异常");
        }
//        返回结果
        return map;
    }
//    删除学生
    @RequestMapping(value = "/deletemystudent",method = RequestMethod.POST)
    public Map<String, Object> deletestudent(@RequestBody Map<String,Object> maps){

        int id = Integer.parseInt(String.valueOf(maps.get("id")));

        Map<String,Object> map = new HashMap<>();


        if (teacherServiceimpl.student_delete(id)){
        map.put("result","删除成功！");
        }else {
            map.put("result","出现异常");
        }

        return map;
    }
//    自己的学生
    @RequestMapping(value = "/mystudent",method = RequestMethod.POST)
    public List<Student> mystudent(@RequestBody Map<String,Object> maps){
        String name = String.valueOf(maps.get("name"));
        return teacherServiceimpl.students(name);
    }
//    添加自己的学生
    @RequestMapping(value = "/studentinsert",method = RequestMethod.POST)
    public Map<String,Object> student_insert(@RequestBody Map<String,Object> maps){

        Map<String,Object> map = new HashMap<>();
        int number = Integer.parseInt(String.valueOf(maps.get("number")));
        String name = String.valueOf(maps.get("name"));
        String email = String.valueOf(maps.get("email"));
        String teacher = String.valueOf(maps.get("teacher"));
        byte[] photo = null;
        if (maps.get("photo")!=""){
            photo = Base64.getDecoder().decode((String) maps.get("photo"));
        }
        int teacherid = Integer.parseInt(String.valueOf(maps.get("teacherid")));


        Student student = new Student();
        student.setNumber(number);
        student.setName(name);
        student.setEmail(email);
        student.setPhoto(photo);
        student.setTeacher(teacher);
        student.setPassword("123456");
        student.setTeacherids(teacherid);


        if (teacherServiceimpl.student_insert(student)){
            map.put("result","添加成功！");
        }else {
            map.put("result","添加失败！");
        }

        return map;
    }
    //添加设备
    @RequestMapping(value = "/faclityinsert",method = RequestMethod.POST)
    public Map<String,Object> faclity_insert(@RequestBody Map<String,Object> maps){

        Map<String,Object> map = new HashMap<>();
        int price = Integer.parseInt(String.valueOf(maps.get("price")));
        String name = String.valueOf(maps.get("name"));
        String location = String.valueOf(maps.get("location"));
        String teacher = String.valueOf(maps.get("teacher"));
        int teacherid = Integer.parseInt(String.valueOf(maps.get("id")));
        byte[] photo1 = null;
        if (maps.get("photo")!=""){
            photo1 = Base64.getDecoder().decode((String) maps.get("photo1"));
        }
        byte[] photo2 = null;
        if (maps.get("photo")!=""){
            photo2 = Base64.getDecoder().decode((String) maps.get("photo2"));
        }


        Faclity faclity = new Faclity();
        faclity.setLocation(location);
        faclity.setName(name);
        faclity.setTeacher(teacher);
        faclity.setUsable(true);
        faclity.setPhoto1(photo1);
        faclity.setPhoto2(photo2);
        faclity.setPrice(price);
        faclity.setTeacherids(teacherid);


        if (teacherServiceimpl.faclity_insert(faclity)){
            map.put("result","添加成功！");
        }else {
            map.put("result","添加失败！");
        }
        return map;
    }
//    查询自己的学生
    @RequestMapping(value = "/studentselect",method = RequestMethod.POST)
    public Student student_select(@RequestBody Map<String,Object> maps){

        int number = Integer.parseInt(String.valueOf(maps.get("number")));
        String teacher = String.valueOf(maps.get("teacher"));

        return teacherServiceimpl.student_select(number,teacher);
    }
//    设备借用
    @RequestMapping(value = "/borrow",method = RequestMethod.POST)
    public Map<String,Object> teacherborror(@RequestBody Map<String,Object> maps){
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
        
        String teacher = String.valueOf(maps.get("teacher"));
        String reason = String.valueOf(maps.get("reason"));
        String faclityname = String.valueOf(maps.get("faclityname"));
        String owner = String.valueOf(maps.get("owner"));
        int id = Integer.parseInt(String.valueOf(maps.get("id")));

        Map<String,Object> map = new HashMap<>();
        if (borrowsServiceimpl.teacherborrow(teacher,mintime,maxtime,reason,faclityname,owner,id)){
            map.put("result","借用成功！");
        }
        else {
            map.put("result","该设备已被借用！");
        }

        return map;
    }
//    审核设备
    @RequestMapping(value = "/result",method = RequestMethod.POST)
    public Map<String,Object> teacherresult(@RequestBody Map<String,Object> maps){
//        获取参数
        String advice =String.valueOf(maps.get("advice"));
        int id = Integer.parseInt(String.valueOf(maps.get("id")));
        String refuser =String.valueOf(maps.get("refuser"));
        String refusereason =String.valueOf(maps.get("refusereason"));
        String name =String.valueOf(maps.get("name"));
        String faclityname =String.valueOf(maps.get("faclityname"));
        String teacher =String.valueOf(maps.get("teacher"));

        Map<String,Object> map = new HashMap<>();
        if (borrowsServiceimpl.teacherresult(advice,refuser,refusereason,name,faclityname,teacher,id)){
            map.put("result","处理成功！");
        }
        else {
            map.put("result","处理失败！");
        }


        return map;
    }
//    查看借用记录
    @RequestMapping(value = "/borrows",method = RequestMethod.POST)
    public List<Borrows> borrows(@RequestBody Map<String,Object> maps){
        String name = String.valueOf(maps.get("name"));
        return borrowsServiceimpl.borrorrecord(name);
    }
    //查看审核记录
    @RequestMapping(value = "/checks",method = RequestMethod.POST)
    public List<Borrows> checks(@RequestBody Map<String,Object> maps){
        String name = String.valueOf(maps.get("name"));
        return borrowsServiceimpl.checkrecord(name);
    }
    //待处理项
    @RequestMapping(value = "/checking",method = RequestMethod.POST)
    public List<Borrows> cherking(@RequestBody Map<String,Object> maps){
        String name = String.valueOf(maps.get("name"));
        return borrowsServiceimpl.checking(name);
    }
}
