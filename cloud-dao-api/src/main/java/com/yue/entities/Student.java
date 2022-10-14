package com.yue.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Student implements Serializable {
    /**
     * 学生
     */

    private int id;
    private int number;
    private String name;
    private String teacher;
    private String password;
    private String email;
    private int teacherids;
    private byte[] photo;

    public Student(int number, String name, String teacher, String password, String email, byte[] photo) {
        this.number = number;
        this.name = name;
        this.teacher = teacher;
        this.password = password;
        this.email = email;
        this.photo = photo;
    }
    public Student(int number, String name, String teacher, String password, String email, byte[] photo,int teacherid) {
        this.number = number;
        this.name = name;
        this.teacher = teacher;
        this.password = password;
        this.email = email;
        this.photo = photo;
        this.teacherids = teacherid;
    }
}
