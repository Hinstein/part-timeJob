package com.parttimejob.service;

import com.parttimejob.entity.CollectWorker;
import com.parttimejob.repository.CollectWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.service
 * @Author: Hinstein
 * @CreateTime: 2019-03-09 15:59
 * @Description:
 */
@Service
public class CollectWorkerService {

    @Autowired
    CollectWorkerRepository collectWorkerRepository;

    public CollectWorker findByWorkerIdAndManagerId(int workerId,int managerId){
        return collectWorkerRepository.findByWorkerIdAndManagerId(workerId,managerId);
    }

    public CollectWorker save(CollectWorker collectWorker){
        return collectWorkerRepository.save(collectWorker);
    }

    public void deleteByWorkerIdAndManagerId(int workerId,int managerId){
        collectWorkerRepository.deleteByWorkerIdAndManagerId(workerId,managerId);
    }

    public List<CollectWorker> findByManagerId(int managerId){
        return collectWorkerRepository.findByManagerId(managerId);
    }
}
