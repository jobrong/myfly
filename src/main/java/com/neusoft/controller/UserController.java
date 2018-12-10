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
    //注册界面
    @RequestMapping("reg")
    public String reg(){return "user/reg";    }
    //设置页面
    @RequestMapping("set")
    public String set(){return "user/set";}
    //用户中心
    @RequestMapping("index")
    public String index(){return "user/index";}
    //我的主页
    @RequestMapping("home")
    public String home(){return "user/home";}
    //我的消息
    @RequestMapping("message")
    public String message(){return "user/message";}
    //注册
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

    //基本设置
    @RequestMapping("baseset")
    @ResponseBody
    public RegRespObj baseset(User user,HttpServletRequest request){
        User user1 =(User) request.getSession().getAttribute("userinfo");
        user.setId(user1.getId());
        user1.setEmail(user.getEmail());
        user1.setCity(user.getCity());
        user1.setNickname(user.getNickname());
        user1.setSex(user.getSex());
        user1.setSign(user.getSign());
        RegRespObj regRespObj = new RegRespObj();
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
    //更新密码
    @RequestMapping("repass")
    @ResponseBody
    public RegRespObj repass(User user,HttpServletRequest request){
        User user1 =(User) request.getSession().getAttribute("userinfo");
        user.setId(user1.getId());
        user.setPasswd(MD5Utils.getPwd(user.getPasswd()));
        RegRespObj regRespObj = new RegRespObj();
        user1.setPasswd( MD5Utils.getPwd(user.getPasswd()) );
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
    //上传图片
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
            //创建文件 放入本地
            File file2 = new File(realPath+File.separator+uuid+file.getOriginalFilename());
            file.transferTo(file2);
            //获得userinfo
            HttpSession session = request.getSession();
            User userinfo = (User)session.getAttribute("userinfo");
//            //删除原头像
//            if(userinfo.getPicPath() != null){
//                File file3 = new File(realPath+userinfo.getPicPath());
//                file3.delete();
//            }
            //修改当前对象picpath
            userinfo.setPicPath(uuid+file.getOriginalFilename());
            //修改当前userinfo对象
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

    @RequestMapping("/checkpass")
    @ResponseBody
    public RegRespObj checkpass(String nowpass,HttpServletRequest request){
        RegRespObj regRespObj = new RegRespObj();
        User user =(User) request.getSession().getAttribute("userinfo");
        System.out.println(nowpass);
        System.out.println(MD5Utils.getPwd(nowpass));
        System.out.println(userMapper.selectByPrimaryKey(user.getId()).getPasswd());
        if(MD5Utils.getPwd(nowpass).equals(userMapper.selectByPrimaryKey(user.getId()).getPasswd())){
            regRespObj.setMsg("密码正确");
        }else {
            regRespObj.setMsg("密码不存在");
        }
        return regRespObj;
    }
}
