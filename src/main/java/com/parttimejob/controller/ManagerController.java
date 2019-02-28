package com.parttimejob.controller;

import com.parttimejob.entity.*;
import com.parttimejob.service.*;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @CreateTime: 2019-01-21 19:05
 * @Description:
 */


@Controller
public class ManagerController {

    @Autowired
    ManagerService managerService;

    @Autowired
    DeliverService deliverService;

    @Autowired
    WorkerService workerService;

    @Autowired
    WorkerDataService workerDataService;

    @Autowired
    EmployService employService;

    @Autowired
    JobService jobService;

    @ResponseBody
    @PostMapping("/manager/register")
    public String registerWorker(Manager manager) {
        Manager manager1 = managerService.findByUserName(manager.getUserName());
        if (manager1 != null) {
            return "注册失败，存在该用户";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        manager.setDate(df.format(new Date()));
        managerService.save(manager);

        return "注册成功,等待管理员审核";
    }

    @ResponseBody
    @PostMapping("/manager/login")
    public String managerLogin(Manager manager, HttpSession session) {
        String username = manager.getUserName();
        String password = manager.getPassword();
        Manager manager1 = managerService.findByUserName(username);
        if (manager1 != null) {
            if (manager1.getPassword().equals(password)) {
                if (manager1.getAudit() == 0) {
                    return "该账号正在审核中，请等待的管理员审核";
                }
                session.setAttribute("managerId", manager1.getId());
                session.setAttribute("userName", manager1.getUserName());
                return "登录成功";
            }
            return "密码错误";
        }

        return "不存在该用户";
    }

    @GetMapping("/manager/index")
    public String managerIndex() {
        return "manager/index";
    }

    @GetMapping("/manager/search")
    public String managerSearch() {
        return "manager/search";
    }

    @GetMapping("/manager/job/deliver/{id}")
    public String jobDeliver(@PathVariable("id") int jobId, Model model, HttpSession session) {
        Job job = new Job();
        job.setId(jobId);
        session.setAttribute("jobId", jobId);
        model.addAttribute("job", job);
        return "manager/job/deliver";
    }

    @ResponseBody
    @GetMapping("/manager/job/deliver/worker/{id}")
    public Map<String, Object> jobDeliver(@PathVariable("id") int jobId, HttpServletRequest request) {
        List<Deliver> delivers = deliverService.findByJobId(jobId);
        List<WorkerData> workers = new ArrayList<>();
        for (Deliver w : delivers) {
            WorkerData worker = workerDataService.findByWorkerId(w.getWorkerId());
            workers.add(worker);
        }
        int page = Integer.parseInt(request.getParameter("limit"));
        int rows = Integer.parseInt(request.getParameter("page"));
        int size = workers.size();
        //截取的开始位置
        int pageStart = (page == 1 ? 0 : (page - 1) * rows);
        //截取的结束位置
        int pageEnd = (size < page * rows ? size : page * rows);
        Map<String, Object> result = new HashMap<String, Object>();
        if (size > pageStart) {
            List<WorkerData> workers1 = workers.subList(pageStart, pageEnd);
            JSONArray json = JSONArray.fromObject(workers1);
            result.put("data", json);
        } else {
            JSONArray json = JSONArray.fromObject(workers);
            result.put("data", json);
        }
        result.put("count", size);
        result.put("code", 0);
        result.put("msg", "");
        return result;

    }

    @GetMapping("/manager/deliver/worker/{id}")
    public String workerInformation(@PathVariable("id") int id, Model model, HttpSession session) {
        WorkerData workerData = workerDataService.findByWorkerId(id);
        model.addAttribute("worker", workerData);
        session.setAttribute("workerId", id);
        return "manager/worker";
    }

    @ResponseBody
    @PostMapping("/employ")
    public String employWorker(HttpSession session, @RequestParam HashMap<String, String> map) {
        int jobId = Integer.parseInt(session.getAttribute("jobId").toString());
        int managerId = Integer.parseInt(session.getAttribute("managerId").toString());
        int workerId = Integer.parseInt(session.getAttribute("workerId").toString());
        Employ employ = employService.findByWorkerIdAndJobIdAndManagerId(workerId, jobId, managerId);
        String date = map.get("date");
        String time = map.get("time");
        String dateTime = date + " " + time;
        if (employ == null) {
            Employ employ1 = new Employ();
            employ1.setWorkerId(workerId);
            employ1.setJobId(jobId);
            employ1.setDate(dateTime);
            employ1.setManagerId(managerId);
            employService.save(employ1);
            deliverService.delete(workerId, jobId);
            return "录用成功";
        } else {
            return "已经录用";
        }
    }

    @GetMapping("/manager/employ")
    public String managerEmployee() {
        return "manager/employ";
    }


    @ResponseBody
    @GetMapping("/manager/employ/list")
    public Map<String, Object> employee(HttpSession session, HttpServletRequest request) {
        int managerId = Integer.parseInt(session.getAttribute("managerId").toString());
        List<Employ> employs = employService.findByManagerId(managerId);
        List<WorkerData> workers = new ArrayList<>();
        for (Employ e : employs) {
            WorkerData workerData = workerDataService.findByWorkerId(e.getWorkerId());
            workers.add(workerData);
        }
        int page = Integer.parseInt(request.getParameter("limit"));
        int rows = Integer.parseInt(request.getParameter("page"));
        int size = workers.size();
        //截取的开始位置
        int pageStart = (page == 1 ? 0 : (page - 1) * rows);
        //截取的结束位置
        int pageEnd = (size < page * rows ? size : page * rows);
        Map<String, Object> result = new HashMap<String, Object>();
        if (size > pageStart) {
            List<WorkerData> workers1 = workers.subList(pageStart, pageEnd);
            JSONArray json = JSONArray.fromObject(workers1);
            result.put("data", json);
        } else {
            JSONArray json = JSONArray.fromObject(workers);
            result.put("data", json);
        }
        result.put("count", size);
        result.put("code", 0);
        result.put("msg", "");
        return result;
    }

    @GetMapping("/manager/workerDeliver/{id}")
    public String workerDeliver(@PathVariable("id") int id, Model model, HttpSession session) {
        WorkerData workerData = workerDataService.findByWorkerId(id);
        model.addAttribute("worker", workerData);
        return "manager/workerDeliver";
    }

    @GetMapping("/manager/deliver/worker/setTime")
    public String cancelEmployee(HttpSession session, Model model) {
        int workerId = Integer.parseInt(session.getAttribute("workerId").toString());
        WorkerData workerData = workerDataService.findByWorkerId(workerId);
        model.addAttribute("worker", workerData);
        return "manager/setTime";
    }

    @GetMapping("/manager/job/{id}")
    public String jobId(@PathVariable("id") int id, Model model) {
        Job job = jobService.findById(id);
        model.addAttribute("job", job);
        return "manager/job";
    }

    @GetMapping("/manager/editor")
    public String managerEditor(){
        return "manager/editor";
    }

    @ResponseBody
    @PostMapping("/manager/editor/save")
    public String changePassword(@RequestParam("newPassword") String newPassword,
                                 @RequestParam("oldPassword") String oldPassword,
                                 HttpSession session) {
        int managerId = Integer.parseInt(session.getAttribute("managerId").toString());
        Manager manager = managerService.findById(managerId);
        if (manager.getPassword().equals(oldPassword)) {
            manager.setPassword(newPassword);
            managerService.saveEditor(manager);
            return "更新密码成功！";
        } else {
            return "旧密码错误";
        }
    }
}
