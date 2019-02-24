package com.parttimejob.service;


import com.parttimejob.entity.Job;
import com.parttimejob.entity.Manager;
import com.parttimejob.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.service
 * @Author: Hinstein
 * @CreateTime: 2019-02-18 12:20
 * @Description:
 */

@Service
public class JobService {
    @Autowired
    JobRepository jobRepository;

    public void jobSave(Job job) {
        jobRepository.save(job);
    }

    public Job findById(int id) {
        return jobRepository.findById(id);
    }

    public void updateEditor(Job job) {
        jobRepository.updateEditor(job);
    }

    public void delete(int id) {
        jobRepository.deleteById(id);
    }

    public List<Job> findByNameLike(String content) {
        return jobRepository.findByContentLike(content);
    }

    public List<Job> findByManagerId(int id)
    {
        return jobRepository.findByManagerId(id);
    }

    @Transactional
    public Page<Job> getJobs(int pageNo, int pageSize) {
        if(pageNo==0){
            pageNo=1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return jobRepository.findAll(pageable);
    }

}