package com.parttimejob.controller;

import com.parttimejob.entity.Job;
import com.parttimejob.entity.Manager;
import com.parttimejob.repository.JobRepository;
import com.parttimejob.service.JobService;
import com.parttimejob.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.controller
 * @Author: Hinstein
 * @CreateTime: 2019-02-18 12:26
 * @Description:
 */

@Controller
public class JobController {

    @Autowired
    JobService jobService;

    @Autowired
    ManagerService managerService;

    @ResponseBody
    @PostMapping("/manager/publish/save")
    public String managerPublishSave(Job job, HttpSession session) {
        System.out.println(job.getContent());
        int id = Integer.parseInt(session.getAttribute("id").toString());
        Manager manager = managerService.findById(id);
        job.setManager(manager);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        job.setDate(df.format(new Date()));
        manager.getJobs().add(job);
        managerService.save(manager);
        return "发布成功！";
    }

    @GetMapping("/manager/allJob")
    public String findAllJob(HttpSession session, Model model){
        int id = Integer.parseInt(session.getAttribute("id").toString());
        Manager manager = managerService.findById(id);
        List<Job> jobs =manager.getJobs();
        model.addAttribute("jobs",jobs);
        return "/manager/allJob";
    }
}
