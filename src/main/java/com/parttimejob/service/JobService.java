package com.parttimejob.service;


import com.parttimejob.entity.Job;
import com.parttimejob.entity.Manager;
import com.parttimejob.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void jobSave(Job job){
        jobRepository.save(job);
    }

}
