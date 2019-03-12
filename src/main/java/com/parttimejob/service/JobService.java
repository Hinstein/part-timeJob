package com.parttimejob.service;


import com.parttimejob.entity.Job;
import com.parttimejob.entity.Manager;
import com.parttimejob.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Autowired
    CollectService collectService;

    @Autowired
    DeliverService deliverService;

    @Autowired
    EmployService employService;

    /**
     * 保存该工作
     *
     * @param job
     */
    public void jobSave(Job job) {
        jobRepository.save(job);
    }

    /**
     * 通过id找到工作
     *
     * @param id
     * @return
     */
    public Job findById(int id) {
        return jobRepository.findById(id);
    }

    /**
     * 更新该工作信息
     *
     * @param job
     */
    public void updateEditor(Job job) {
        jobRepository.updateEditor(job);
    }

    /**
     * 通过id删除该工作
     *
     * @param id
     */
    public void deleteById(int id) {
        collectService.deleteCollectByJobId(id);
        deliverService.deleteDeliverByJobId(id);
        employService.deleteEmployByJobId(id);
        jobRepository.deleteById(id);
    }

    /**
     * 通过招聘者的id找到所有的工作
     *
     * @param id
     * @return
     */
    public List<Job> findByManagerId(int id) {
        return jobRepository.findByManagerId(id);
    }

    /**
     * 分页得到job的模糊查询
     *
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
     *
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

    /**
     * 通过招聘者id删除工作
     */
    public void deleteJobByManagerId(int managerId) {
        jobRepository.deleteJobByManagerId(managerId);
    }

    public Page<Job> findByManagerId(int id, int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return jobRepository.findByManagerId(id, pageable);
    }

    @Transactional
    public Page<Job> finDescByTime(int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return jobRepository.findAll(pageable);
    }

    @Transactional
    public Page<Job> finDescByViews(int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        Sort sort = new Sort(Sort.Direction.DESC, "views");
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return jobRepository.findAll(pageable);
    }

    public void collectionSave(int id) {
        jobRepository.collectionSave(id);
    }

    public void deliverSave(int id) {
        jobRepository.deliverSave(id);
    }

    public void collectionCancel(int id) {
        jobRepository.collectionCancel(id);
    }

    public void deliverCancel(int id) {
        jobRepository.deliverCancel(id);
    }

    public List<Job> findAll() {
        return jobRepository.findAll();
    }
}
