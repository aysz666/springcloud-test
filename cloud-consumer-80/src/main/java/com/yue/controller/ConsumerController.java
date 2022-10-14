package com.yue.controller;

import com.yue.entities.Admin;
import com.yue.entities.Borrows;
import com.yue.entities.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
public class ConsumerController {
    private static final String ADMIN_REST_URL="http://localhost:8001";
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/consumer/admins/teacher")
    public List<Teacher> getTeacher(){
        return restTemplate.getForObject(ADMIN_REST_URL+"/admins/teacher",List.class);
    }

    //管理员查询某个老师
    @RequestMapping(value = "/consumer/admins/teacher/search")
    public Teacher search_teacher(@RequestBody Map<String,Object> maps){
        return restTemplate.postForObject(ADMIN_REST_URL+"/admins/teacher/search",maps,Teacher.class);
    }

    //管理员对老师进行插入
    @RequestMapping(value = "/consumer/admins/teacher/insert")
    public Map<String,Object> insert_teacher(@RequestBody Map<String,Object> maps){
        return restTemplate.postForObject(ADMIN_REST_URL+"/admins/teacher/insert",maps,Map.class);
    }

    //管理员对老师进行删除
    @RequestMapping(value = "/consumer/admins/teacher/delete")
    public Map<String, Object> delete_teacher(@RequestBody Map<String,Object> maps){

        return restTemplate.postForObject(ADMIN_REST_URL+"/admins/teacher/delete",maps,Map.class);
    }

    //查看所有学生
    @RequestMapping(value = "/consumer/admins/student")
    public List<?> manager_student(){
        return restTemplate.getForObject(ADMIN_REST_URL+"/admins/student",List.class);
    }

    //搜索某个学生
    @RequestMapping(value = "/consumer/admins/student/search")
    public Object search_student(@RequestBody Map<String,Object> maps){
        return restTemplate.postForObject(ADMIN_REST_URL+"/admins/student/search",maps,Object.class);
    }
    //对学生进行插入
    @RequestMapping(value = "/consumer/admins/student/insert")
    public Map<String, Object> insert_student(@RequestBody Map<String,Object> maps){

        return restTemplate.postForObject(ADMIN_REST_URL+"/admins/student/insert",maps,Map.class);
    }


    //删除某个学生
    @RequestMapping(value = "/consumer/admins/student/delete")
    public Map<String, Object> delete_student(@RequestBody Map<String,Object> maps){


        return restTemplate.postForObject(ADMIN_REST_URL+"/admins/student/delete",maps,Map.class);
    }
    //查看管理员信息
    @RequestMapping(value = "/consumer/admins/person")
    public Admin select(@RequestBody Map<String,Object> maps){
        return restTemplate.postForObject(ADMIN_REST_URL+"/admins/person",maps,Admin.class);
    }
    //修改管理员信息
    @RequestMapping(value = "/consumer/admins/change")
    public Map<String, Object> change(@RequestBody Map<String,Object> maps){
        return restTemplate.postForObject(ADMIN_REST_URL+"/admins/change",maps,Map.class);
    }
    //修改管理员密码
    @RequestMapping(value = "/consumer/admins/changepw")
    public Map<String, Object> change_password(@RequestBody Map<String,Object> maps){

        return restTemplate.postForObject(ADMIN_REST_URL+"/admins/changepw",maps,Map.class);
    }
    //    审核记录
    @RequestMapping("/consumer/admins/record")
    public List<Borrows> checkrecord(){
        return restTemplate.getForObject(ADMIN_REST_URL+"/admins/record",List.class);
    }
    // 待处理
    @RequestMapping("/consumer/admins/checking")
    public List<Borrows> checking(){
        return restTemplate.getForObject(ADMIN_REST_URL+"/admins/checking",List.class);
    }
    // 处理
    @RequestMapping(value = "/consumer/admins/result")
    public Map<String,Object> adminresult(@RequestBody Map<String,Object> maps){

        return restTemplate.postForObject(ADMIN_REST_URL+"/admins/result",maps,Map.class);
    }
}
