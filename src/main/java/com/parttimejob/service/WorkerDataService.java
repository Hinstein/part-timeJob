package com.parttimejob.service;


import com.parttimejob.entity.Manager;
import com.parttimejob.entity.WorkerData;
import com.parttimejob.repository.WorkerDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.service
 * @Author: Hinstein
 * @CreateTime: 2019-02-08 16:13
 * @Description:
 */
@Service
public class WorkerDataService {

    @Autowired
    WorkerDataRepository workerDataRepository;

    /**
     * 通过兼职者的id，找到该兼职者的资料
     *
     * @param id
     * @return
     */
    public WorkerData findByWorkerId(int id) {
        return workerDataRepository.findByWorkerId(id);
    }

    /**
     * 保存兼职者资料
     *
     * @param workerData
     */
    public void save(WorkerData workerData) {
        workerDataRepository.save(workerData);
    }


    /**
     * 更新该兼职者的资料
     *
     * @param workerData
     */
    public void updata(WorkerData workerData) {
        workerDataRepository.update(workerData);
    }

    /**
     * 分页得到兼职者的资料
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Transactional
    public Page<WorkerData> findAll(int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return workerDataRepository.findAll(pageable);
    }

    /**
     * 通过兼职者的id删除兼职者的资料
     *
     * @param workerId
     */
    public void deleteWorkerDataByWorkerId(int workerId) {
        workerDataRepository.deleteWorkerDataByWorkerId(workerId);
    }

    @Transactional
    public Page<WorkerData> findByJobIntensionLike(String content, int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return workerDataRepository.findByJobIntensionLike(content, pageable);
    }

    public void active(int id) {
        workerDataRepository.active(id);
    }

    @Transactional
    public Page<WorkerData> workerActive(int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        Sort sort = new Sort(Sort.Direction.DESC, "active");
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return workerDataRepository.findAll(pageable);
    }

    public void headPhotoEditor(String src, String pathName,int id) {
        workerDataRepository.headPhotoEditor(src, pathName,id);
    }


    public int workerGradeOne(){
        return workerDataRepository.countByEducationBackground("大一");
    }

    public int workerGradeTwo (){
        return workerDataRepository.countByEducationBackground("大二");
    }

    public int workerGradeThree(){
        return workerDataRepository.countByEducationBackground("大三");
    }

    public int workerGradeFour(){
        return workerDataRepository.countByEducationBackground("大四");
    }
}
