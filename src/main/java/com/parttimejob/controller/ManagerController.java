package com.parttimejob.controller;

import com.parttimejob.entity.Manager;
import com.parttimejob.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    ManagerRepository managerRepository;

    @ResponseBody
    @PostMapping("/manager/register")
    public String registerWorker(Manager manager) {
        Manager manager1 =managerRepository.findByUserName(manager.getUserName());
        if (manager1!=null){
            return "注册失败，存在该用户";
        }
        managerRepository.save(manager);
        return "注册成功";
    }
}
