package com.parttimejob.service;

import com.parttimejob.entity.Deliver;
import com.parttimejob.repository.DeliverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.service
 * @Author: Hinstein
 * @CreateTime: 2019-02-25 16:05
 * @Description:
 */
@Service
public class DeliverService {

    @Autowired
    DeliverRepository deliverRepository;

    public void save(Deliver deliver) {
        deliverRepository.save(deliver);
    }

    public Deliver findByWorkerIdAndJobId(int workerId, int jobId) {
        return deliverRepository.findByWorkerIdAndJobId(workerId, jobId);
    }

    public void delete(int workerId, int jobId) {
        deliverRepository.deleteByWorkerIdAndJobId(workerId, jobId);
    }

    public List<Deliver> findByWorkerId(int workerId) {
        return deliverRepository.findByWorkerId(workerId);
    }

    public List<Deliver> findByJobId(int jobId) {
        return deliverRepository.findByJobId(jobId);
    }
}
