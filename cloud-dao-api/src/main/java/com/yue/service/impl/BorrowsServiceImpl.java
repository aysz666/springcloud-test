package com.yue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yue.dao.BorrowsDao;
import com.yue.dao.FaclityDao;
import com.yue.entities.Borrows;
import com.yue.entities.Faclity;
import com.yue.service.BorrowsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Service
public class BorrowsServiceImpl implements BorrowsService {
    @Autowired
    private BorrowsDao borrowsdao;
    @Autowired
    private FaclityDao faclitydao;

    //老师设备借用
    @Override
    public Boolean teacherborrow(String name, Timestamp time1, Timestamp time2, String reason, String faclityname, String owner, int id) {

//        创造对象
        Borrows borrows = new Borrows();
        borrows.setTeaapplicant(name);
        borrows.setAptime(time1);
        borrows.setOutime(time2);
        borrows.setApplicantreason(reason);
        borrows.setFaclityname(faclityname);
        borrows.setAdmin("admin");
        borrows.setOwner(owner);
        borrows.setTeacherid(id);

        QueryWrapper<Borrows> wrapper = new QueryWrapper<>();

//        通过设备名称获取设备的id
        QueryWrapper<Faclity> fwrapper = new QueryWrapper<>();
        fwrapper.eq("name",faclityname);
        Faclity faclity = faclitydao.selectOne(fwrapper);
        borrows.setFaclityid(faclity.getId());

//        判断是否被借用
        if (faclity.getUsable()){
            borrowsdao.insert(borrows);
            return true;
        }
        else{
//            判断该时间段是否有人借用
            wrapper.isNull("result")
                    .and(wp -> wp
                            .lt("mintime",time2)
                            .gt("mintime",time1)
                            .or(wp1->wp1
                                    .gt("maxtime",time1)
                                    .lt("maxtime",time2))
                            .or(wp2->wp2
                                    .gt("maxtime",time2)
                                    .lt("mintime",time1)));
            List<Borrows> borrows1 = borrowsdao.selectList(wrapper);
            if (borrows1.size() == 0){
                borrowsdao.insert(borrows);
                return true;
            }
            return false;
        }
    }

//    学生借用
    @Override
    public Boolean studentborrow(String name, Timestamp time1, Timestamp time2, String reason, String faclityname, String teacher,String owner,int id) {

        Borrows borrows = new Borrows();
        borrows.setStuapplicant(name);
        borrows.setAptime(time1);
        borrows.setOutime(time2);
        borrows.setApplicantreason(reason);
        borrows.setFaclityname(faclityname);
        borrows.setApplicantteacher(teacher);
        borrows.setOwner(owner);
        borrows.setAdmin("admin");
        borrows.setStudentid(id);

        QueryWrapper<Borrows> wrapper = new QueryWrapper<>();

//        获取设备id
        QueryWrapper<Faclity> fwrapper = new QueryWrapper<>();
        fwrapper.eq("name",faclityname);
        Faclity faclity = faclitydao.selectOne(fwrapper);
        borrows.setFaclityid(faclity.getId());

        if (faclity.getUsable()){
            borrowsdao.insert(borrows);
            return  true;
        }
        else{
            wrapper.isNotNull("result")
                    .and(wp -> wp
                            .lt("aptime",time2).gt("outime",time1)
                            .or(wp1->wp1
                                    .gt("outime",time1)
                                    .lt("outime",time2))
                            .or(wp2->wp2
                                    .gt("outime",time2)
                                    .lt("aptime",time1)));
            List<Borrows> borrows1 = borrowsdao.selectList(wrapper);
            if (borrows1.size() == 0){
                borrowsdao.insert(borrows);
                return true;
            }
            else {
                return false;
            }
        }
    }

    @Override
    public Boolean teacherresult(String advice, String refuser, String refusereason, String name, String faclityname, String teacher, int id) {
//        老师审核
        UpdateWrapper<Borrows> wrapper = new UpdateWrapper<>();
//        拥有者审核
        UpdateWrapper<Borrows> wrappers = new UpdateWrapper<>();

//        其余人拒绝
        UpdateWrapper<Borrows> wrapper_else = new UpdateWrapper<>();
        UpdateWrapper<Borrows> wrapper_if = new UpdateWrapper<>();
        try {
//            老师审核
            wrapper.eq("faclityname",faclityname).eq("stuapplicant",name).eq("applicantteacher",teacher).eq("id",id);
//            拥有者审核
            wrappers.eq("faclityname",faclityname).eq("owner",teacher).eq("id",id);
            if (Objects.equals(advice, "true")){
                wrapper.set("teacheresualt",1);
                wrappers.set("owneresualt",1);
            }else {
                wrapper.set("refuser",refuser)
                        .set("teacheresualt",0)
                        .set("refusereason",refusereason)
                        .set("result",0);
                wrappers.set("refuser",refuser)
                        .set("owneresualt",0)
                        .set("refusereason",refusereason)
                        .set("result",0);
            }
            borrowsdao.update(null,wrapper);
            borrowsdao.update(null,wrappers);

//            多人同时借用,拒绝其他的借用申请
            if (Objects.equals(advice, "true")){
                QueryWrapper<Borrows> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("faclityname",faclityname).eq("id",id);
                Borrows borrow =  borrowsdao.selectOne(queryWrapper);
                Timestamp time1 = borrow.getAptime();
                Timestamp time2 = borrow.getOutime();
                wrapper_else
                        .eq("faclityname",faclityname).ne("id",id)
                        .eq("owner",teacher)
                        .isNull("owneresualt")
                        .isNull("result")
                        .and(wq->wq
                                .isNull("applicantteacher")
                                .or(sq->sq
                                        .isNotNull("applicantteacher")
                                        .eq("teacheresualt",1)))
                        .and(wp -> wp
                                .lt("aptime",time2).gt("aptime",time1)
                                .or(wp1->wp1
                                        .gt("outime",time1)
                                        .lt("outime",time2))
                                .or(wp2->wp2
                                        .ge("outime",time2)
                                        .le("aptime",time1)));
                wrapper_if
                        .eq("faclityname",faclityname)
                        .ne("id",id)
                        .eq("applicantteacher",teacher)
                        .isNull("teacheresualt")
                        .and(wp -> wp
                                .lt("aptime",time2)
                                .gt("aptime",time1)
                                .or(wp1->wp1
                                        .gt("outime",time1)
                                        .lt("outime",time2))
                                .or(wp2->wp2
                                        .ge("outime",time2)
                                        .le("aptime",time1)));
                wrapper_else
                        .set("owneresualt",0)
                        .set("refuser",teacher)
                        .set("refusereason","设备已被借用！")
                        .set("result",0);
                wrapper_if
                        .set("teacheresualt",0)
                        .set("refuser",teacher)
                        .set("refusereason","设备已被借用！")
                        .set("result",0);
                borrowsdao.update(null,wrapper_if);
                borrowsdao.update(null,wrapper_else);
            }

        }
        catch (Exception exception){
            return false;
        }
       return true;
    }
//管理员审核
    @Override
    public Boolean adminresult(int id, String advice, String refuser, String refusereason, String name, String faclityname) {
//        审核借用记录
        UpdateWrapper<Borrows> wrapper = new UpdateWrapper<>();
//        修改设备可用状态
        UpdateWrapper<Faclity> fwrapper = new UpdateWrapper<>();
        wrapper.eq("faclityname",faclityname)
                .eq("id",id)
                .and(aq->aq
                        .eq("stuapplicant",name)
                        .or()
                        .eq("teaapplicant",name));
        fwrapper.eq("name",faclityname);

        try{
            if (Objects.equals(advice, "true")){
                wrapper.set("adminresualt",true);
                wrapper.set("result",true);
                fwrapper.set("usable",false);
                fwrapper.set("borrower",name);
                faclitydao.update(null,fwrapper);
            }else {
                wrapper.set("refuser",refuser);
                wrapper.set("refusereason",refusereason);
                wrapper.set("result",false);
            }
            borrowsdao.update(null,wrapper);
        }catch (Exception e){
            return false;

        }
        return true;
    }
//审核记录
    @Override
    public List<Borrows> checkrecord(String name) {
        LambdaQueryWrapper<Borrows> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(Borrows::getOwner ,name)
                .isNotNull(Borrows::getOwneresualt)
                .or(wq->wq
                        .eq(Borrows::getApplicantteacher,name)
                        .isNotNull(Borrows::getTeacheresualt))
                .or(wq->wq
                        .isNull(Borrows::getApplicantteacher)
                        .eq(Borrows::getOwner,name)
                        .isNotNull(Borrows::getOwneresualt));
        wrapper.select(Borrows::getStuapplicant,Borrows::getTeaapplicant,Borrows::getFaclityname,Borrows::getApplicantreason,Borrows::getAptime,Borrows::getOutime,Borrows::getTeacheresualt,Borrows::getOwneresualt,Borrows::getAdminresualt,Borrows::getRefuser,Borrows::getRefusereason,Borrows::getResult);
        return borrowsdao.selectList(wrapper);
    }
//借用记录
    @Override
    public List<Borrows> borrorrecord(String name) {
        LambdaQueryWrapper<Borrows> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Borrows::getTeaapplicant ,name).or().eq(Borrows::getStuapplicant,name);
        wrapper.select(Borrows::getFaclityname,Borrows::getApplicantreason,Borrows::getAptime,Borrows::getOutime,Borrows::getTeacheresualt,Borrows::getOwneresualt,Borrows::getAdminresualt,Borrows::getRefuser,Borrows::getRefusereason,Borrows::getResult);
        return borrowsdao.selectList(wrapper);
    }

//    待审核记录
    @Override
    public List<Borrows> checking(String name) {
        LambdaQueryWrapper<Borrows> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Borrows::getOwner ,name)
                .isNull(Borrows::getOwneresualt)
                .isNull(Borrows::getResult)
                .isNotNull(Borrows::getTeacheresualt)
                .or(wq->wq.isNull(Borrows::getApplicantteacher)
                        .eq(Borrows::getOwner,name)
                        .isNull(Borrows::getOwneresualt)
                        .isNull(Borrows::getTeacheresualt)
                        .isNull(Borrows::getResult))
               .or((aq)-> aq
                       .eq(Borrows::getApplicantteacher ,name)
                       .isNull(Borrows::getTeacheresualt)
                       .isNull(Borrows::getResult));
        wrapper.select(Borrows::getId,Borrows::getStuapplicant,Borrows::getTeaapplicant,Borrows::getFaclityname,Borrows::getApplicantreason,Borrows::getAptime,Borrows::getOutime,Borrows::getTeacheresualt,Borrows::getOwneresualt,Borrows::getAdminresualt,Borrows::getRefuser,Borrows::getRefusereason,Borrows::getResult);
        return borrowsdao.selectList(wrapper);
    }

//    管理员待审核
    @Override
    public List<Borrows> adminchecking() {
        LambdaQueryWrapper<Borrows> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(Borrows::getOwneresualt,true)
                .isNull(Borrows::getResult)
                .and(wq->wq.eq(Borrows::getTeacheresualt,true)
                        .or(aq->aq
                                .isNull(Borrows::getApplicantteacher)
                                .isNull(Borrows::getTeacheresualt)
                        ));
        wrapper.select(Borrows::getId,Borrows::getStuapplicant,Borrows::getTeaapplicant,Borrows::getFaclityname,Borrows::getApplicantreason,Borrows::getAptime,Borrows::getOutime,Borrows::getTeacheresualt,Borrows::getOwneresualt,Borrows::getAdminresualt,Borrows::getRefuser,Borrows::getRefusereason,Borrows::getResult);
        return borrowsdao.selectList(wrapper);
    }

//    管理员审核记录
    @Override
    public List<Borrows> admincheckrecord() {
        LambdaQueryWrapper<Borrows> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Borrows::getOwneresualt,true).eq(Borrows::getTeacheresualt,true).isNotNull(Borrows::getResult);
        wrapper.select(Borrows::getStuapplicant,Borrows::getTeaapplicant,Borrows::getApplicantreason,Borrows::getAptime,Borrows::getOutime,Borrows::getTeacheresualt,Borrows::getOwneresualt,Borrows::getAdminresualt,Borrows::getRefuser,Borrows::getRefusereason,Borrows::getResult,Borrows::getFaclityname);
        return borrowsdao.selectList(wrapper);
    }

}
