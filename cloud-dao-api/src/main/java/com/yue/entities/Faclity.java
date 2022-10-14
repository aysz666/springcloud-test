package com.yue.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Faclity implements Serializable {
    /*
    *设备
     */
    private int id;
    private String name;
    private String teacher;
    private String location;
    private int price;
    private byte[] photo1;
    private byte[] photo2;
    private String borrower;
    private Boolean usable;
    private int teacherids;

}
