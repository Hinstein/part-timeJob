package com.parttimejob.controller;

import com.parttimejob.entity.Worker;
import com.parttimejob.entity.WorkerData;
import com.parttimejob.service.WorkerDataService;
import com.parttimejob.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
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


    @ResponseBody
    @PostMapping("/worker/register")
    public String registerWorker(Worker worker) {
        Worker worker1 = workerService.findByUserName(worker.getUserName());
        if (worker1 != null) {
            return "注册失败，存在该用户";
        }
        workerService.save(worker);
        WorkerData workerData =new WorkerData();
        workerData.setWorkerId(worker.getId());
        workerDataService.save(workerData);
        return "注册成功";
    }

    @ResponseBody
    @PostMapping("/worker/login")
    public String workerLogin(Worker worker, HttpSession session) {
        String username = worker.getUserName();
        String password = worker.getPassword();
        Worker worker1 = workerService.findByUserName(username);
        if (worker1 != null) {
            System.out.println("存在用户");
            if (worker1.getPassword().equals(password)) {
                System.out.println("登录成功");
                session.setAttribute("id", worker1.getId());
                session.setAttribute("userName", worker1.getUserName());
                return "登录成功";
            }
            return "密码错误";
        }
        System.out.println("失败");
        return "不存在该用户";
    }

    @GetMapping("/worker/editor")
    public String workerEditor(HttpSession session, Model model) {

        return "worker/editor";
    }

    @GetMapping("/worker/index")
    public String workerIndex() {
        return "worker/index";
    }


    @GetMapping("/worker/resume")
    public String workerResume(HttpSession session, Model model) {
       int id = Integer.parseInt(session.getAttribute("id").toString());
        WorkerData workerData=workerDataService.findByWorkerId(id);
        model.addAttribute("worker", workerData);
        return "worker/resume";
    }

    @ResponseBody
    @PostMapping("/worker/resume/save")
    public String workerDateSave(WorkerData workerData, HttpSession session) {
        int id = Integer.parseInt(session.getAttribute("id").toString());
        workerData.setWorkerId(id);
        workerDataService.updata(workerData);
        return "保存成功";
    }

//    @ResponseBody
//    @PostMapping("/worker/editor/save")
//    public Map<String, String> changePassword(@RequestParam("newPassword") String newPassword,
//                                              @RequestParam("oldPassword") String oldPassword,
//                                              HttpSession session) {
//        Map<String, String> map = new HashMap<>(50);
//        System.out.println("sss");
//        String username = (String) session.getAttribute("userName");
//        Worker worker =workerRepository.findByUserName(username);
//        if (worker.getPassword().equals(oldPassword)) {
//            worker.setPassword(newPassword);
//            map.put("msg", "更新密码成功！");
//            workerRepository.save(worker);
//        } else if (newPassword.equals("") || oldPassword.equals("")) {
//            map.put("msg", "密码不能为空");
//        } else {
//            map.put("msg", "旧密码错误");
//        }
//        return map;
//    }

    @ResponseBody
    @PostMapping("/worker/editor/save")
    public String changePassword(@RequestParam("newPassword") String newPassword,
                                              @RequestParam("oldPassword") String oldPassword,
                                              HttpSession session) {
        Map<String, String> map = new HashMap<>(50);
        String username = (String) session.getAttribute("userName");
        Worker worker =workerService.findByUserName(username);
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

    @GetMapping("/worker/search")
    public String workerSearch(){
        return "worker/search";
    }


}


