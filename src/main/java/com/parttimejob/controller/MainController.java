package com.parttimejob.controller;

import com.parttimejob.entity.Job;
import com.parttimejob.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.controller
 * @Author: Hinstein
 * @CreateTime: 2019-01-22 11:14
 * @Description:
 */

@Controller
public class MainController {

    @Autowired
    JobService jobService;

    /**
     * 兼职者登录页面
     * @return
     */
    @GetMapping("/workerLogin")
    public String workerLogin() {
        return "/homePage/workerLogin";
    }

    /**
     * 招聘者登录页面
     * @return
     */
    @GetMapping("/managerLogin")
    public String managerLogin() {
        return "/homePage/managerLogin";
    }

    /**
     * 兼职者注册页面
     * @return
     */
    @GetMapping("/workerRegister")
    public String workerRegister() {
        return "/homePage/workerRegister";
    }

    /**
     * 招聘者注册页面
     * @return
     */
    @GetMapping("/managerRegister")
    public String managerRegister() {
        return "/homePage/managerRegister";
    }

    /**
     * 招聘网主页
     * @return
     */
    @GetMapping("/index")
    public String index(Model model) {
        Page<Job> jobs=jobService.getJobs(1,27);
        Page<Job> jobs1=jobService.finDescByTime(1,27);
        Page<Job> jobs2=jobService.finDescByViews(1,27);
        model.addAttribute("jobs",jobs);
        model.addAttribute("jobs1",jobs1);
        model.addAttribute("jobs2",jobs2);
        return "/homePage/index";
    }

    /**
     * 后台登录页面
     * @return
     */
    @GetMapping("/admin/login")
    public String adminLogin() {
        return "/admin/login";
    }
}
