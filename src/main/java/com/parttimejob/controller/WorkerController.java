package com.parttimejob.controller;

import com.parttimejob.entity.*;
import com.parttimejob.service.*;
import net.sf.json.JSONArray;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

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
    ManagerService  managerService;

    @Autowired
    CollectService collectService;

    @Autowired
    JobService jobService;

    @Autowired
    DeliverService deliverService;

    @Autowired
    EmployService employService;

    @Autowired
    BBSService bbsService;

    @Autowired
    EvaluationToWorkerService evaluationToWorkerService;

    /**
     * 兼职者注册
     *
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
     *
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
                session.setAttribute("username", worker1.getUserName());
                WorkerData workerData = workerDataService.findByWorkerId(worker1.getId());
                session.setAttribute("workerData", workerData);
                List<Collect> collectList = collectService.findByWorkerId(worker1.getId());
                List<Deliver> delivers = deliverService.findByWorkerId(worker1.getId());
                List<BBS> bbs=bbsService.findByWorkerId(worker1.getId());
                List<EvaluationToWorker> evaluations = evaluationToWorkerService.findByWorkerId(worker1.getId());
                session.setAttribute("collectList", collectList.size());
                session.setAttribute("delivers", delivers.size());
                session.setAttribute("bbs", bbs.size());
                session.setAttribute("evaluations", evaluations.size());
                return "登录成功";
            }
            return "密码错误";
        }
        return "不存在该用户";
    }

    @RequestMapping("/login")
    public String login(String name,String password) {

        //1、获取subject
        Subject subject = SecurityUtils.getSubject();

        //2、封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(name,password);

        //3、执行登录方法
        try {
            //交给Realm处理--->执行它的认证方法
            subject.login(token);
            //登录成功
            return "redirect:/testThymeleaf";
        }catch (UnknownAccountException e){
            //登录失败:用户名不存在
            return "用户名不存在";
        }catch (IncorrectCredentialsException e){
            //登录失败：密码错误
            return "密码错误";
        }
    }


    /**
     * 来到兼职者更换密码页面
     *
     * @return
     */
    @GetMapping("/worker/editor")
    public String workerEditor() {

        return "/worker/editor";
    }

    /**
     * 来到兼职者主页
     *
     * @return
     */
    @GetMapping("/worker/index")
    public String workerIndex(HttpSession session, Model model) {
        WorkerData workerData = (WorkerData) session.getAttribute("workerData");
        if (workerData.getName() == null) {
            return "redirect:/worker/resume";
        } else {
            return "worker/index";
        }
    }

    /**
     * 来到兼职者简历页面
     *
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/worker/resume")
    public String workerResume(HttpSession session, Model model) {
        Worker worker = (Worker) session.getAttribute("worker");
        WorkerData workerData = workerDataService.findByWorkerId(worker.getId());
        model.addAttribute("worker", workerData);
        return "worker/resume";
    }

    /**
     * 异步保存兼职者简历
     *
     * @param workerData
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("/worker/resume/save")
    public String workerDateSave(WorkerData workerData, HttpSession session) {
        Worker worker = (Worker) session.getAttribute("worker");
        workerData.setWorkerId(worker.getId());
        workerDataService.updata(workerData);
        return "保存成功";
    }

    /**
     * 异步更新用户密码
     *
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
     *
     * @return
     */
    @GetMapping("/worker/search")
    public String workerSearch() {
        return "worker/search";
    }

    /**
     * 来到兼职者搜藏页面
     *
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/worker/collect")
    public String workerCollect(Model model, HttpSession session) {
        Worker worker = (Worker) session.getAttribute("worker");
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
     *
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/worker/deliver")
    public String workerDeliver(Model model, HttpSession session) {
        Worker worker = (Worker) session.getAttribute("worker");
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
        Worker worker = (Worker) session.getAttribute("worker");
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
    public String workerExit(HttpSession session) {
        session.removeAttribute("worker");
        session.removeAttribute("username");
        session.removeAttribute("workerData");
        return "redirect:/index";
    }



    @GetMapping("/worker/BBS/index")
    public String managerBBSIndex() {
        return "/worker/bbs/index";
    }

    @ResponseBody
    @GetMapping("/worker/BBS/delete/{id}")
    public String deleteBBS(@PathVariable("id")int id){
        bbsService.deleteById(id);
        return "删除成功！";
    }

    @GetMapping("/worker/BBS/publish")
    public String workerBBSPublish() {
        return "/worker/bbs/publish";
    }

    @GetMapping("/worker/BBS/editor")
    public String workerBBSEditor(){
        return "/worker/BBS/post";
    }

    @GetMapping("/worker/BBS/editor/{id}")
    public String myBBSEditor(@PathVariable("id")int id,Model model){
        BBS bbs=bbsService.findById(id);
        model.addAttribute("bbs",bbs);
        return "/worker/BBS/editor";
    }

    @ResponseBody
    @PostMapping("/worker/BBS/editor/save")
    public String manageBBSEditorSave(BBS bbs){
        bbsService.editorSave(bbs);
        return "修改成功！";
    }

    @GetMapping("/worker/BBS/search")
    public String managerBBSSearch(){
        return "worker/BBS/search";
    }

    @ResponseBody
    @PostMapping("/worker/BBS/publish/save")
    public String manageBBSPublishSave(BBS bbs,HttpSession session){
        Worker worker=(Worker) session.getAttribute("worker");
        bbs.setStatus(1);
        bbs.setWorkerId(worker.getId());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bbs.setDate(df.format(new Date()));
        bbsService.BBSSave(bbs);
        return "发布成功！";
    }

    @ResponseBody
    @GetMapping("/worker/BBS/article")
    public  Map<String, Object> managerBBSArticle(HttpSession session, HttpServletRequest request){
        Worker worker=(Worker)session.getAttribute("worker");
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        int pageNumber = Integer.parseInt(request.getParameter("page"));
        Page<BBS> bbs = bbsService.findByWorkerId(worker.getId(),pageNumber, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", bbs.getTotalElements());
        JSONArray json = JSONArray.fromObject(bbs.getContent());
        result.put("data", json);
        return result;
    }

    @ResponseBody
    @GetMapping("/worker/BBS/findAll")
    public  Map<String, Object> managerBBSArticleFindAll(HttpSession session,HttpServletRequest request){
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        int pageNumber = Integer.parseInt(request.getParameter("page"));
        Page<BBS> bbs = bbsService.findAll(pageNumber, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", bbs.getTotalElements());
        JSONArray json = JSONArray.fromObject(bbs.getContent());
        result.put("data", json);
        return result;
    }

    @GetMapping("/worker/BBS/look/{id}")
    public String look(@PathVariable("id")int id,Model model){
        BBS bbs=bbsService.findById(id);
        if(bbs.getStatus()==1)
        {
            model.addAttribute("status","招聘者");
            WorkerData workerData = workerDataService.findByWorkerId(bbs.getWorkerId());
            model.addAttribute("username",workerData.getName());
        }
        else if(bbs.getStatus()==2){
            model.addAttribute("status","兼职者");

            Manager manager=managerService.findById(bbs.getManagerId());
            model.addAttribute("username",manager.getName());
        }
        bbsService.views(id);
        model.addAttribute("bbs",bbs);
        return "/worker/BBS/look";
    }

}


