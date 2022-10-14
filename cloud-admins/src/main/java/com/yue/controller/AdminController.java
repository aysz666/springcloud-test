package com.yue.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yue.dao.TeacherDao;
import com.yue.entities.Admin;
import com.yue.entities.Borrows;
import com.yue.entities.Student;
import com.yue.entities.Teacher;
import com.yue.service.impl.BorrowsServiceImpl;
import com.yue.service.impl.AdminSeviceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//管理员业务

@RestController
@RequestMapping("/admins")
public class AdminController {
    @Autowired
    private BorrowsServiceImpl borrowsServiceimpl;
    @Autowired
    private AdminSeviceImpl adminSeviceimpl;

    @Autowired
    TeacherDao teacherdao;

    //管理员查看所有老师的信息
    @RequestMapping(value = "/teacher",method = RequestMethod.GET)
    public List<Teacher> manager_teacher(){
        return adminSeviceimpl.teachers();
    }

    //管理员查询某个老师
    @RequestMapping(value = "/teacher/search",method = RequestMethod.POST)
    public Teacher search_teacher(@RequestBody Map<String,Object> maps){
        int number = Integer.parseInt(String.valueOf(maps.get("number")));
        return adminSeviceimpl.teacher_select(number);
    }
    //管理员对老师进行插入
    @RequestMapping(value = "/teacher/insert",method = RequestMethod.POST)
    public Map<String,Object> insert_teacher(@RequestBody Map<String,Object> maps){

        Map<String,Object> map = new HashMap<>();
        int number = Integer.parseInt(String.valueOf(maps.get("number")));
        String name = String.valueOf(maps.get("name"));
        String email = String.valueOf(maps.get("email"));
        String password = "123456";
        byte[] photo = null;

        if (maps.get("photo")!=""){
            photo = Base64.getDecoder().decode((String) maps.get("photo"));

        }
        if (adminSeviceimpl.teacher_insert(new Teacher(number,name,email,password,photo))){
            map.put("result","添加成功！");
        }
        else {
            map.put("result","工号已存在！");
        }


        return map;
    }
    //管理员对老师进行删除
    @RequestMapping(value = "/teacher/delete",method = RequestMethod.POST)
    public Map<String, Object> delete_teacher(@RequestBody Map<String,Object> maps){

        int id = Integer.parseInt(String.valueOf(maps.get("id")));
        Map<String,Object> map = new HashMap<>();


        if (adminSeviceimpl.teacher_delete(id)){
            map.put("result","处理成功！");
        }
        else {
            map.put("result","出现异常！");
        }


        return map;
    }
    //查看所有学生
    @RequestMapping(value = "/student",method = RequestMethod.GET)
    public List<?> manager_student(){
        return adminSeviceimpl.students();
    }
    //搜索某个学生
    @RequestMapping(value = "/student/search",method = RequestMethod.POST)
    public Object search_student(@RequestBody Map<String,Object> maps){
        int number = Integer.parseInt(String.valueOf(maps.get("number")));
        return adminSeviceimpl.student_select(number);
    }
    //对学生进行插入
    @RequestMapping(value = "/student/insert",method = RequestMethod.POST)
    public Map<String, Object> insert_student(@RequestBody Map<String,Object> maps){

        Map<String,Object> map = new HashMap<>();
        int number = Integer.parseInt(String.valueOf(maps.get("number")));
        String name = String.valueOf(maps.get("name"));
        String email = String.valueOf(maps.get("email"));
        String teacher = String.valueOf(maps.get("teacher"));

        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        wrapper.eq("name",teacher);
        Teacher teacher1 = teacherdao.selectOne(wrapper);
        int teacherid = 0;

        if (teacher1!=null){
            teacherid = teacher1.getId();
        }else {
            map.put("result","老师不存在!");
            return map;
        }

        String password = "123456";
        byte[] photo = Base64.getDecoder().decode((String) maps.get("photo"));


        if (adminSeviceimpl.student_insert(new Student(number,name,teacher,email,password,photo,teacherid))){
            map.put("result","添加成功!");
        }
        else {
            map.put("result","添加失败！账户已存在！");
        }


        return map;
    }


    //删除某个学生
    @RequestMapping(value = "/student/delete",method = RequestMethod.POST)
    public Map<String, Object> delete_student(@RequestBody Map<String,Object> maps){


        int id = Integer.parseInt(String.valueOf(maps.get("id")));
        Map<String,Object> map = new HashMap<>();
        if ( adminSeviceimpl.student_delete(id))
        {
            map.put("result","处理成功！");

        }
        else {
            map.put("result","出现异常！");
        }


        return map;
    }
    //查看管理员信息
    @RequestMapping(value = "/person",method = RequestMethod.POST)
    public Admin select(@RequestBody Map<String,Object> maps){
        String number = (String) maps.get("number");
        return adminSeviceimpl.person_select(number);
    }
    //修改管理员信息
    @RequestMapping(value = "/change",method = RequestMethod.POST)
    public Map<String, Object> change(@RequestBody Map<String,Object> maps){


        String number =String.valueOf(maps.get("number"));
        String email = String.valueOf(maps.get("email"));
        byte[] photo = null;
        if (maps.get("photo") != ""){
            photo = Base64.getDecoder().decode(String.valueOf(maps.get("photo")));
        }
        Map<String,Object> map = new HashMap<>();


        if (adminSeviceimpl.person_change(number, email, photo)) {
            map.put("result","处理成功！");


        } else {
            map.put("result","出现异常！");
        }

        return map;
    }
    //修改管理员密码
    @RequestMapping(value = "/changepw",method = RequestMethod.POST)
    public Map<String, Object> change_password(@RequestBody Map<String,Object> maps){


        String password1 = String.valueOf(maps.get("password1"));
        String password2 = String.valueOf(maps.get("password2"));
        String number = String.valueOf(maps.get("number"));
        Map<String,Object> map = new HashMap<>();

        if (adminSeviceimpl.person_changepw(number,password1,password2)){

            map.put("result","处理成功！");
        }else {
            map.put("result","旧密码错误！");
        }


        return map;
    }
//    审核记录
    @RequestMapping(value = "/record",method = RequestMethod.GET)
    public List<Borrows> checkrecord(){
        return borrowsServiceimpl.admincheckrecord();
    }
    // 待处理
    @RequestMapping(value = "/checking",method = RequestMethod.GET)
    public List<Borrows> checking(){
        return borrowsServiceimpl.adminchecking();
    }
    // 处理
    @RequestMapping(value = "/result",method = RequestMethod.POST)
    public Map<String,Object> adminresult(@RequestBody Map<String,Object> maps){

        String advice =String.valueOf(maps.get("advice"));
        int id = Integer.parseInt(String.valueOf(maps.get("id")));
        String refuser ="管理员";
        String refusereason =String.valueOf(maps.get("refusereason"));
        String name =String.valueOf(maps.get("name"));
        String faclityname =String.valueOf(maps.get("faclityname"));
        Map<String,Object> map = new HashMap<>();


        if(borrowsServiceimpl.adminresult(id,advice,refuser,refusereason,name,faclityname)){
            map.put("result","处理成功！");
        }
        else {
            map.put("result","出现异常！");
        }

        return map;
    }
}
