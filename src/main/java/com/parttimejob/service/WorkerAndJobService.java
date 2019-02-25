package com.parttimejob.service;

import com.parttimejob.entity.WorkerAndJob;
import com.parttimejob.repository.WorkerAndJobRepository;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.service
 * @Author: Hinstein
 * @CreateTime: 2019-02-25 14:38
 * @Description:
 */
@Service
public class WorkerAndJobService {

    @Autowired
    WorkerAndJobRepository workerAndJobRepository;

    public void save(WorkerAndJob workerAndJob) {
        workerAndJobRepository.save(workerAndJob);
    }

    public WorkerAndJob findByWorkerIdAndJobId(int workerId, int jobId){
        return workerAndJobRepository.findByWorkerIdAndJobId(workerId,jobId);
    }

    public void delete(int workerId, int jobId){
        workerAndJobRepository.deleteByWorkerIdAndJobId(workerId,jobId);
    }

    public List<WorkerAndJob> findbyWorkerId(int workerId){
        return workerAndJobRepository.findByWorkerId(workerId);
    }
}
