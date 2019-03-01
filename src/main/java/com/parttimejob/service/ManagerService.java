package com.parttimejob.service;

import com.parttimejob.entity.Manager;
import com.parttimejob.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    /**
     * 分页查找出未通过审核的招聘者
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
     * @param id
     */
    public void deleteManagerById(int id) {
        managerRepository.deleteManagerById(id);
    }

    /**
     * 该招聘者通过审核
     * @param id
     */
    public void passManager(int id) {
        managerRepository.passManager(id);
    }

    /**
     * 通过用户名找到招聘者
     * @param userName
     * @return
     */
    public Manager findByUserName(String userName) {
        return managerRepository.findByUserName(userName);
    }

    /**
     * 保存招聘者
     * @param manager
     */
    public void save(Manager manager) {
        managerRepository.save(manager);
    }

    /**
     * 通过id找到招聘者
     * @param id
     * @return
     */
    public Manager findById(int id) {
        return managerRepository.findById(id);
    }

    /**
     * 保存招聘者的资料
     * @param manager
     */
    public void saveEditor(Manager manager) {
        managerRepository.saveEditor(manager);
    }

    /**
     * 分页查找出所有招聘者
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
        return managerRepository.findAll( pageable);
    }
}
