package com.parttimejob.service;

import com.parttimejob.entity.Job;
import com.parttimejob.entity.Manager;
import com.parttimejob.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.service
 * @Author: Hinstein
 * @CreateTime: 2019-02-07 18:30
 * @Description:
 */

@Service
public class ManagerService {

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    CollectService collectService;

    @Autowired
    DeliverService deliverService;

    @Autowired
    EmployService employService;

    @Autowired
    JobService jobService;

    /**
     * 分页查找出未通过审核的招聘者
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Transactional
    public Page<Manager> findByAudit(int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return managerRepository.findByAudit(0, pageable);
    }

    /**
     * 通过招聘者的id删除
     *
     * @param managerId
     */
    public void deleteManagerById(int managerId) {
        List<Job> jobs = jobService.findByManagerId(managerId);
        for (Job j : jobs) {
            collectService.deleteCollectByJobId(j.getId());
            deliverService.deleteDeliverByJobId(j.getId());
        }
        employService.deleteEmployByManagerId(managerId);
        jobService.deleteJobByManagerId(managerId);
        managerRepository.deleteManagerById(managerId);
    }

    /**
     * 该招聘者通过审核
     *
     * @param id
     */
    public void passManager(int id) {
        managerRepository.passManager(id);
    }

    /**
     * 通过用户名找到招聘者
     *
     * @param userName
     * @return
     */
    public Manager findByUserName(String userName) {
        return managerRepository.findByUserName(userName);
    }

    /**
     * 保存招聘者
     *
     * @param manager
     */
    public void save(Manager manager) {
        managerRepository.save(manager);
    }

    /**
     * 通过id找到招聘者
     *
     * @param id
     * @return
     */
    public Manager findById(int id) {
        return managerRepository.findById(id);
    }

    /**
     * 保存招聘者的资料
     *
     * @param manager
     */
    public void saveEditor(Manager manager) {
        managerRepository.saveEditor(manager);
    }

    /**
     * 分页查找出所有招聘者
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Transactional
    public Page<Manager> findAll(int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return managerRepository.findAll(pageable);
    }

    public void informationSave(Manager manager) {
        managerRepository.informationSave(manager);
    }

    public void adminSave(Manager manager) {
        managerRepository.adminSave(manager);
    }

    public List<Manager> findAll() {
        return managerRepository.findAll();
    }

    public void active(int id) {
        managerRepository.active(id);
    }

    @Transactional
    public Page<Manager> findManagers(int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        Sort sort = new Sort(Sort.Direction.DESC, "active");
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return managerRepository.findAll(pageable);
    }


    public void headPhotoEditor(String src, int id) {
        managerRepository.headPhotoEditor(src, id);
    }
}
