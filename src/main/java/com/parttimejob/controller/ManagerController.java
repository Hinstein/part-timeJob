package com.parttimejob.controller;

import com.parttimejob.entity.Deliver;
import com.parttimejob.entity.Manager;
import com.parttimejob.entity.Worker;
import com.parttimejob.service.DeliverService;
import com.parttimejob.service.ManagerService;
import com.parttimejob.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            System.out.println("存在用户");
            if (manager1.getPassword().equals(password)) {
                if(manager1.getAudit()==0)
                {
                    return "该账号正在审核中，请等待的管理员审核";
                }
                System.out.println("登录成功");
                session.setAttribute("id", manager1.getId());
                session.setAttribute("userName", manager1.getUserName());
                return "登录成功";
            }
            return "密码错误";
        }
        System.out.println("失败");
        return "不存在该用户";
    }

    @GetMapping("/manager/index")
    public String managerIndex() {
        return "manager/index";
    }

    @GetMapping("/manager/search")
    public String managerSearch(){
        return "manager/search";
    }

    @GetMapping("/manager/job/deliver/{id}")
    public String jobDeliver(@PathVariable("id") int jobId, Model model){
        List<Deliver> delivers= deliverService.findByJobId(jobId);
        List<Worker> workers =new ArrayList<>();
        for(Deliver w:delivers){
            Worker worker =workerService.findById(w.getWorkerId());
            workers.add(worker);
        }
        model.addAttribute("workers",workers);
        return "manager/deliver";
    }

}
