package com.yue.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin implements Serializable {
    /*
    管理员

    */
//    账号
    private String number;
//    密码
    private String password;
//    邮箱
    private String email;
//    照片
    private byte[] photo;
    public Admin(String a,String b,byte[] c){
        this.email = b;
        this.number = a;
        this.photo = c;
    }

    public Admin(String number, String email) {
        this.number = number;
        this.email = email;
    }

}
