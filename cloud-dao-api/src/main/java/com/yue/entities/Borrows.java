package com.yue.entities;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Borrows implements Serializable {
    /**
     * 借用记录
     */


    private int id;                 //借用记录id
    private String stuapplicant;            //申请人
    private String teaapplicant;            //申请人
    private String applicantteacher;            //申请人老师
    private String owner;            //设备拥有者
    private String admin;            //管理员
    private Timestamp aptime;            //开始时间
    private Timestamp outime;            //结束时间
    private String applicantreason;            //申请原因
    private String refuser;            //拒绝人
    private String refusereason;            //拒绝原因
    private String faclityname;            //设备名字
    private Boolean teacheresualt;            //老师审核结果
    private Boolean owneresualt;            //拥有人审核结果
    private Boolean adminresualt;            //管理员审核结果
    private Boolean result;            //借用结果
    private int studentid;
    private int teacherid;
    private int faclityid;
}
