package com.parttimejob.controller;

import com.parttimejob.entity.*;
import com.parttimejob.service.*;
import com.sun.org.apache.xpath.internal.operations.Mod;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    @Autowired
    JobService jobService;

    @Autowired
    BBSService bbsService;

    @Autowired
    AdminDataTypeService adminDataTypeService;

    /**
     * 管理员登录
     *
     * @param admin
     * @param session
     * @return
     */
    @PostMapping("/admin/login")
    @ResponseBody
    public Map<String, String> adminLogin(Admin admin, HttpSession session, HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        String rightCode = (String) request.getSession().getAttribute("rightCode");
        String tryCode = request.getParameter("tryCode");
        if (tryCode.equals(rightCode)) {
            String username = admin.getUserName();
            String password = admin.getPassword();
            Admin admin1 = adminService.findByUserName(username);
            if (admin1 != null) {
                if (admin1.getPassword().equals(password)) {
                    session.setAttribute("username", admin1.getUserName());
                    session.setAttribute("adminUserName", admin1.getUserName());
                    List<Worker> workers = workerService.findAll();
                    List<Manager> managers = managerService.findAll();
                    List<Job> jobs = jobService.findAll();
                    List<BBS> bbs = bbsService.findAll();
                    session.setAttribute("workers", workers.size());
                    session.setAttribute("managers", managers.size());
                    session.setAttribute("bbs", bbs.size());
                    session.setAttribute("jobs", jobs.size());

                    session.setAttribute("gradeOne", jobService.gradeOne());
                    session.setAttribute("gradeTwo", jobService.gradeTwo());
                    session.setAttribute("gradeThree", jobService.gradeThree());
                    session.setAttribute("gradeFour", jobService.gradeFour());
                    session.setAttribute("noLimit", jobService.noLimit());

                    session.setAttribute("workerGradeOne", workerDataService.workerGradeOne());
                    session.setAttribute("workerGradeTwo", workerDataService.workerGradeTwo());
                    session.setAttribute("workerGradeThree", workerDataService.workerGradeThree());
                    session.setAttribute("workerGradeFour", workerDataService.workerGradeFour());

                    map.put("success", "登录成功");
                }
                map.put("error", "密码错误");
            } else {
                map.put("error", "不存在该用户");
            }
        } else {
            map.put("error", "验证码错误");
        }
        return map;
    }

    /**
     * 用户退出
     *
     * @param session
     * @return
     */
    @GetMapping("/admin/exit")
    public String adminExit(HttpSession session) {
        session.removeAttribute("username");
        session.removeAttribute("adminUserName");
        session.removeAttribute("workers");
        session.removeAttribute("managers");
        session.removeAttribute("bbs");
        session.removeAttribute("jobs");
        return "redirect:/index";
    }

    /**
     * 管理员主页
     *
     * @return
     */
    @GetMapping("/admin/index")
    public String adminIndex(Model model) {
        AdminDataType adminDataType = adminDataTypeService.findById();
        model.addAttribute("adminDataType", adminDataType);
        return "/admin/index";
    }

    /**
     * 未通过审核的兼职者页面
     *
     * @return
     */
    @GetMapping("/admin/manager/audit")
    public String managerAudit() {
        return "/admin/manager/audit";
    }

    /**
     * 删除兼职者
     *
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping("/admin/manager/audit/delete/{id}")
    public String deleteManager(@PathVariable("id") Integer id) {
        managerService.deleteManagerById(id);
        return "删除成功";
    }

    /**
     * 兼职者通过审核
     *
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping("/admin/manager/audit/pass/{id}")
    public String passManager(@PathVariable("id") Integer id) {
        managerService.passManager(id);
        return "通过成功";
    }

    /**
     * 兼职者投递信息
     *
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/admin/manager/audit/data")
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

    /**
     * 查看所有兼职者页面
     *
     * @return
     */
    @GetMapping("/admin/workers")
    public String allWorkers() {
        return "/admin/worker/all";
    }

    /**
     * 所有兼职者资料页面
     *
     * @param request
     * @return
     */
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

    /**
     * 兼职者信息页面
     *
     * @return
     */
    @GetMapping("/admin/worker/information")
    public String workerInformation() {
        return "/admin/worker/information";
    }

    /**
     * 得到所有兼职者信息页面
     *
     * @param request
     * @return
     */
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

    /**
     * 查找所有工作页面
     *
     * @return
     */
    @GetMapping("/admin/jobs")
    public String adminJobs() {
        return "/admin/job/all";
    }

    /**
     * 查找招聘者页面
     *
     * @return
     */
    @GetMapping("/admin/manager/editor")
    public String managerEditor() {
        return "/admin/manager/all";
    }

    /**
     * 查找招聘者资料
     *
     * @param request
     * @return
     */
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

    /**
     * 兼职者资料页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/admin/worker/{id}")
    public String workerId(@PathVariable("id") int id, Model model) {
        Worker worker = workerService.findById(id);
        WorkerData workerData = workerDataService.findByWorkerId(id);
        model.addAttribute("w", worker);
        model.addAttribute("worker", workerData);
        return "/admin/worker/editor";
    }

    /**
     * 更新兼职者资料页面
     *
     * @param workerData
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/admin/worker/save")
    public String adminWorkerSave(WorkerData workerData, HttpServletRequest request) {
        workerDataService.save(workerData);
        int workerId = Integer.parseInt(request.getParameter("workerId"));
        String workerName = request.getParameter("workerName");
        String workerPassword = request.getParameter("workerPassword");
        Worker worker = new Worker();
        worker.setId(workerId);
        worker.setUserName(workerName);
        worker.setPassword(workerPassword);
        workerService.save(worker);
        return "更新成功";
    }

    /**
     * 删除兼职用户
     */
    @ResponseBody
    @PostMapping("/admin/worker/delete/{id}")
    public String workerDelete(@PathVariable("id") int id) {
        workerService.deleteById(id);
        return "删除成功！";
    }

    /**
     * 招聘者资料页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/admin/manager/{id}")
    public String managerId(@PathVariable("id") int id, Model model) {
        Manager manager = managerService.findById(id);
        model.addAttribute("manager", manager);
        return "/admin/manager/editor";
    }

    /**
     * 招聘者修改页面
     *
     * @param manager
     * @return
     */
    @ResponseBody
    @PostMapping("/admin/manager/save")
    public String managerSave(Manager manager) {
        managerService.adminSave(manager);
        return "修改成功！";
    }

    /**
     * 工作信息页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/admin/job/{id}")
    public String jobId(@PathVariable("id") int id, Model model) {
        Job job = jobService.findById(id);
        model.addAttribute("job", job);
        return "/admin/job/editor";
    }

    /**
     * 删除工作
     *
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping("/admin/job/delete/{id}")
    public String jobDelete(@PathVariable("id") int id) {
        jobService.deleteById(id);
        return "删除成功";
    }

    @GetMapping("/admin/BBS/index")
    public String bbsIndex() {
        return "/admin/BBS/index";
    }

    @ResponseBody
    @GetMapping("/admin/BBS/data")
    public Map<String, Object> bbsData(HttpServletRequest request) {
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

    @ResponseBody
    @PostMapping("/admin/BBS/delete/{id}")
    public String bbsDelete(@PathVariable("id") int id) {
        bbsService.deleteById(id);
        return "删除成功！";
    }

    @GetMapping("/admin/BBS/editor/{id}")
    public String bbsEditor(@PathVariable("id") int id, Model model) {
        BBS bbs = bbsService.findById(id);
        model.addAttribute("bbs", bbs);
        return "/admin/BBS/editor";
    }

    @ResponseBody
    @PostMapping("/admin/BBS/editor/save")
    public String bbsEditorSave(BBS bbs) {
        bbsService.editorSave(bbs);
        return "修改成功！";
    }

    @ResponseBody
    @GetMapping("/admin/job/hot")
    public Map<String, Object> jobHot(HttpServletRequest request) {
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        int pageNumber = Integer.parseInt(request.getParameter("page"));
        Page<Job> jobs2 = jobService.finDescByViews(pageNumber, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", jobs2.getTotalElements());
        JSONArray json = JSONArray.fromObject(jobs2.getContent());
        result.put("data", json);
        return result;
    }

    @ResponseBody
    @GetMapping("/admin/job/new")
    public Map<String, Object> jobNew(HttpServletRequest request) {
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        int pageNumber = Integer.parseInt(request.getParameter("page"));
        Page<Job> jobs1 = jobService.finDescByTime(pageNumber, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", jobs1.getTotalElements());
        JSONArray json = JSONArray.fromObject(jobs1.getContent());
        result.put("data", json);
        return result;
    }

    @ResponseBody
    @GetMapping("/admin/worker/active")
    public Map<String, Object> workerActive(HttpServletRequest request) {
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        int pageNumber = Integer.parseInt(request.getParameter("page"));
        Page<WorkerData> workerData = workerDataService.workerActive(pageNumber, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", workerData.getTotalElements());
        JSONArray json = JSONArray.fromObject(workerData.getContent());
        result.put("data", json);
        return result;
    }

    @GetMapping("/admin/photo/{id}")
    public String photo(@PathVariable("id") int id, Model model) {
        Manager manager = managerService.findById(id);
        model.addAttribute("src", manager.getRelativePath());
        return "/manager/photo";
    }

    @GetMapping("/admin/editor")
    public String indexEditor(Model model) {
        AdminDataType adminDataType = adminDataTypeService.findById();
        model.addAttribute("adminDataType", adminDataType);
        return "/admin/data/editor";
    }

    @ResponseBody
    @PostMapping("/admin/editor/save")
    public Map<String, String> eidtorSave(HttpServletRequest request) {
        AdminDataType adminDataType = new AdminDataType();
        HashMap<String, String> map = new HashMap<>();
        String data = request.getParameter("data");
        String jobType = request.getParameter("jobType");
        String managerType = request.getParameter("managerType");
        if (data != null) {
            adminDataType.setData(1);
        } else {
            adminDataType.setData(0);
        }
        if (jobType != null) {
            adminDataType.setJobType(1);
        } else {
            adminDataType.setJobType(0);
        }
        if (managerType != null) {
            adminDataType.setManagerType(1);
        } else {
            adminDataType.setManagerType(0);
        }
        adminDataTypeService.updata(adminDataType);
        map.put("success", "保存成功！");
        return map;
    }


    @ResponseBody
    @PostMapping("/admin/data/selfDefine/put")
    public Map<String, Object> selfDefinePut(HttpServletRequest request) {

        Map<String, Object> result = new HashMap<String, Object>();

        String collection = request.getParameter("collect");
        String deliver = request.getParameter("deliver");
        String employ = request.getParameter("employ");
        String subject = request.getParameter("subject");
        String type = request.getParameter("type");
        String workerLimit = request.getParameter("workerLimit");

        List<Job> jobs=jobService.jobData( subject, type, workerLimit);
        JSONArray json = new JSONArray();
        if (collection != null) {

            JSONObject json1 = new JSONObject();
            json1.accumulate("name", "收藏量");
            int collectNum=0;
            for(Job job: jobs){
                collectNum =job.getCollection()+collectNum;
            }
            json1.accumulate("value", collectNum);
            json.add(json1);
        }
        if (deliver != null) {

            JSONObject json2 = new JSONObject();
            json2.accumulate("name", "投递量");
            int deliverNum=0;
            for(Job job: jobs){
                deliverNum =job.getDeliver()+deliverNum;
            }

            json2.accumulate("value", deliverNum);
            json.add(json2);
        }
        if (employ != null) {

            JSONObject json3 = new JSONObject();
            json3.accumulate("name", "录用量");
            int employNum=0;
            for(Job job: jobs){
                employNum =job.getEmployNumber()+employNum;
            }
            json3.accumulate("value", employNum);
            json.add(json3);
        }
        result.put("data", json);
        return result;
    }
}
