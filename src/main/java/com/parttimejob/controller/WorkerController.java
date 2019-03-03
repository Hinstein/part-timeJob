package com.parttimejob.controller;

import com.parttimejob.entity.*;
import com.parttimejob.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.controller
 * @Author: Hinstein
 * @CreateTime: 2019-01-21 10:57
 * @Description:
 */

@Controller
public class WorkerController {

    @Autowired
    WorkerService workerService;

    @Autowired
    WorkerDataService workerDataService;

    @Autowired
    CollectService collectService;

    @Autowired
    JobService jobService;

    @Autowired
    DeliverService deliverService;

    @Autowired
    EmployService employService;

    /**
     * 兼职者注册
     * @param worker
     * @return
     */
    @ResponseBody
    @PostMapping("/worker/register")
    public String registerWorker(Worker worker) {
        Worker worker1 = workerService.findByUserName(worker.getUserName());
        if (worker1 != null) {
            return "注册失败，存在该用户";
        }
        workerService.save(worker);
        WorkerData workerData = new WorkerData();
        workerData.setWorkerId(worker.getId());
        workerDataService.save(workerData);
        return "注册成功";
    }

    /**
     * 兼职者登录
     * @param worker
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("/worker/login")
    public String workerLogin(Worker worker, HttpSession session) {
        String username = worker.getUserName();
        String password = worker.getPassword();
        Worker worker1 = workerService.findByUserName(username);
        if (worker1 != null) {
            if (worker1.getPassword().equals(password)) {
                session.setAttribute("worker", worker1);
                WorkerData workerData=workerDataService.findByWorkerId(worker1.getId());
                session.setAttribute("workerData",workerData);
                return "登录成功";
            }
            return "密码错误";
        }
        return "不存在该用户";
    }

    /**
     * 来到兼职者更换密码页面
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/worker/editor")
    public String workerEditor(HttpSession session, Model model) {

        return "worker/editor";
    }

    /**
     * 来到兼职者主页
     * @return
     */
    @GetMapping("/worker/index")
    public String workerIndex() {
        return "worker/index";
    }

    /**
     * 来到兼职者简历页面
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/worker/resume")
    public String workerResume(HttpSession session, Model model) {
        Worker worker = (Worker)session.getAttribute("worker");
        WorkerData workerData = workerDataService.findByWorkerId(worker.getId());
        model.addAttribute("worker", workerData);
        return "worker/resume";
    }

    /**
     * 异步保存兼职者简历
     * @param workerData
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("/worker/resume/save")
    public String workerDateSave(WorkerData workerData, HttpSession session) {
        Worker worker = (Worker)session.getAttribute("worker");
        workerData.setWorkerId(worker.getId());
        workerDataService.updata(workerData);
        return "保存成功";
    }

    /**
     * 异步更新用户密码
     * @param newPassword
     * @param oldPassword
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("/worker/editor/save")
    public String changePassword(@RequestParam("newPassword") String newPassword,
                                 @RequestParam("oldPassword") String oldPassword,
                                 HttpSession session) {
        Map<String, String> map = new HashMap<>(50);
        String username = (String) session.getAttribute("userName");
        Worker worker = workerService.findByUserName(username);
        if (worker.getPassword().equals(oldPassword)) {
            worker.setPassword(newPassword);
            workerService.save(worker);
            return "更新密码成功！";
        } else if (newPassword.equals("") || oldPassword.equals("")) {
            return "密码不能为空";
        } else {
            return "旧密码错误";
        }
    }

    /**
     * 来到兼职者查找工作页面
     * @return
     */
    @GetMapping("/worker/search")
    public String workerSearch() {
        return "worker/search";
    }

    /**
     * 来到兼职者搜藏页面
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/worker/collect")
    public String workerCollect(Model model, HttpSession session) {
        Worker worker = (Worker)session.getAttribute("worker");
        System.out.println(worker.getId());
        List<Collect> workerAndJobs = collectService.findByWorkerId(worker.getId());
        List<Job> jobs = new ArrayList<>();
        for (Collect w : workerAndJobs) {
            Job job = jobService.findById(w.getJobId());
            jobs.add(job);
        }
        model.addAttribute("jobs", jobs);
        return "worker/collect";
    }

    /**
     * 来到兼职者投递页面
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/worker/deliver")
    public String workerDeliver(Model model, HttpSession session) {
        Worker worker = (Worker)session.getAttribute("worker");
        List<Deliver> delivers = deliverService.findByWorkerId(worker.getId());
        List<Job> jobs = new ArrayList<>();
        for (Deliver w : delivers) {
            Job job = jobService.findById(w.getJobId());
            jobs.add(job);
        }
        model.addAttribute("jobs", jobs);
        return "worker/deliver";
    }

    @GetMapping("/worker/employ")
    public String workerEmploy(Model model, HttpSession session) {
        Worker worker = (Worker)session.getAttribute("worker");
        List<Employ> employs = employService.findByWorkerId(worker.getId());
        List<Job> jobs = new ArrayList<>();
        for (Employ w : employs) {
            Job job = jobService.findById(w.getJobId());
            jobs.add(job);
        }
        model.addAttribute("jobs", jobs);
        return "worker/employ";
    }

    @GetMapping("/worker/exit")
    public String workerExit( HttpSession session) {
      session.removeAttribute("worker");
        return "redirect:/index";
    }

}


