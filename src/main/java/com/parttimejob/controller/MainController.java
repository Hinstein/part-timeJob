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

    @GetMapping("workerLogin")
    public String workerLogin() {
        return "homePage/workerLogin";
    }

    @GetMapping("managerLogin")
    public String managerLogin() {
        return "homePage/managerLogin";
    }

    @GetMapping("workerRegister")
    public String workerRegister() {
        return "homePage/workerRegister";
    }

    @GetMapping("managerRegister")
    public String managerRegister() {
        return "homePage/managerRegister";
    }

    @GetMapping("index")
    public String index() {
        return "homePage/index";
    }


    @GetMapping("admin/login")
    public String adminLogin() {
        return "admin/login";
    }
}
