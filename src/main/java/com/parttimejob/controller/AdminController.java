package com.parttimejob.controller;

import com.parttimejob.entity.Admin;
import com.parttimejob.entity.Manager;
import com.parttimejob.entity.Worker;
import com.parttimejob.entity.WorkerData;
import com.parttimejob.repository.AdminRepository;
import com.parttimejob.repository.ManagerRepository;
import com.parttimejob.service.AdminService;
import com.parttimejob.service.ManagerService;
import com.parttimejob.service.WorkerDataService;
import com.parttimejob.service.WorkerService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.controller
 * @Author: Hinstein
 * @CreateTime: 2019-02-03 16:51
 * @Description:
 */

@Controller
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    ManagerService managerService;

    @Autowired
    WorkerService workerService;

    @Autowired
    WorkerDataService workerDataService;

    @PostMapping("admin/login")
    @ResponseBody
    public String adminLogin(Admin admin, HttpSession session) {
        String username = admin.getUserName();
        String password = admin.getPassword();
        Admin admin1 = adminService.findByUserName(username);
        if (admin1 != null) {
            if (admin1.getPassword().equals(password)) {
                session.setAttribute("adminUserName", admin1.getUserName());
                return "登录成功,正在跳转";
            }
            return "密码错误";
        }
        return "不存在该用户";
    }

    @GetMapping("admin/index")
    public String adminIndex() {
        return "admin/index";
    }

    @GetMapping("admin/manager/audit")
    public String managerAudit() {
        return "admin/manager/audit";
    }

    @ResponseBody
    @PostMapping("admin/manager/audit/delete/{id}")
    public String deleteManager(@PathVariable("id") Integer id) {
        managerService.deleteManagerById(id);
        return "删除成功";
    }

    @ResponseBody
    @PostMapping("admin/manager/audit/pass/{id}")
    public String passManager(@PathVariable("id") Integer id) {
        managerService.passManager(id);
        return "通过成功";
    }

    @ResponseBody
    @GetMapping("admin/manager/audit/data")
    public Map<String, Object> managerAudit(HttpServletRequest request) {
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        int pageNumber = Integer.parseInt(request.getParameter("page"));
        Page<Manager> managers = managerService.findByAudit(pageNumber, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", managers.getTotalElements());
        JSONArray json = JSONArray.fromObject(managers.getContent());
        result.put("data", json);
        return result;
    }

    @GetMapping("/admin/workers")
    public String allWorkers() {
        return "admin/worker/all";
    }

    @ResponseBody
    @GetMapping("/admin/workers/data")
    public Map<String, Object> workersData(HttpServletRequest request) {
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        int pageNumber = Integer.parseInt(request.getParameter("page"));
        Page<Worker> workers = workerService.getWorkers(pageNumber, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", workers.getTotalElements());
        JSONArray json = JSONArray.fromObject(workers.getContent());
        result.put("data", json);
        return result;
    }

    @GetMapping("/admin/worker/information")
    public String workerInformation() {
        return "admin/worker/information";
    }

    @ResponseBody
    @GetMapping("/admin/workers/information/data")
    public Map<String, Object> workersInformationData(HttpServletRequest request) {
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        int pageNumber = Integer.parseInt(request.getParameter("page"));
        Page<WorkerData> workers = workerDataService.findAll(pageNumber, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", workers.getTotalElements());
        JSONArray json = JSONArray.fromObject(workers.getContent());
        result.put("data", json);
        return result;
    }

    @GetMapping("/admin/jobs")
    public String adminJobs() {
        return "admin/job/all";
    }

    @GetMapping("/admin/manager/editor")
    public String managerEditor() {
        return "admin/manager/editor";
    }

    @ResponseBody
    @GetMapping("/admin/manager/editor/data")
    public Map<String, Object> managerEditorData(HttpServletRequest request) {
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        int pageNumber = Integer.parseInt(request.getParameter("page"));
        Page<Manager> managers = managerService.findAll(pageNumber, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", managers.getTotalElements());
        JSONArray json = JSONArray.fromObject(managers.getContent());
        result.put("data", json);
        return result;
    }

    @GetMapping("/admin/worker/{id}")
    public String workerId(@PathVariable("id") int id, Model model) {
        Worker worker = workerService.findById(id);
        WorkerData workerData = workerDataService.findByWorkerId(id);
        model.addAttribute("w", worker);
        model.addAttribute("worker", workerData);
        return "admin/worker/editor";
    }

    @ResponseBody
    @PostMapping("/admin/worker/save")
    public String adminWorkerSave(WorkerData workerData, HttpServletRequest request) {
        workerDataService.save(workerData);
        int workerId = Integer.parseInt(request.getParameter("workerId"));
        String workerName = request.getParameter("workerName");
        String workerPassword = request.getParameter("workerPassword");
        Worker worker=new Worker();
        worker.setId(workerId);
        worker.setUserName(workerName);
        worker.setPassword(workerPassword);
        workerService.save(worker);
        return "更新成功";
    }
}
