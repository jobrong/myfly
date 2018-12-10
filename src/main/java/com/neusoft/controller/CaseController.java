package com.neusoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/12/9.
 */
@RequestMapping("case")
@Controller
public class CaseController {
    @RequestMapping("case")
    public String index(){
        return "case/case";
    }
}
