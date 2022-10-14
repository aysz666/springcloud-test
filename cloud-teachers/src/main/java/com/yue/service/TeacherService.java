package com.yue.service;

import com.yue.entities.Faclity;
import com.yue.entities.Student;
import com.yue.entities.Teacher;

import java.util.List;

public interface TeacherService {
     List<Student> students(String teacher);

    Student student_select(int number, String teacher);

    Boolean student_delete(int id);
     Boolean faclity_delete(int id);
     Boolean student_insert(Student student);
     Boolean faclity_insert(Faclity faclity);

     Faclity faclitysbyname(String name);

    List<Faclity> faclitysperson(String teacher);

    List<Faclity> faclitysall();

    List<Faclity> faclitysbyprice(int minprice,int maxprice);


    Boolean faclitychange(int id, Faclity faclity);

    Teacher person_select(int number);

    Boolean person_change(int number, String name, String email, byte[] photo);

    Boolean person_changepw(int number, String password1, String password2);

}
