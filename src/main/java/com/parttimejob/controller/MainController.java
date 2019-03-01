package com.parttimejob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.controller
 * @Author: Hinstein
 * @CreateTime: 2019-01-22 11:14
 * @Description:
 */

@Controller
public class MainController {

    /**
     * 兼职者登录页面
     * @return
     */
    @GetMapping("workerLogin")
    public String workerLogin() {
        return "homePage/workerLogin";
    }

    /**
     * 招聘者登录页面
     * @return
     */
    @GetMapping("managerLogin")
    public String managerLogin() {
        return "homePage/managerLogin";
    }

    /**
     * 兼职者注册页面
     * @return
     */
    @GetMapping("workerRegister")
    public String workerRegister() {
        return "homePage/workerRegister";
    }

    /**
     * 招聘者注册页面
     * @return
     */
    @GetMapping("managerRegister")
    public String managerRegister() {
        return "homePage/managerRegister";
    }

    /**
     * 招聘网主页
     * @return
     */
    @GetMapping("index")
    public String index() {
        return "homePage/index";
    }

    /**
     * 后台登录页面
     * @return
     */
    @GetMapping("admin/login")
    public String adminLogin() {
        return "admin/login";
    }
}
