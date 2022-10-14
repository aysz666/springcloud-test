package com.yue.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Teacher implements Serializable {
    /*
    老师
    * */
    private int id;
    private int number;
    private String name;
    private String email;
    private String password;
    private byte[] photo;

    public Teacher(int number, String name, String email, String password, byte[] photo) {
        this.number = number;
        this.name = name;
        this.email = email;
        this.password = password;
        this.photo = photo;
    }
}
