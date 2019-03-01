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
    CollectRepository collectRepository;

    /**
     * 保存搜藏
     * @param collect
     */
    public void save(Collect collect) {
        collectRepository.save(collect);
    }

    /**
     * 通过兼职者id和工作id找到收藏
     * @param workerId
     * @param jobId
     * @return
     */
    public Collect findByWorkerIdAndJobId(int workerId, int jobId) {
        return collectRepository.findByWorkerIdAndJobId(workerId, jobId);
    }

    /**
     * 通过兼职者id和工作id删除收藏
     * @param workerId
     * @param jobId
     */
    public void delete(int workerId, int jobId) {
        collectRepository.deleteByWorkerIdAndJobId(workerId, jobId);
    }

    /**
     * 通过兼职者id找到该兼职者的所有收藏信息
     * @param workerId
     * @return
     */
    public List<Collect> findByWorkerId(int workerId) {
        return collectRepository.findByWorkerId(workerId);
    }

    /**
     * 通过兼职者id删除收藏信息
     * @param workerId
     */
    public void deleteCollectByWorkerId(int workerId){
        collectRepository.deleteCollectByWorkerId(workerId);
    }

    /**
     * 通过工作id删除收藏信息
     * @param jobId
     */
    public void deleteCollectByJobId(int jobId){
        collectRepository.deleteCollectByJobId(jobId);
    }

}
