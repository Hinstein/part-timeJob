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

    public void save(Employ employ) {
        employRepository.save(employ);
    }

    public Employ findByWorkerIdAndJobIdAndManagerId(int workerId, int jobId, int managerId) {
        return employRepository.findByWorkerIdAndJobIdAndManagerId(workerId, jobId, managerId);
    }

    public List<Employ> findByJobId(int jobId) {
        return employRepository.findByJobId(jobId);
    }

    public List<Employ> findByManagerId(int managerId) {
        return employRepository.findByManagerId(managerId);
    }
}
