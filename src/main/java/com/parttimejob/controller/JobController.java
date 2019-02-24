package com.parttimejob.controller;

import com.parttimejob.entity.Job;
import com.parttimejob.entity.Manager;
import com.parttimejob.repository.JobRepository;
import com.parttimejob.service.JobService;
import com.parttimejob.service.ManagerService;
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

    @ResponseBody
    @PostMapping("/manager/publish/save")
    public String managerPublishSave(Job job, HttpSession session) {
        int id = Integer.parseInt(session.getAttribute("id").toString());
        job.setManagerId(id);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        job.setDate(df.format(new Date()));
        jobService.jobSave(job);
        return "发布成功！";
    }

    @GetMapping("/manager/allJob")
    public String findAllJob(HttpSession session, Model model) {
        int id = Integer.parseInt(session.getAttribute("id").toString());
        List<Job> jobs = jobService.findByManagerId(id);
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
    public String jobDelete(@PathVariable("id") Integer id, HttpSession session) {

        jobService.delete(id);
        return "删除成功";
    }

    @ResponseBody
    @PostMapping("/job/find")
    public List<Job> jobFindByContent(Job job) {
        List<Job> jobs =jobService.findByNameLike(job.getContent());
        return jobs ;
    }

    @ResponseBody
    @GetMapping("/worker/search/allJobs")
    public Map<String, Object> managerAudit(Model model, HttpServletRequest request) {
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        int pageNumber = Integer.parseInt(request.getParameter("page"));
        Page<Job> jobs = jobService.getJobs(pageNumber, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", jobs.getTotalElements());
        JSONArray json = JSONArray.fromObject(jobs.getContent());
        result.put("data", json);
        return result;
    }

    @GetMapping("/worker/job/{id}")
    public String job(@PathVariable("id") int id,Model model){
        Job job =jobService.findById(id);
        model.addAttribute("job",job);
        return "worker/job";
    }

}
