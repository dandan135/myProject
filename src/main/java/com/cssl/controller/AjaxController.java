package com.cssl.controller;

import com.cssl.service.OptionsService;
import com.cssl.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AjaxController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private OptionsService optionsService;
    @RequestMapping("/verify")
    public String Verify(String userName) {
        System.out.println("注册验证用户名唯一");
        if (usersService.isReg(userName)>0){
            return "no";
        }
        return "yes";
    }
    @RequestMapping("/verifyTitle")
    public String verifyTitle(String title){
        if(optionsService.yzTitle(title)>0){
            System.out.println("验证title唯一：no");
            return "no";
        }
        System.out.println("验证title唯一：yes");
        return "yes";
    }
}
