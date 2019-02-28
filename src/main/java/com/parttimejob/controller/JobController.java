package com.parttimejob.controller;

import com.parttimejob.entity.Deliver;
import com.parttimejob.entity.Job;
import com.parttimejob.entity.Collect;
import com.parttimejob.service.DeliverService;
import com.parttimejob.service.JobService;
import com.parttimejob.service.ManagerService;
import com.parttimejob.service.CollectService;
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

    @ResponseBody
    @PostMapping("/manager/publish/save")
    public String managerPublishSave(Job job, HttpSession session) {
        int managerId = Integer.parseInt(session.getAttribute("managerId").toString());
        job.setManagerId(managerId);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        job.setDate(df.format(new Date()));
        jobService.jobSave(job);
        return "发布成功！";
    }

    @GetMapping("/manager/allJob")
    public String findAllJob(HttpSession session, Model model) {
        int managerId = Integer.parseInt(session.getAttribute("managerId").toString());
        List<Job> jobs = jobService.findByManagerId(managerId);
        model.addAttribute("jobs", jobs);
        return "manager/job/allJob";
    }

    @GetMapping("/manager/publish")
    public String managerPublish() {
        return "/manager/job/publish";
    }

    @GetMapping("/manager/job/editor/{id}")
    public String managerJobEditor(@PathVariable("id") Integer id, Model model) {
        Job job = jobService.findById(id);
        model.addAttribute("job", job);
        return "manager/job/editor";
    }

    @ResponseBody
    @PostMapping("/manager/job/editor/save")
    public String jobSave(Job job) {
        jobService.updateEditor(job);
        return "保存成功";
    }

    @ResponseBody
    @PostMapping("/manager/job/delete/{id}")
    public String jobDelete(@PathVariable("id") Integer id) {
        jobService.delete(id);
        return "删除成功";
    }

    @ResponseBody
    @GetMapping("/worker/search/allJobs")
    public Map<String, Object> managerAudit(HttpServletRequest request,
                                            @RequestParam(value = "content", required = false) String content) {
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        int pageNumber = Integer.parseInt(request.getParameter("page"));
        Map<String, Object> result = new HashMap<String, Object>();
        if (content != null) {
            Page<Job> jobs = jobService.findByTitleLike(content, pageNumber, pageSize);
            result.put("count", jobs.getTotalElements());
            JSONArray json = JSONArray.fromObject(jobs.getContent());
            result.put("data", json);
        } else {
            Page<Job> jobs = jobService.getJobs(pageNumber, pageSize);
            result.put("count", jobs.getTotalElements());
            JSONArray json = JSONArray.fromObject(jobs.getContent());
            result.put("data", json);
        }
        result.put("code", 0);
        result.put("msg", "");
        return result;
    }

    @GetMapping("/worker/job/{id}")
    public String job(@PathVariable("id") int jobId, Model model, HttpSession session) {
        int workerId = Integer.parseInt(session.getAttribute("workerId").toString());
        Job job = jobService.findById(jobId);
        if (collectService.findByWorkerIdAndJobId(workerId, jobId) != null) {
            job.setCollection(1);
        }
        ;
        if (deliverService.findByWorkerIdAndJobId(workerId, jobId) != null) {
            job.setDeliver(1);
        }
        model.addAttribute("job", job);
        return "worker/job";
    }

    @ResponseBody
    @PostMapping(value = "/worker/job/save")
    public String saveJob(@RequestParam HashMap<String, String> map, HttpSession session) {
        Collect workerAndJob = new Collect();
        int jobId = Integer.parseInt(map.get("id"));
        int workerId = Integer.parseInt(session.getAttribute("workerId").toString());
        workerAndJob.setJobId(jobId);
        workerAndJob.setWorkerId(workerId);
        collectService.save(workerAndJob);
        return "收藏成功";
    }

    @ResponseBody
    @PostMapping(value = "/worker/job/cancelSave")
    public String cancelSaveJob(@RequestParam HashMap<String, String> map, HttpSession session) {
        int jobId = Integer.parseInt(map.get("id"));
        int workerId = Integer.parseInt(session.getAttribute("workerId").toString());
        collectService.delete(workerId, jobId);
        return "取消收藏";
    }

    @ResponseBody
    @PostMapping(value = "/worker/job/deliver")
    public String deliver(@RequestParam HashMap<String, String> map, HttpSession session) {
        Deliver deliver = new Deliver();
        int jobId = Integer.parseInt(map.get("id"));
        int workerId = Integer.parseInt(session.getAttribute("workerId").toString());
        deliver.setJobId(jobId);
        deliver.setWorkerId(workerId);
        deliverService.save(deliver);
        return "投递成功";
    }

    @ResponseBody
    @PostMapping(value = "/worker/job/cancelDeliver")
    public String cancelDeliver(@RequestParam HashMap<String, String> map, HttpSession session) {
        int jobId = Integer.parseInt(map.get("id"));
        int workerId = Integer.parseInt(session.getAttribute("workerId").toString());
        deliverService.delete(workerId, jobId);
        return "取消投递";
    }
}
