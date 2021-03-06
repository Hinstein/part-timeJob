package com.parttimejob.controller;

import com.parttimejob.entity.*;
import com.parttimejob.service.*;
import net.sf.json.JSONArray;
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
 * @CreateTime: 2019-02-18 12:26
 * @Description:
 */

@Controller
public class JobController {

    @Autowired
    JobService jobService;

    @Autowired
    ManagerService managerService;

    @Autowired
    CollectService collectService;

    @Autowired
    DeliverService deliverService;

    @Autowired
    EmployService employService;

    /**
     * 发布工作
     *
     * @param job
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("/manager/publish/save")
    public String managerPublishSave(Job job, HttpSession session) {
        Manager manager = (Manager) session.getAttribute("manager");
        job.setManagerId(manager.getId());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        job.setDate(df.format(new Date()));
        jobService.jobSave(job);
        return "发布成功！";
    }


    @GetMapping("/manager/allJobs")
    public String findAllJob() {
        return "manager/job/allJob";
    }

    @ResponseBody
    @GetMapping("/manager/find/allJobs")
    public Map<String, Object> findAllJobs(HttpSession session, HttpServletRequest request) {
        Manager manager = (Manager) session.getAttribute("manager");
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        int pageNumber = Integer.parseInt(request.getParameter("page"));
        Map<String, Object> result = new HashMap<String, Object>();
        Page<Job> jobs = jobService.findByManagerId(manager.getId(), pageNumber, pageSize);
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", jobs.getTotalElements());
        JSONArray json = JSONArray.fromObject(jobs.getContent());
        result.put("data", json);
        return result;
    }

    /**
     * 发布工作
     *
     * @return
     */
    @GetMapping("/manager/publish")
    public String managerPublish() {
        return "/manager/job/publish";
    }


    /**
     * 编辑工作页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/manager/job/editor/{id}")
    public String managerJobEditor(@PathVariable("id") Integer id, Model model) {
        Job job = jobService.findById(id);
        model.addAttribute("job", job);
        return "/manager/job/editor";
    }

    /**
     * 保存工作
     *
     * @param job
     * @return
     */
    @ResponseBody
    @PostMapping("/manager/job/editor/save")
    public String jobSave(Job job) {
        jobService.updateEditor(job);
        return "保存成功";
    }

    /**
     * 删除工作
     *
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping("/manager/job/delete/{id}")
    public String jobDelete(@PathVariable("id") Integer id) {
        jobService.deleteById(id);
        return "删除成功";
    }

    /**
     * 查询工作
     *
     * @param request
     * @param content
     * @return
     */
    @ResponseBody
    @GetMapping("/worker/search/allJobs")
    public Map<String, Object> managerAudit(HttpServletRequest request,
                                            @RequestParam(value = "content", required = false) String content,
                                            @RequestParam(value = "type", required = false) String type,
                                            @RequestParam(value = "workerLimit", required = false) String workerLimit) {
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        int pageNumber = Integer.parseInt(request.getParameter("page"));
        Map<String, Object> result = new HashMap<String, Object>();
        if (content != null || type != null || workerLimit != null) {
            if (content != null && !content.equals("")) {
                Page<Job> jobs = jobService.findByTitleLike(content, pageNumber, pageSize);
                result.put("count", jobs.getTotalElements());
                JSONArray json = JSONArray.fromObject(jobs.getContent());
                result.put("data", json);
            } else if (type != null && !type.equals("")) {
                Page<Job> jobs = jobService.findByType(type, pageNumber, pageSize);
                result.put("count", jobs.getTotalElements());
                JSONArray json = JSONArray.fromObject(jobs.getContent());
                result.put("data", json);
            } else if (workerLimit != null && !workerLimit.equals("")) {
                Page<Job> jobs = jobService.findByWorkerLimit(workerLimit, pageNumber, pageSize);
                result.put("count", jobs.getTotalElements());
                JSONArray json = JSONArray.fromObject(jobs.getContent());
                result.put("data", json);
            } else {
                Page<Job> jobs = jobService.getJobs(pageNumber, pageSize);
                result.put("count", jobs.getTotalElements());
                JSONArray json = JSONArray.fromObject(jobs.getContent());
                result.put("data", json);
            }
        } else {
            Page<Job> jobs = jobService.getJobs(pageNumber, pageSize);
            result.put("count", jobs.getTotalElements());
            JSONArray json = JSONArray.fromObject(jobs.getContent());
            result.put("data", json);
        }
        result.put("code", 0);
        result.put("msg", "");
        result.put("limit",pageSize);
        result.put("page",pageNumber);
        return result;
    }

    /**
     * 查看工作信息
     *
     * @param jobId
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/worker/job/{id}")
    public String job(@PathVariable("id") int jobId, Model model, HttpSession session) {
        Worker worker = (Worker) session.getAttribute("worker");
        Job job = jobService.findById(jobId);
        job.setViews(job.getViews() + 1);
        jobService.jobSave(job);
        if (collectService.findByWorkerIdAndJobId(worker.getId(), jobId) != null) {
            job.setCheckCollection(1);
        }
        if (deliverService.findByWorkerIdAndJobId(worker.getId(), jobId) != null) {
            job.setCheckDeliver(1);
        }
        if (employService.findByWorkerIdAndJobId(worker.getId(), jobId) != null) {
            job.setCheckCollection(2);
            job.setCheckDeliver(2);
        }
        Page<Job> jobs = jobService.finDescByViews(1, 5);
        Manager manager = managerService.findById(job.getManagerId());
        model.addAttribute("manager", manager);
        model.addAttribute("job", job);
        model.addAttribute("jobs", jobs.getContent());
        return "/worker/job";
    }

    /**
     * 收藏工作
     *
     * @param map
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/worker/job/save")
    public String saveJob(@RequestParam HashMap<String, String> map, HttpSession session) {
        Collect workerAndJob = new Collect();
        int jobId = Integer.parseInt(map.get("id"));
        Worker worker = (Worker) session.getAttribute("worker");
        workerAndJob.setJobId(jobId);
        jobService.collectionSave(jobId);
        workerAndJob.setWorkerId(worker.getId());
        collectService.save(workerAndJob);
        return "收藏成功";
    }

    /**
     * 取消收藏
     *
     * @param
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/worker/job/cancelSave")
    public String cancelSaveJob(HttpServletRequest request, HttpSession session) {
        int jobId = Integer.valueOf(request.getParameter("id"));
        Worker worker = (Worker) session.getAttribute("worker");
        collectService.delete(worker.getId(), jobId);
        jobService.collectionCancel(jobId);
        return "取消收藏";
    }

    /**
     * 投递工作
     *
     * @param map
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/worker/job/deliver")
    public Map<String, String> deliver(@RequestParam HashMap<String, String> map, HttpSession session) {
        HashMap<String, String> map1 = new HashMap<>();
        int jobId = Integer.parseInt(map.get("id"));
        WorkerData worker = (WorkerData) session.getAttribute("workerData");
        Job job = jobService.findById(jobId);
        if (job.getWorkerLimit().equals(worker.getEducationBackground()) || job.getWorkerLimit().equals("无限制")) {
            Deliver deliver = new Deliver();
            deliver.setJobId(jobId);
            deliver.setWorkerId(worker.getWorkerId());
            deliverService.save(deliver);
            jobService.deliverSave(jobId);
            map.put("success", "投递成功");
        } else {
            map.put("error", "投递失败，不符合条件");
        }
        return map;
    }

    /**
     * 取消投递
     *
     * @param map
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/worker/job/cancelDeliver")
    public String cancelDeliver(@RequestParam HashMap<String, String> map, HttpSession session) {
        int jobId = Integer.parseInt(map.get("id"));
        Worker worker = (Worker) session.getAttribute("worker");
        deliverService.delete(worker.getId(), jobId);
        jobService.deliverCancel(jobId);
        return "取消投递";
    }
}
