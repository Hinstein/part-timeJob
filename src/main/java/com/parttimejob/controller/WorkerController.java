package com.parttimejob.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.parttimejob.entity.Worker;
import com.parttimejob.repository.WorkerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    WorkerRepository workerRepository;

    @GetMapping("/worker")
    public Worker inserWorker(Worker worker) {
        Worker save = workerRepository.save(worker);
        return save;
    }

    @ResponseBody
    @PostMapping("/worker/register")
    public String registerWorker(Worker worker) {
        Worker worker1 = workerRepository.findByUserName(worker.getUserName());
        if (worker1 != null) {
            return "注册失败，存在该用户";
        }
        workerRepository.save(worker);
        return "注册成功";
    }

    @ResponseBody
    @PostMapping("/worker/login")
    public String workerLogin(Worker worker, HttpSession session) {

        String username = worker.getUserName();
        String password = worker.getPassword();
        Worker worker1 = workerRepository.findByUserName(username);
        if (worker1 != null) {
            System.out.println("存在用户");
            if (worker1.getPassword().equals(password)) {
                System.out.println("登录成功");
                session.setAttribute("id", worker1.getId());
                session.setAttribute("username", worker1.getUserName());
                return "登录成功";
            }
            return "密码错误";
        }
        System.out.println("失败");
        return "不存在该用户";
    }

    @GetMapping("/worker/editor")
    public String workerEditor(HttpSession session, Model model) {
        String username = session.getAttribute("username").toString();
        Worker worker =workerRepository.findByUserName(username);
        model.addAttribute("worker", worker);
        return "worker/editor";
    }

    @GetMapping("/worker/index")
    public String workerIndex() {
        return "worker/index";
    }

    @ResponseBody
    @PostMapping("/worker/editor/save")
    public String workerEditorSave(Worker worker) {
        workerRepository.updateEditor(worker);
        return "保存成功";
    }

    @GetMapping("/worker/resume")
    public String workerResume() {
        return "worker/resume";
    }
}


