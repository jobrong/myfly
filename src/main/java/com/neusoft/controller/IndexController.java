package com.neusoft.controller;

import com.neusoft.mapper.TopicMapper;
import com.neusoft.utils.StringDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/6.
 */
@Controller
public class IndexController {

    @Autowired
    TopicMapper topicMapper;

    @RequestMapping("/")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();
        final List<Map<String, Object>> context = topicMapper.getAllTopics();
        for(Map<String,Object> map : context)
        {
            Date date = (Date)map.get("create_time");
            String strDate = StringDate.getStringDate(date);
            map.put("create_time",strDate);
        }
        modelAndView.setViewName("index");
        modelAndView.addObject("topics",context);
        return modelAndView;
    }
}
