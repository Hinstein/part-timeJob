package com.parttimejob.service;


import com.parttimejob.entity.WorkerData;
import com.parttimejob.repository.WorkerDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.service
 * @Author: Hinstein
 * @CreateTime: 2019-02-08 16:13
 * @Description:
 */
@Service
public class WorkerDataService {

    @Autowired
    WorkerDataRepository workerDataRepository;

    public WorkerData findByManagerId(int id)
    {
        return workerDataRepository.findByWorkerId(id);
    }

    public void save(WorkerData workerData){
        workerDataRepository.save(workerData);
    }
}
