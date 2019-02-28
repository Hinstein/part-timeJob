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

    @Transactional
    public Page<Manager> getManagers(int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return managerRepository.findByAudit(0, pageable);
    }

    public void deleteManagerById(int id) {
        managerRepository.deleteManagerById(id);
    }

    public void passManager(int id) {
        managerRepository.passManager(id);
    }

    public Manager findByUserName(String userName) {
        return managerRepository.findByUserName(userName);
    }

    public void save(Manager manager) {
        managerRepository.save(manager);
    }

    public Manager findById(int id) {
        return managerRepository.findById(id);
    }


}
