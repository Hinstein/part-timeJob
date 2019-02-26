package com.parttimejob.service;

import com.parttimejob.entity.Collect;
import com.parttimejob.repository.CollectRepository;
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
public class CollectService {

    @Autowired
    CollectRepository workerAndJobRepository;

    public void save(Collect collect) {
        workerAndJobRepository.save(collect);
    }

    public Collect findByWorkerIdAndJobId(int workerId, int jobId){
        return workerAndJobRepository.findByWorkerIdAndJobId(workerId,jobId);
    }

    public void delete(int workerId, int jobId){
        workerAndJobRepository.deleteByWorkerIdAndJobId(workerId,jobId);
    }

    public List<Collect> findbyWorkerId(int workerId){
        return workerAndJobRepository.findByWorkerId(workerId);
    }
}
