package com.neusoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/12/5.
 */
@Controller
public class reg {

    @RequestMapping("reg")
    public String login(){

        return "user/reg";
    }
}
