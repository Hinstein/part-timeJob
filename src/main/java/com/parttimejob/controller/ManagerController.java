package com.parttimejob.controller;

import com.parttimejob.entity.*;
import com.parttimejob.service.*;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
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

    @Autowired
    EvaluationToWorkerService evaluationToWorkerService;

    @Autowired
    EvaluationToManagerService evaluationToManagerService;

    @Autowired
    CollectService collectService;

    @Autowired
    BBSService bbsService;

    @Autowired
    CollectWorkerService collectWorkerService;

    @Autowired
    MessageService messageService;

    @Autowired
    DiscussService discussService;

    /**
     * 招聘者注册
     *
     * @param manager
     * @return/manager/register
     */
    @ResponseBody
    @PostMapping("/manager/register")
    public String registerWorker(Manager manager, HttpSession session) {
        Manager manager1 = managerService.findByUserName(manager.getUserName());
        if (manager1 != null) {
            return "注册失败，存在该用户";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        manager.setHeadPhoto("/assets/picture/head.png");
        manager.setDate(df.format(new Date()));
        session.setAttribute("registerManager", manager);
//        managerService.save(manager);
        return "注册成功,等待管理员审核";
    }

    /**
     * 招聘者登录
     *
     * @param manager
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("/manager/login")
    public Map<String, String> managerLogin(Manager manager, HttpSession session, HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        String rightCode = (String) request.getSession().getAttribute("rightCode");
        String tryCode = request.getParameter("tryCode");
        if (tryCode.equals(rightCode)) {
            String username = manager.getUserName();
            String password = manager.getPassword();
            Manager manager1 = managerService.findByUserName(username);
            if (manager1 != null) {
                if (manager1.getPassword().equals(password)) {
                    if (manager1.getAudit() == 0) {
                        map.put("error", "该账号正在审核中，请等待的管理员审核");
                        return map;
                    }
                    session.setAttribute("manager", manager1);
                    session.setAttribute("photoSrc", manager1.getRelativePath());
                    session.setAttribute("username", manager1.getUserName());
                    List<Employ> employs = employService.findByManagerId(manager1.getId());
                    List<Job> jobs = jobService.findByManagerId(manager1.getId());
                    List<EvaluationToWorker> evaluations = evaluationToWorkerService.findByManagerId(manager1.getId());
                    List<BBS> bbs = bbsService.findByManagerId(manager1.getId());
                    managerService.active(manager1.getId());
                    session.setAttribute("bbs", bbs.size());
                    session.setAttribute("employs", employs.size());
                    session.setAttribute("jobs", jobs.size());
                    session.setAttribute("evaluations", evaluations.size());
                    map.put("success", "登录成功");
                    return map;
                }
                map.put("error", "密码错误");
                return map;
            }
            map.put("error", "不存在该用户");
            return map;
        }
        map.put("error", "验证码错误");
        return map;
    }

    @GetMapping("/manager/exit")
    public String workerExit(HttpSession session) {
        session.removeAttribute("manager");
        session.removeAttribute("username");
        session.removeAttribute("employs");
        session.removeAttribute("jobs");
        session.removeAttribute("evaluations");
        return "redirect:/index";
    }

    /**
     * 来到招聘者主页
     *
     * @return
     */
    @GetMapping("/manager/index")
    public String managerIndex() {
        return "/manager/index";
    }

    /**
     * 招聘者查找工作页面
     *
     * @return
     */
    @GetMapping("/manager/search")
    public String managerSearch() {
        return "/manager/search";
    }

    /**
     * 招聘者投递页面
     *
     * @param jobId
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/manager/job/deliver/{id}")
    public String jobDeliver(@PathVariable("id") int jobId, Model model, HttpSession session) {
        Job job = new Job();
        job.setId(jobId);
        session.setAttribute("jobId", jobId);
        model.addAttribute("job", job);
        return "/manager/job/deliver";
    }

    /**
     * 招聘者所招聘的员工信息
     *
     * @param jobId
     * @param request
     * @return
     */
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

    /**
     * 查看投递的兼职者的资料
     *
     * @param id
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/manager/deliver/worker/{id}")
    public String workerInformation(@PathVariable("id") int id, Model model, HttpSession session) {
        WorkerData workerData = workerDataService.findByWorkerId(id);
        model.addAttribute("worker", workerData);
        List<EvaluationToWorker> evaluation = evaluationToWorkerService.findByWorkerId(id);
        model.addAttribute("evaluations", evaluation);
        return "/manager/worker";
    }

    /**
     * 录用兼职者
     *
     * @param session
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/employ/{id}")
    public String employWorker(HttpSession session, @RequestParam HashMap<String, String> map, @PathVariable("id") int id) {
        int jobId = Integer.parseInt(session.getAttribute("jobId").toString());
        Job job = jobService.findById(jobId);
        Manager manager = (Manager) session.getAttribute("manager");
        int workerId = id;
        Employ employ = employService.findByWorkerIdAndManagerId(workerId, manager.getId());
        String date = map.get("date");
        String time = map.get("time");
        String address = map.get("address");
        String dateTime = date + " " + time;
        if (employ == null) {
            if (job.getEmployNumber() >= job.getWorkerNumber()) {
                return "录用失败，该工作已达上限";
            } else {
                jobService.employ(jobId);
                Employ employ1 = new Employ();
                employ1.setWorkerId(workerId);
                employ1.setDate(dateTime);
                employ1.setManagerId(manager.getId());
                employ1.setJobId(jobId);
                employ1.setAddress(address);
                employService.save(employ1);
                deliverService.delete(workerId, jobId);
                return "录用成功";
            }
        } else {
            return "你已经录用过该名员工";
        }
    }

    /**
     * 来到兼职者录用的员工页面
     *
     * @return
     */
    @GetMapping("/manager/employ")
    public String managerEmployee() {
        return "/manager/employ";
    }

    /**
     * 得到所有的录用的员工
     *
     * @param session
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/manager/employ/list")
    public Map<String, Object> employee(HttpSession session, HttpServletRequest request) {
        Manager manager = (Manager) session.getAttribute("manager");
        List<Employ> employs = employService.findByManagerId(manager.getId());
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
        } else if (size > 0) {
            JSONArray json = JSONArray.fromObject(workers);
            result.put("data", json);
        }
        result.put("count", size);
        result.put("code", 0);
        result.put("msg", "");
        return result;
    }

    /**
     * 查看员工资料
     *
     * @param id
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/manager/workerDeliver/{id}")
    public String workerDeliver(@PathVariable("id") int id, Model model, HttpSession session) {
        Manager manager = (Manager) session.getAttribute("manager");
        WorkerData workerData = workerDataService.findByWorkerId(id);
        Employ employ = employService.findByWorkerIdAndManagerId(id, manager.getId());
        model.addAttribute("employ", employ);
        model.addAttribute("worker", workerData);
        return "/manager/workerDeliver";
    }

    /**
     * 安排员工面试时间页面
     *
     * @param
     * @param model
     * @return
     */
    @GetMapping("/manager/deliver/worker/setTime/{id}")
    public String cancelEmployee(@PathVariable("id") int id, Model model) {
        int workerId = id;
        WorkerData workerData = workerDataService.findByWorkerId(workerId);
        model.addAttribute("worker", workerData);
        return "/manager/setTime";
    }

    /**
     * 找到工作
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/manager/job/{id}")
    public String jobId(@PathVariable("id") int id, Model model) {
        Job job = jobService.findById(id);
        model.addAttribute("job", job);
        return "/manager/job";
    }

    /**
     * 兼职者更新密码
     *
     * @return
     */
    @GetMapping("/manager/editor")
    public String managerEditor() {
        return "/manager/editor";
    }

    /**
     * 兼职者保存密码
     *
     * @param newPassword
     * @param oldPassword
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("/manager/editor/save")
    public String changePassword(@RequestParam("newPassword") String newPassword,
                                 @RequestParam("oldPassword") String oldPassword,
                                 HttpSession session) {
        Manager m = (Manager) session.getAttribute("manager");
        Manager manager = managerService.findById(m.getId());
        if (manager.getPassword().equals(oldPassword)) {
            manager.setPassword(newPassword);
            managerService.saveEditor(manager);
            return "更新密码成功！";
        } else {
            return "旧密码错误";
        }
    }

    @GetMapping("/manager/worker/evaluate/{id}")
    public String evaluate(@PathVariable("id") int id, Model model, HttpSession session) {
        Worker worker = workerService.findById(id);
        WorkerData workerData = workerDataService.findByWorkerId(id);
        model.addAttribute("workerData", workerData);
        model.addAttribute("worker", worker);
        return "/manager/evaluate";
    }

    @ResponseBody
    @PostMapping("/manager/worker/evaluate/save")
    public String evaluateSave(EvaluationToWorker evaluation, HttpSession session) {
        Manager manager = (Manager) session.getAttribute("manager");
        if (evaluationToWorkerService.findByManagerIdAndWorkerId(manager.getId(), evaluation.getWorkerId()) != null) {
            return "您以评价过该兼职者！";
        } else {
            evaluation.setManagerId(manager.getId());
            evaluation.setManagerName(manager.getName());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            evaluation.setDate(df.format(new Date()));
            employService.evaluated(evaluation.getWorkerId(), manager.getId());
            evaluationToWorkerService.evaluationToWorkerSave(evaluation);
            return "评价成功！";
        }
    }

    @GetMapping("/manager/evaluate")
    public String workerEvaluate(HttpSession session, Model model) {
        Manager manager = (Manager) session.getAttribute("manager");
        List<EvaluationToWorker> evaluations = evaluationToWorkerService.findByManagerId(manager.getId());
        model.addAttribute("evaluations", evaluations);
        return "/manager/workerEvaluate";
    }

    @GetMapping("/manager/evaluation/{id}")
    public String evaluation(@PathVariable("id") int id, Model model, HttpSession session) {
        EvaluationToWorker evaluation = evaluationToWorkerService.findById(id);
        model.addAttribute("evaluation", evaluation);
        return "/manager/evaluation";
    }

    @GetMapping("/manager/search/worker")
    public String managerSearchWorker() {
        return "/manager/searchWorker";
    }

    @GetMapping("/manager/workerInformation/{id}")
    public String workerInformations(@PathVariable("id") int id, Model model, HttpSession session) {
        Manager manager = (Manager) session.getAttribute("manager");
        WorkerData workerData = workerDataService.findByWorkerId(id);
        session.setAttribute("workerId", workerData.getWorkerId());
        if (collectWorkerService.findByWorkerIdAndManagerId(id, manager.getId()) != null) {
            workerData.setCheckCollect(1);
        }
        EvaluationToWorker evaluation = evaluationToWorkerService.findByWorkerIdAndUsed(id);
        model.addAttribute("evaluationHidden", 1);
        model.addAttribute("evaluation", evaluation);
        model.addAttribute("worker", workerData);
        return "/manager/worker";
    }

    @GetMapping("/manager/informationEditor")
    public String managerId() {
        return "/manager/informationEditor";
    }

    @ResponseBody
    @PostMapping("/manager/information/save")
    public String managerInformationSave(Manager manager, HttpSession session) {
        Manager manager1 = (Manager) session.getAttribute("manager");
        Manager registerManager = (Manager) session.getAttribute("registerManager");
        if (registerManager == null) {
            manager.setRelativePath(manager1.getRelativePath());
            manager.setDatePath(manager1.getDatePath());
        } else {
            File file = new File(manager1.getDatePath());
            file.delete();
            manager.setRelativePath(registerManager.getRelativePath());
            manager.setDatePath(registerManager.getDatePath());
            session.setAttribute("photoSrc", registerManager.getRelativePath());
            session.removeAttribute("registerManager");
        }
        managerService.informationSave(manager);
        Manager manager2 = managerService.findById(manager.getId());
        session.setAttribute("manager", manager2);
        return "修改成功！";
    }

    @ResponseBody
    @PostMapping("/manager/information/addPhoto")
    public Map<String, Object> addPhoto(@RequestParam("file") MultipartFile file, HttpSession session, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        //通过session查看当前登录的用户信息
        Manager manager = new Manager();
        try {
            //如果文件不为空
            if (null != file) {
                //生成uuid作为文件名称
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                //获得文件类型（判断如果不是图片文件类型，则禁止上传）
                String contentType = file.getContentType();
                //获得文件后缀名称
                String imageName = contentType.substring(contentType.indexOf("/") + 1);
                //获取文件的项目路径
                String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/images/";
                //根据日期来创建对应的文件夹manager
                String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

                //userId
                String path = filePath;
                //如果不存在，则创建新文件夹
                File f = new File(path);
                if (!f.exists()) {
                    f.mkdirs();
                }
                //新生成的文件名称
                String fileName = uuid + "." + imageName;
                //图片保存的完整路径
                String pathName = path + fileName;
                //图片保存的相对路径
                String relativePath = "/images/" + fileName;
                //将图片从源位置复制到目标位置
                file.transferTo(new File(pathName));

                //设置manager实体类的数据

                manager.setDatePath(pathName);

                manager.setRelativePath(relativePath);
                session.setAttribute("registerManager", manager);
                session.setAttribute("photoSrc", relativePath);
                //新建路径
                map.put("code", 0);
                map.put("msg", "上传成功！");
                map.put("relativePath", relativePath);
                map.put("data", pathName);

            }
            //返回json数据
            else {
                map.put("msg", "文件为空！");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    @GetMapping("/photo")
    public String photo(HttpSession session, Model model) {
        String src = session.getAttribute("photoSrc").toString();
        model.addAttribute("src", src);
        return "/manager/photo";
    }

    @GetMapping("/manager/BBS/index")
    public String managerBBSIndex() {
        return "/manager/bbs/index";
    }

    @ResponseBody
    @PostMapping("/manager/BBS/delete/{id}")
    public String deleteBBS(@PathVariable("id") int id) {
        bbsService.deleteById(id);
        return "删除成功！";
    }

    @GetMapping("/manager/BBS/publish")
    public String managerBBSPublish() {
        return "/manager/bbs/publish";
    }

    @GetMapping("/manager/BBS/editor")
    public String managerBBSEditor() {
        return "/manager/BBS/post";
    }

    @GetMapping("/manager/BBS/editor/{id}")
    public String myBBSEditor(@PathVariable("id") int id, Model model) {
        BBS bbs = bbsService.findById(id);
        model.addAttribute("bbs", bbs);
        return "/manager/BBS/editor";
    }

    @ResponseBody
    @PostMapping("/manager/BBS/editor/save")
    public String manageBBSEditorSave(BBS bbs) {
        bbsService.editorSave(bbs);
        return "修改成功！";
    }

    @GetMapping("/manager/BBS/search")
    public String managerBBSSearch() {
        return "/manager/BBS/search";
    }

    @ResponseBody
    @PostMapping("/manager/BBS/publish/save")
    public String manageBBSPublishSave(BBS bbs, HttpSession session) {
        Manager manager = (Manager) session.getAttribute("manager");
        bbs.setStatus(2);
        bbs.setManagerId(manager.getId());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bbs.setDate(df.format(new Date()));
        bbsService.BBSSave(bbs);
        return "发布成功！";
    }

    @ResponseBody
    @GetMapping("/manager/BBS/article")
    public Map<String, Object> managerBBSArticle(HttpSession session, HttpServletRequest request) {
        Manager manager = (Manager) session.getAttribute("manager");
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        int pageNumber = Integer.parseInt(request.getParameter("page"));
        Page<BBS> bbs = bbsService.findByManagerId(manager.getId(), pageNumber, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", bbs.getTotalElements());
        JSONArray json = JSONArray.fromObject(bbs.getContent());
        result.put("data", json);
        return result;
    }

    @ResponseBody
    @GetMapping("/manager/BBS/findAll")
    public Map<String, Object> managerBBSArticleFindAll(HttpSession session, HttpServletRequest request) {
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

    @GetMapping("/manager/BBS/look/{id}")
    public String look(@PathVariable("id") int id, Model model) {
        BBS bbs = bbsService.findById(id);
        if (bbs.getStatus() == 1) {
            model.addAttribute("status", "招聘者");
            WorkerData workerData = workerDataService.findByWorkerId(bbs.getWorkerId());
            model.addAttribute("data", workerData);
        } else if (bbs.getStatus() == 2) {
            model.addAttribute("status", "兼职者");
            Manager manager = managerService.findById(bbs.getManagerId());
            model.addAttribute("data", manager);
        }
        Page<BBS> view = bbsService.view(1, 5);
        Page<BBS> hot = bbsService.hot(1, 5);
        bbsService.views(id);
        List<Discuss> discussList = discussService.findByBbsId(id);
        model.addAttribute("bbs", bbs);
        model.addAttribute("discussList",discussList);
        model.addAttribute("view",view.getContent());
        model.addAttribute("hot",hot.getContent());
        return "manager/BBS/look";
    }

    @ResponseBody
    @GetMapping("/manager/workers/information/data")
    public Map<String, Object> workersInformationData(HttpServletRequest request,
                                                      @RequestParam(value = "content", required = false) String content) {
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        int pageNumber = Integer.parseInt(request.getParameter("page"));

        Map<String, Object> result = new HashMap<String, Object>();

        if (content != null) {
            Page<WorkerData> workers = workerDataService.findByJobIntensionLike(content, pageNumber, pageSize);
            result.put("count", workers.getTotalElements());
            JSONArray json = JSONArray.fromObject(workers.getContent());
            result.put("data", json);
        } else {
            Page<WorkerData> workers = workerDataService.findAll(pageNumber, pageSize);
            result.put("count", workers.getTotalElements());
            JSONArray json = JSONArray.fromObject(workers.getContent());
            result.put("data", json);
        }
        result.put("code", 0);
        result.put("msg", "");
        return result;
    }

    @GetMapping("/manager/evaluated")
    public String managerEvaluated(HttpSession session, Model model) {
        Manager manager = (Manager) session.getAttribute("manager");
        List<EvaluationToManager> evaluations = evaluationToManagerService.findByManagerId(manager.getId());
        model.addAttribute("evaluations", evaluations);
        return "/manager/managerEvaluate";
    }

    @GetMapping("/manager/receiveEvaluation/{id}")
    public String receiveEvaluation(@PathVariable("id") int id, Model model) {
        EvaluationToManager evaluation = evaluationToManagerService.findById(id);
        model.addAttribute("evaluation", evaluation);
        return "/manager/receiveEvaluation";
    }

    @ResponseBody
    @PostMapping(value = "/manager/workerData/save")
    public String saveWorkerData(@RequestParam HashMap<String, String> map, HttpSession session) {
        CollectWorker managerAndWorker = new CollectWorker();
        int workerDataId = Integer.parseInt(map.get("id"));
        int workerId = Integer.parseInt(map.get("workerId"));
        Manager manager = (Manager) session.getAttribute("manager");
        managerAndWorker.setWorkerId(workerId);
        managerAndWorker.setManagerId(manager.getId());
        managerAndWorker.setWorkerDataId(workerDataId);
        collectWorkerService.save(managerAndWorker);
        return "收藏成功";
    }

    /**
     * 取消收藏
     *
     * @param map
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/manager/workerData/cancelSave")
    public String cancelSaveJob(@RequestParam HashMap<String, String> map, HttpSession session, Model model) {
        int workerId = Integer.parseInt(map.get("workerId"));
        Manager manager = (Manager) session.getAttribute("manager");
        collectWorkerService.deleteByWorkerIdAndManagerId(workerId, manager.getId());
        return "取消收藏";
    }

    @GetMapping("/manager/collect")
    public String workerCollect(Model model, HttpSession session) {
        Manager manager = (Manager) session.getAttribute("manager");
        List<CollectWorker> collectWorkers = collectWorkerService.findByManagerId(manager.getId());
        List<WorkerData> workerData = new ArrayList<>();
        for (CollectWorker w : collectWorkers) {
            WorkerData workerData1 = workerDataService.findByWorkerId(w.getWorkerId());
            workerData.add(workerData1);
        }
        model.addAttribute("workers", workerData);
        return "/manager/collect";
    }

    @GetMapping("/manager/message")
    public String managerMessage(HttpSession session, Model model) {
        Manager manager = (Manager) session.getAttribute("manager");
        List<Message> messages = messageService.findByManagerId(manager.getId());
        model.addAttribute("messages", messages);
        return "/manager/message";
    }

    @ResponseBody
    @DeleteMapping("/manager/message/delete/{id}")
    public Map<String, String> deleteMessage(@PathVariable("id") int id) {
        HashMap<String, String> map = new HashMap<>();
        messageService.deleteById(id);
        map.put("success", "删除成功！");
        return map;
    }

    @ResponseBody
    @PostMapping("/manager/deliver/cancel/{id}")
    public String deliverCancel(@PathVariable("id") int workerId, HttpSession session) {
        int jobId = Integer.parseInt(session.getAttribute("jobId").toString());
        deliverService.delete(workerId, jobId);
        return "已拒绝录用";
    }

    @GetMapping("/manager/worker/resume/{id}")
    public String workerResume(@PathVariable("id") int id, Model model) {
        WorkerData worker = workerDataService.findByWorkerId(id);
        model.addAttribute("worker", worker);
        EvaluationToWorker evaluation = evaluationToWorkerService.findByWorkerIdAndUsed(id);
        if (evaluation != null) {
            model.addAttribute("evaluation", evaluation);
            Manager manager = managerService.findById(evaluation.getManagerId());
            model.addAttribute("vendorName", manager.getVendorName());
        }
        return "/manager/resume/index";
    }

    @GetMapping("/manager/editor/headPhoto")
    public String headPhotoEditor() {
        return "/manager/editor/headPhotoEditor";
    }

    @ResponseBody
    @PostMapping("/manager/editor/headPhoto")
    public Map<String, Object> editorHeadPhoto(@RequestParam("file") MultipartFile file, HttpSession session, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        //通过session查看当前登录的用户信息
        Manager manager = (Manager) session.getAttribute("manager");
        try {
            //如果文件不为空
            if (null != file) {
                //生成uuid作为文件名称
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                //获得文件类型（判断如果不是图片文件类型，则禁止上传）
                String contentType = file.getContentType();
                //获得文件后缀名称
                String imageName = contentType.substring(contentType.indexOf("/") + 1);
                //获取文件的项目路径
                String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/images/";
                //根据日期来创建对应的文件夹
                String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
                //根据id分类来创建对应的文件夹
                String leagueIdPath = manager.getId() + "/";
                //userId
                String path = filePath + leagueIdPath;
                //如果不存在，则创建新文件夹
                File f = new File(path);
                if (!f.exists()) {
                    f.mkdirs();
                }
                //新生成的文件名称
                String fileName = uuid + "." + imageName;
                //图片保存的完整路径
                String pathName = path + fileName;
                //图片保存的相对路径
                String relativePath = "/images/" + leagueIdPath + fileName;
                //将图片从源位置复制到目标位置
                file.transferTo(new File(pathName));

                //设置photo实体类的数据
                if(manager.getPathName()!=null){
                    File file1 = new File(manager.getPathName());
                    file1.delete();
                }
                managerService.headPhotoEditor(relativePath, pathName,manager.getId());
                Manager manager1 = managerService.findById(manager.getId());
                session.setAttribute("manager", manager1);
                //返回json数据
                map.put("code", 0);
                map.put("relativePath", relativePath);
                map.put("data", pathName);
            } else {
                map.put("msg", "文件为空！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    @ResponseBody
    @PostMapping("/manager/BBS/discuss/{id}")
    public Map<String,String> discuss(@PathVariable("id") int bbsId,HttpServletRequest request,HttpSession session){
        Manager manager = (Manager) session.getAttribute("manager");
        String content=request.getParameter("content");
        Discuss discuss = new Discuss();
        discuss.setBbsId(bbsId);
        discuss.setContent(content);
        discuss.setHeadPhoto(manager.getHeadPhoto());
        discuss.setAuthor(manager.getName());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        discuss.setTime(df.format(new Date()));
        discussService.save(discuss);
        HashMap<String, String> map = new HashMap<>();
        map.put("success","评论成功！");
        return  map;
    }

    @ResponseBody
    @PostMapping("/manager/BBS/good/{id}")
    public Map<String,String> good(@PathVariable("id") int discussId){
        int good = discussService.good(discussId);
        HashMap<String, String> map = new HashMap<>();
        map.put("good",good+"");
        return  map;
    }

    @ResponseBody
    @PostMapping("/manager/BBS/cancelGood/{id}")
    public Map<String,String> cancelGood(@PathVariable("id") int discussId){
        int good = discussService.cancelGood(discussId);
        HashMap<String, String> map = new HashMap<>();
        map.put("good",good+"");
        return  map;
    }

}
