package com.neusoft.controller;

import com.neusoft.domain.Category;
import com.neusoft.domain.Topic;
import com.neusoft.domain.User;
import com.neusoft.mapper.CategoryMapper;
import com.neusoft.mapper.TopicMapper;
import com.neusoft.response.RegRespObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/12/9.
 */
@RequestMapping("jie")
@Controller
public class JieController {

    @Autowired
    CategoryMapper CategoryMapper;
    @Autowired
    TopicMapper topicMapper;
    @RequestMapping("add")
    public ModelAndView add(){
    List<Category> topicCategoryList = CategoryMapper.getAllCategories();
    ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("categories",topicCategoryList);
        modelAndView.setViewName("jie/add");
        return modelAndView;
    }
    @RequestMapping("doadd")
    @ResponseBody
    public RegRespObj doadd(Topic topic, HttpServletRequest request){
        RegRespObj regRespObj = new RegRespObj();
        User user =(User) request.getSession().getAttribute("userinfo");
        topic.setUserid(user.getId());
        topic.setCreateTime(new Date());
        int i = topicMapper.insertSelective(topic);
        if (i > 0){
            regRespObj.setStatus(0);
            regRespObj.setAction(request.getServletContext().getContextPath() + "/");
        }
        return regRespObj;
    }
    @RequestMapping("index")
    public String index(){
        return "jie/index";
    }
    @RequestMapping("detail")
    public String detail(){
        return "jie/detail";
    }
}
