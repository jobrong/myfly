package com.neusoft.controller;

import com.neusoft.utils.MD5Utils;
import com.neusoft.domain.User;
import com.neusoft.mapper.UserMapper;
import com.neusoft.response.RegRespObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2018/12/6.
 */
@Controller
@RequestMapping("user")
public class UserController {


    @RequestMapping("reg")
    public String reg(){
        System.out.println(userMapper.selectByPrimaryKey(7369));
        return "user/reg";
    }

    @Autowired
    UserMapper userMapper;
    @RequestMapping("doreg")
    @ResponseBody
    public RegRespObj doReg(User user){
        RegRespObj regRespObj = new RegRespObj();
        user.setKissNum(100);
        user.setJoinTime(new Date());
        String passwd = user.getPasswd();
        String pwd = MD5Utils.getPwd(passwd);
        user.setPasswd(pwd);
        int i = userMapper.insertSelective(user);
        if(i>0){
            regRespObj.setStatus(0);
            regRespObj.setAction("/");
        }else {
            regRespObj.setStatus(1);
            regRespObj.setMsg("数据库错误，联系管理员");
        }
        return regRespObj;
    }
    @RequestMapping("set")
    public String set(){

        return "user/set";
    }
    @RequestMapping("baseset")
    @ResponseBody
    public RegRespObj baseset(User user,HttpServletRequest request){
        RegRespObj regRespObj = new RegRespObj();
        User user1 =(User) request.getSession().getAttribute("userinfo");
        user.setId(user1.getId());
        int i = userMapper.updateByPrimaryKeySelective(user);
        if(i>=0){
            regRespObj.setStatus(0);
            regRespObj.setMsg("更新成功");
            regRespObj.setAction(request.getServletContext().getContextPath()+"/user/set");
        }else {
            regRespObj.setMsg("更新失败");
            regRespObj.setStatus(1);
        }
        return regRespObj;
    }

    @RequestMapping("upload")
    @ResponseBody
    public RegRespObj upload(@RequestParam MultipartFile file,HttpServletRequest request) throws IOException {
        RegRespObj regRespObj = new RegRespObj();
        if(file.getSize()>0){
            String realPath = request.getServletContext().getRealPath("/res/uploadImgs");
            File file1 = new File(realPath);
            //建立文件夹
            if(!file1.exists()){
                file1.mkdirs();
            }
            //获取uuid
            UUID uuid = UUID.randomUUID();
            //创建文件
            File file2 = new File(realPath+File.separator+uuid+file.getOriginalFilename());
            file.transferTo(file2);
            //获得userinfo
            HttpSession session = request.getSession();
            User userinfo = (User)session.getAttribute("userinfo");
            //修改当前对象picpath
            userinfo.setPicPath(uuid+file.getOriginalFilename());
            //修改当前对象
            session.setAttribute("userinfo",userinfo);
            //更新数据库
            userMapper.updateByPrimaryKeySelective(userinfo);
            regRespObj.setStatus(0);
        }
        else
        {
            regRespObj.setStatus(1);
        }
        return regRespObj;
    }

    @RequestMapping("login")
    public String login(){

        return "user/login";
    }
    @RequestMapping("logout")
    public String logout(HttpServletRequest request)
    {
        request.getSession().invalidate();
        return "redirect:" + request.getServletContext().getContextPath() +"/";
    }
    @RequestMapping("dologin")
    @ResponseBody
    public RegRespObj dologin(User user,HttpServletRequest request){
        RegRespObj regRespObj = new RegRespObj();
        user.setPasswd(MD5Utils.getPwd(user.getPasswd()));
        User user1 = userMapper.selectByEmailAndPass(user);
        if(user1==null){
            regRespObj.setStatus(1);
            regRespObj.setMsg("登录失败");
        }else {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("userinfo",user1);
            regRespObj.setStatus(0);
            regRespObj.setAction(request.getServletContext().getContextPath() + "/");
        }
        return regRespObj;
    }

    @RequestMapping("/checkEmail")
    @ResponseBody
    public RegRespObj checkEmail(User user){
        RegRespObj regRespObj = new RegRespObj();
        User user1 = userMapper.selectByEmail(user.getEmail());
        if(user1==null){
            regRespObj.setMsg("可以注册");
        }else {
            regRespObj.setMsg("邮箱重复，请更换邮箱");
        }
        return regRespObj;
    }
}