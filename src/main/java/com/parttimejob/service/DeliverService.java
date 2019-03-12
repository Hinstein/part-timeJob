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

    /**
     * 保存投递
     *
     * @param deliver
     */
    public void save(Deliver deliver) {
        deliverRepository.save(deliver);
    }

    /**
     * 通过兼职者的id和工作的id找到投递
     *
     * @param workerId
     * @param jobId
     * @return
     */
    public Deliver findByWorkerIdAndJobId(int workerId, int jobId) {
        return deliverRepository.findByWorkerIdAndJobId(workerId, jobId);
    }

    /**
     * 通过兼职者id删除所有投递
     *
     * @param workerId
     * @param jobId
     */
    public void delete(int workerId, int jobId) {
        deliverRepository.deleteByWorkerIdAndJobId(workerId, jobId);
    }

    /**
     * 通过兼职者id找到投递信息
     *
     * @param workerId
     * @return
     */
    public List<Deliver> findByWorkerId(int workerId) {
        return deliverRepository.findByWorkerId(workerId);
    }

    /**
     * 通过工作id找到投递信息
     *
     * @param jobId
     * @return
     */
    public List<Deliver> findByJobId(int jobId) {
        return deliverRepository.findByJobId(jobId);
    }

    /**
     * 通过兼职者id删除投递信息
     *
     * @param workerId
     */
    public void deleteDeliverByWorkerId(int workerId) {
        deliverRepository.deleteDeliverByWorkerId(workerId);
    }

    /**
     * 通过兼职者id删除投递信息
     *
     * @param jobId
     */
    public void deleteDeliverByJobId(int jobId) {
        deliverRepository.deleteDeliverByJobId(jobId);
    }
}
