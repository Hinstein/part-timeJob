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

    /**
     * 保存该工作
     * @param job
     */
    public void jobSave(Job job) {
        jobRepository.save(job);
    }

    /**
     * 通过id找到工作
     * @param id
     * @return
     */
    public Job findById(int id) {
        return jobRepository.findById(id);
    }

    /**
     * 更新该工作信息
     * @param job
     */
    public void updateEditor(Job job) {
        jobRepository.updateEditor(job);
    }

    /**
     * 通过id删除该工作
     * @param id
     */
    public void delete(int id) {
        jobRepository.deleteById(id);
    }

    /**
     * 通过招聘者的id找到所有的工作
     * @param id
     * @return
     */
    public List<Job> findByManagerId(int id) {
        return jobRepository.findByManagerId(id);
    }

    /**
     * 分页得到job的模糊查询
     * @param content
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Transactional
    public Page<Job> findByTitleLike(String content, int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return jobRepository.findByTitleLike(content, pageable);
    }

    /**
     * 分页得到所有工作
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Transactional
    public Page<Job> getJobs(int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return jobRepository.findAll(pageable);
    }

}
