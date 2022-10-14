package com.yue.service;

import com.yue.entities.Faclity;
import com.yue.entities.Student;

import java.util.List;

public interface StudentService {

     Faclity faclitysbyname(String name);
     List<Faclity> faclitysall();
     List<Faclity> faclitysbyprice(int minprice, int maxprice);


    Student person_select(int number);

    Boolean person_change(int teacherid, int number, String name, String teacher, String email, byte[] photo);

    Boolean person_changepw(String number, String password1, String password2);
}
