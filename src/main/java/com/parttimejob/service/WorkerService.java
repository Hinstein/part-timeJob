package com.parttimejob.service;

import com.parttimejob.entity.Worker;
import com.parttimejob.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.service
 * @Author: Hinstein
 * @CreateTime: 2019-02-08 16:13
 * @Description:
 */
@Service
public class WorkerService {

    @Autowired
    WorkerRepository workerRepository;

    public Worker findByUserName(String userName) {
        return workerRepository.findByUserName(userName);
    }

    public void save(Worker worker) {
        workerRepository.save(worker);
    }

    public Worker findById(int id) {
        return workerRepository.findById(id);
    }

    @Transactional
    public Page<Worker> getWorkers(int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return workerRepository.findAll(pageable);
    }
}
