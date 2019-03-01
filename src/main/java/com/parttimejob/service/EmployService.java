package com.parttimejob.service;

import com.parttimejob.entity.Collect;
import com.parttimejob.entity.Employ;
import com.parttimejob.repository.EmployRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.service
 * @Author: Hinstein
 * @CreateTime: 2019-02-26 20:49
 * @Description:
 */
@Service
public class EmployService {

    @Autowired
    EmployRepository employRepository;

    /**
     * 保存员工
     * @param employ
     */
    public void save(Employ employ) {
        employRepository.save(employ);
    }

    /**
     * 通过兼职者的id，工作的id，招聘者的id找到雇佣信息
     * @param workerId
     * @param jobId
     * @param managerId
     * @return
     */
    public Employ findByWorkerIdAndJobIdAndManagerId(int workerId, int jobId, int managerId) {
        return employRepository.findByWorkerIdAndJobIdAndManagerId(workerId, jobId, managerId);
    }

    /**
     * 通过工作的id找到雇佣信息
     * @param jobId
     * @return
     */
    public List<Employ> findByJobId(int jobId) {
        return employRepository.findByJobId(jobId);
    }

    /**
     * 通过招聘者的id找到雇佣信息
     * @param managerId
     * @return
     */
    public List<Employ> findByManagerId(int managerId) {
        return employRepository.findByManagerId(managerId);
    }

    /**
     * 通过兼职者的id删除雇佣信息
     * @param workerId
     */
    public void deleteEmployByWorkerId(int workerId){
        employRepository.deleteEmployByWorkerId(workerId);
    }

    /**
     * 通过招聘者的id删除雇佣信息
     * @param managerId
     */
    public void deleteEmployByManagerId(int managerId){
        employRepository.deleteEmployByManagerId(managerId);
    }


    /**
     * 通过工作的id删除雇佣信息
     * @param managerId
     */
    public void deleteEmployByJobId(int jobId){
        employRepository.deleteEmployByJobId(jobId);
    }

}
