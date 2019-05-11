package com.parttimejob.service;

import com.parttimejob.entity.Worker;
import com.parttimejob.repository.WorkerRepository;
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
 * @CreateTime: 2019-02-08 16:13
 * @Description:
 */
@Service
public class WorkerService {

    @Autowired
    WorkerRepository workerRepository;

    @Autowired
    CollectService collectService;

    @Autowired
    DeliverService deliverService;

    @Autowired
    EmployService employService;

    @Autowired
    WorkerDataService workerDataService;

    /**
     * 通过用户名找到兼职者
     *
     * @param userName
     * @return
     */
    public Worker findByUserName(String userName) {
        return workerRepository.findByUserName(userName);
    }

    /**
     * 保存兼职者
     *
     * @param worker
     */
    public void save(Worker worker) {
        workerRepository.save(worker);
    }

    /**
     * 通过id找到兼职者
     *
     * @param id
     * @return
     */
    public Worker findById(int id) {
        return workerRepository.findById(id);
    }

    /**
     * 分页得到所有兼职者的资料
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Transactional(rollbackOn = Exception.class)
    public Page<Worker> getWorkers(int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return workerRepository.findAll(pageable);
    }

    /**
     * 删除兼职者，并且删除有关该兼职者的收藏，投递，雇佣，个人资料
     *
     * @param id
     */
    public void deleteById(int id) {
        workerDataService.deleteWorkerDataByWorkerId(id);
        employService.deleteEmployByWorkerId(id);
        deliverService.deleteDeliverByWorkerId(id);
        collectService.deleteCollectByWorkerId(id);
        workerRepository.deleteWorkerById(id);
    }

    public List<Worker> findAll() {
        return workerRepository.findAll();
    }

    public void changePassword(String password, int id) {
        workerRepository.changePassword(password, id);
    }
}
