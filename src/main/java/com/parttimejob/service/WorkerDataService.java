package com.parttimejob.service;


import com.parttimejob.entity.WorkerData;
import com.parttimejob.repository.WorkerDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public WorkerData findByWorkerId(int id) {
        return workerDataRepository.findByWorkerId(id);
    }

    public void save(WorkerData workerData) {
        workerDataRepository.save(workerData);
    }

    public void updata(WorkerData workerData) {
        workerDataRepository.update(workerData);
    }

    @Transactional
    public Page<WorkerData> findAll(int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return workerDataRepository.findAll(pageable);
    }
}
