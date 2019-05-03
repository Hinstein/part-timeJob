package com.parttimejob.controller;

import com.parttimejob.entity.Job;
import com.parttimejob.entity.Manager;
import com.parttimejob.service.JobService;
import com.parttimejob.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.controller
 * @Author: Hinstein
 * @CreateTime: 2019-01-22 11:14
 * @Description:
 */

@Controller
public class MainController {

    @Autowired
    JobService jobService;

    @Autowired
    ManagerService managerService;

    /**
     * 兼职者登录页面
     *
     * @return
     */
    @GetMapping("/workerLogin")
    public String workerLogin() {
        return "/homePage/workerLogin";
    }

    /**
     * 招聘者登录页面
     *
     * @return
     */
    @GetMapping("/managerLogin")
    public String managerLogin() {
        return "/homePage/managerLogin";
    }

    /**
     * 兼职者注册页面
     *
     * @return
     */
    @GetMapping("/workerRegister")
    public String workerRegister() {
        return "/homePage/workerRegister";
    }

    /**
     * 招聘者注册页面
     *
     * @return
     */
    @GetMapping("/managerRegister")
    public String managerRegister() {
        return "/homePage/managerRegister";
    }

    /**
     * 招聘网主页
     *
     * @return
     */
    @GetMapping("/index")
    public String index(Model model) {
        Page<Job> jobs = jobService.getJobs(1, 27);
        Page<Job> jobs1 = jobService.finDescByTime(1, 27);
        Page<Job> jobs2 = jobService.finDescByViews(1, 27);
        model.addAttribute("jobs", jobs);
        model.addAttribute("jobs1", jobs1);
        model.addAttribute("jobs2", jobs2);
        return "/homePage/index";
    }

    /**
     * 后台登录页面
     *
     * @return
     */
    @GetMapping("/admin/login")
    public String adminLogin() {
        return "/admin/login";
    }


    @GetMapping("/manager/information")
    public String managerInformation() {
        return "/homePage/managerInformation";
    }

    @ResponseBody
    @PostMapping("/addPhoto")
    public Map<String, Object> addPhoto(@RequestParam("file") MultipartFile file, HttpSession session, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        //通过session查看当前登录的用户信息
        Manager manager = (Manager) session.getAttribute("registerManager");
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


    @ResponseBody
    @PostMapping("/manager/register/vendor")
    public Map<String, String> vendorInformation(Manager manager, HttpSession session) {
        Manager registerManager = (Manager) session.getAttribute("registerManager");
        HashMap<String, String> map = new HashMap<>();
        if (registerManager != null) {
            if (registerManager.getDatePath() == null) {
                map.put("error", "请先上传商家许可证");
            } else {
                registerManager.setVendorName(manager.getVendorName());
                registerManager.setAddress(manager.getAddress());
                registerManager.setVendorTime(manager.getVendorTime());
                session.removeAttribute("registerManager");
                managerService.save(registerManager);
                map.put("success", "添加成功，请等待管理员审批");
            }

        } else {
            map.put("error", "您以注册，请等待管理员审批");
        }
        return map;
    }
}
