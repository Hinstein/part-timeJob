package com.parttimejob.controller;

import com.parttimejob.entity.Admin;
import com.parttimejob.entity.Manager;
import com.parttimejob.entity.Worker;
import com.parttimejob.repository.AdminRepository;
import com.parttimejob.repository.ManagerRepository;
import com.parttimejob.service.AdminService;
import com.parttimejob.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import javax.servlet.http.HttpSession;
import java.util.List;

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

    @PostMapping("admin/login")
    @ResponseBody
    public String adminLogin(Admin admin, HttpSession session) {
        String username = admin.getUserName();
        String password = admin.getPassword();
        Admin admin1 = adminService.findByUserName(username);
        if (admin1 != null) {
            if (admin1.getPassword().equals(password)) {
                session.setAttribute("adminUserName", admin1.getUserName());
                System.out.println("登录成功！");
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
    public String managerAudit(Model model, @RequestParam(name = "pageNumber", required = false, defaultValue = "1") String startPage) {

        int pageNumber = 1;
        try {
            //对pageNumber的校验
            pageNumber = Integer.parseInt(startPage);
            if (pageNumber < 1) {
                pageNumber = 1;
            }

        } catch (Exception e) {
        }

        Page<Manager> managers = managerService.getManagers(pageNumber, 10);

        if (pageNumber > managers.getTotalPages()) {
            pageNumber = managers.getTotalPages();
            managers = managerService.getManagers(pageNumber, 10);
        }
        model.addAttribute("managers", managers);

        return "admin/manager/audit";
    }

    @GetMapping("admin/manager/audit/delete/{id}")
    public String deleteManager(@PathVariable("id") Integer id) {
        managerService.deleteManagerById(id);

        return "redirect:/admin/manager/audit";
    }

    @GetMapping("admin/manager/audit/pass/{id}")
    public String passManager(@PathVariable("id") Integer id) {

        managerService.passManager(id);

        return "redirect:/admin/manager/audit";
    }


}
