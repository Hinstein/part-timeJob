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
    ManagerService managerService;

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

    @Autowired
    EvaluationToManagerService evaluationToManagerService;

    @Autowired
    MessageService messageService;

    @Autowired
    DiscussService discussService;

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
        workerData.setHeadPhoto("/assets/picture/head.png");
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
    public Map<String, String> workerLogin(Worker worker, HttpSession session, HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        String rightCode = (String) request.getSession().getAttribute("rightCode");
        String tryCode = request.getParameter("tryCode");
        if (tryCode.equals(rightCode)) {
            String username = worker.getUserName();
            String password = worker.getPassword();
            Worker worker1 = workerService.findByUserName(username);
            if (worker1 != null) {
                if (worker1.getPassword().equals(password)) {
                    session.setAttribute("worker", worker1);
                    session.setAttribute("username", worker1.getUserName());
                    WorkerData workerData = workerDataService.findByWorkerId(worker1.getId());
                    session.setAttribute("workerData", workerData);
                    workerDataService.active(worker1.getId());
                    List<Collect> collectList = collectService.findByWorkerId(worker1.getId());
                    List<Deliver> delivers = deliverService.findByWorkerId(worker1.getId());
                    List<BBS> bbs = bbsService.findByWorkerId(worker1.getId());
                    List<EvaluationToWorker> evaluations = evaluationToWorkerService.findByWorkerId(worker1.getId());
                    session.setAttribute("collectList", collectList.size());
                    session.setAttribute("delivers", delivers.size());
                    session.setAttribute("bbs", bbs.size());
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
            return "/worker/index";
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
        return "/worker/resume";
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
        Worker worker = (Worker) session.getAttribute("worker");
        if (worker.getPassword().equals(oldPassword)) {
            worker.setPassword(newPassword);
            workerService.save(worker);
            return "更新密码成功！";
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
        List<Collect> workerAndJobs = collectService.findByWorkerId(worker.getId());
        List<Job> jobs = new ArrayList<>();
        for (Collect w : workerAndJobs) {
            Job job = jobService.findById(w.getJobId());
            jobs.add(job);
        }
        model.addAttribute("jobs", jobs);
        return "/worker/collect";
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
        return "/worker/deliver";
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
        return "/worker/employ";
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
    @PostMapping("/worker/BBS/delete/{id}")
    public String deleteBBS(@PathVariable("id") int id) {
        bbsService.deleteById(id);
        return "删除成功！";
    }

    @GetMapping("/worker/BBS/publish")
    public String workerBBSPublish() {
        return "/worker/bbs/publish";
    }

    @GetMapping("/worker/BBS/editor")
    public String workerBBSEditor() {
        return "/worker/BBS/post";
    }

    @GetMapping("/worker/BBS/editor/{id}")
    public String myBBSEditor(@PathVariable("id") int id, Model model) {
        BBS bbs = bbsService.findById(id);
        model.addAttribute("bbs", bbs);
        return "/worker/BBS/editor";
    }

    @ResponseBody
    @PostMapping("/worker/BBS/editor/save")
    public String manageBBSEditorSave(BBS bbs) {
        bbsService.editorSave(bbs);
        return "修改成功！";
    }

    @GetMapping("/worker/BBS/search")
    public String managerBBSSearch() {
        return "worker/BBS/search";
    }

    @ResponseBody
    @PostMapping("/worker/BBS/publish/save")
    public String manageBBSPublishSave(BBS bbs, HttpSession session) {
        Worker worker = (Worker) session.getAttribute("worker");
        bbs.setStatus(1);
        bbs.setWorkerId(worker.getId());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bbs.setDate(df.format(new Date()));
        bbsService.BBSSave(bbs);
        return "发布成功！";
    }

    @ResponseBody
    @GetMapping("/worker/BBS/article")
    public Map<String, Object> managerBBSArticle(HttpSession session, HttpServletRequest request) {
        Worker worker = (Worker) session.getAttribute("worker");
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        int pageNumber = Integer.parseInt(request.getParameter("page"));
        Page<BBS> bbs = bbsService.findByWorkerId(worker.getId(), pageNumber, pageSize);
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
    public Map<String, Object> managerBBSArticleFindAll(HttpSession session, HttpServletRequest request) {
        Worker worker = (Worker) session.getAttribute("worker");
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
        model.addAttribute("discussList", discussList);
        model.addAttribute("view", view.getContent());
        model.addAttribute("hot", hot.getContent());
        return "/worker/BBS/look";
    }


    @GetMapping("/worker/employ/job/{id}")
    public String workerEmployee(@PathVariable("id") int id, Model model, HttpSession session) {
        Job job = jobService.findById(id);
        job.setCheckEmploy(1);
        bbsService.views(id);
        Worker worker = (Worker) session.getAttribute("worker");
        Manager manager = managerService.findById(job.getManagerId());
        if (evaluationToManagerService.findByManagerIdAndWorkerId(manager.getId(), worker.getId()) != null) {
            job.setCheckEmploy(2);
        }
        model.addAttribute("manager", manager);
        model.addAttribute("job", job);
        return "/worker/job";
    }

    @GetMapping("/worker/evaluationToManager/{id}")
    public String evaluationToManager(@PathVariable("id") int id, Model model) {
        Manager manager = managerService.findById(id);
        model.addAttribute("manager", manager);
        return "/worker/evaluationToManager";
    }

    @ResponseBody
    @PostMapping("/worker/evaluationToManager/save")
    public String evaluationToManager(EvaluationToManager evaluation, HttpSession session) {
        Worker worker = (Worker) session.getAttribute("worker");
        if (evaluationToManagerService.findByManagerIdAndWorkerId(evaluation.getManagerId(), worker.getId()) != null) {
            return "您以评价过该招聘者！";
        } else {
            WorkerData workerData = workerDataService.findByWorkerId(worker.getId());
            evaluation.setWorkerId(worker.getId());
            evaluation.setWorkerName(workerData.getName());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            evaluation.setDate(df.format(new Date()));
            employService.evaluated(worker.getId(), evaluation.getManagerId());
            evaluationToManagerService.evaluationToManagerSave(evaluation);
            return "评价成功！";
        }
    }

    @GetMapping("/worker/evaluate")
    public String workerEvaluate(HttpSession session, Model model) {
        Worker worker = (Worker) session.getAttribute("worker");
        List<EvaluationToManager> evaluations = evaluationToManagerService.findByWorkerId(worker.getId());
        model.addAttribute("evaluations", evaluations);
        return "/worker/managerEvaluate";
    }

    @GetMapping("/worker/evaluation/{id}")
    public String evaluation(@PathVariable("id") int id, Model model) {
        EvaluationToManager evaluation = evaluationToManagerService.findById(id);
        model.addAttribute("evaluation", evaluation);
        return "/worker/evaluation";
    }

    @GetMapping("/worker/evaluated")
    public String workerEvaluated(HttpSession session, Model model) {
        Worker worker = (Worker) session.getAttribute("worker");
        List<EvaluationToWorker> evaluations = evaluationToWorkerService.findByWorkerId(worker.getId());
        model.addAttribute("evaluations", evaluations);
        return "/worker/workerEvaluate";
    }

    @GetMapping("/worker/receiveEvaluation/{id}")
    public String receiveEvaluation(@PathVariable("id") int id, Model model) {
        EvaluationToWorker evaluation = evaluationToWorkerService.findById(id);
        model.addAttribute("evaluation", evaluation);
        return "/worker/receiveEvaluation";
    }

    @ResponseBody
    @PostMapping("/worker/sendMessage")
    public String saveMessage(Message message, HttpSession session) {
        Worker worker = (Worker) session.getAttribute("worker");
        WorkerData workerData = workerDataService.findByWorkerId(worker.getId());
        message.setWorkerId(worker.getId());
        message.setWorkerName(workerData.getName());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        message.setDate(df.format(new Date()));
        messageService.save(message);
        return "留言成功！";
    }

    @ResponseBody
    @PostMapping("/worker/evaluation/use/{id}")
    public Map<String, String> evaluationUsed(@PathVariable("id") int id, HttpSession session) {
        Map<String, String> map = new HashMap<>();
        Worker worker = (Worker) session.getAttribute("worker");
        EvaluationToWorker evaluationToWorker = evaluationToWorkerService.findByWorkerIdAndUsed(worker.getId());
        if (evaluationToWorker != null) {
            map.put("error", "填加失败，已添加有评论到简历中。<br>请取消后重试！");
        } else {
            evaluationToWorkerService.usedEvaluation(id);
            map.put("success", "填加成功！");
        }
        return map;
    }

    @ResponseBody
    @PostMapping("/worker/evaluation/cancel/{id}")
    public String evaluationCancel(@PathVariable("id") int id) {
        evaluationToWorkerService.cancelUsedEvaluation(id);
        return "取消成功！";
    }

    @ResponseBody
    @GetMapping("/worker/manager/active")
    public Map<String, Object> managerActive(HttpServletRequest request) {
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        int pageNumber = Integer.parseInt(request.getParameter("page"));
        Page<Manager> managers = managerService.findManagers(pageNumber, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", managers.getTotalElements());
        JSONArray json = JSONArray.fromObject(managers.getContent());
        result.put("data", json);
        return result;
    }

    @GetMapping("/worker/employ/time/{id}")
    public String employTime(@PathVariable("id") int jobId, HttpSession session, Model model) {
        Worker worker = (Worker) session.getAttribute("worker");
        Employ employ = employService.findByWorkerIdAndJobId(worker.getId(), jobId);
        model.addAttribute("employ", employ);
        return "/manager/employTime";
    }

    @GetMapping("/worker/resume/index")
    public String resumeIndex(HttpSession session, Model model) {
        Worker worker = (Worker) session.getAttribute("worker");
        WorkerData workerData = workerDataService.findByWorkerId(worker.getId());
        model.addAttribute("worker", workerData);
        EvaluationToWorker evaluation = evaluationToWorkerService.findByWorkerIdAndUsed(worker.getId());
        if (evaluation != null) {
            model.addAttribute("evaluation", evaluation);
            Manager manager = managerService.findById(evaluation.getManagerId());
            model.addAttribute("vendorName", manager.getVendorName());
        }
        return "/worker/resume/index";
    }

    @GetMapping("/worker/editor/headPhoto")
    public String headPhotoEditor() {
        return "/worker/editor/headPhotoEditor";
    }

    @ResponseBody
    @PostMapping("/worker/editor/headPhoto")
    public Map<String, Object> editorHeadPhoto(@RequestParam("file") MultipartFile file, HttpSession session, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        //通过session查看当前登录的用户信息
        WorkerData workerData = (WorkerData) session.getAttribute("workerData");
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
                String leagueIdPath = workerData.getId() + "/";
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
                if (workerData.getPathName() != null) {
                    File file1 = new File(workerData.getPathName());
                    file1.delete();
                }
                workerDataService.headPhotoEditor(relativePath, pathName, workerData.getId());
                WorkerData workerData1 = workerDataService.findByWorkerId(workerData.getWorkerId());
                session.setAttribute("workerData", workerData1);
                //返回json数据
                map.put("code", 0);
                map.put("msg", "上传成功！");
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
    @PostMapping("/worker/BBS/discuss/{id}")
    public Map<String, String> discuss(@PathVariable("id") int bbsId, HttpServletRequest request, HttpSession session) {
        WorkerData workerData = (WorkerData) session.getAttribute("workerData");
        String content = request.getParameter("content");
        Discuss discuss = new Discuss();
        discuss.setBbsId(bbsId);
        discuss.setContent(content);
        discuss.setHeadPhoto(workerData.getHeadPhoto());
        discuss.setAuthor(workerData.getName());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        discuss.setTime(df.format(new Date()));
        discussService.save(discuss);
        HashMap<String, String> map = new HashMap<>();
        map.put("success", "评论成功！");
        return map;
    }

    @ResponseBody
    @PostMapping("/worker/BBS/good/{id}")
    public Map<String, String> good(@PathVariable("id") int discussId) {
        int good = discussService.good(discussId);
        HashMap<String, String> map = new HashMap<>();
        map.put("good", good + "");
        return map;
    }

    @ResponseBody
    @PostMapping("/worker/BBS/cancelGood/{id}")
    public Map<String, String> cancelGood(@PathVariable("id") int discussId) {
        int good = discussService.cancelGood(discussId);
        HashMap<String, String> map = new HashMap<>();
        map.put("good", good + "");
        return map;
    }

    @GetMapping("/worker/findPassword")
    public String workerFindPassword() {
        return "/homePage/worker/workerFindPassword";
    }

    @ResponseBody
    @PostMapping("/worker/findPassword/submit")
    public Map<String, String> workerFindPasswordSubmit(HttpSession session, HttpServletRequest request) {
        String code = session.getAttribute("code").toString();
        String tryCode = request.getParameter("vercode");
        HashMap<String, String> map = new HashMap<>();
        if (code.equals(tryCode)) {
            map.put("success", "验证成功！");
        } else {
            map.put("error", "短信验证码错误！");
        }
        return map;
    }

    @GetMapping("/worker/changePassword")
    public String workerChangePassword() {
        return "/homePage/worker/changePassword";
    }

    @ResponseBody
    @PostMapping("/worker/changePassword/submit")
    public Map<String, String> workerChangePasswordSubmit(HttpSession session, HttpServletRequest request) {
        HashMap<String, String> map = new HashMap<>();
        int userId = Integer.parseInt(session.getAttribute("changeId").toString());
        String password = request.getParameter("password");
        workerService.changePassword(password, userId);
        map.put("success", "密码更改成功！");
        return map;
    }
}


