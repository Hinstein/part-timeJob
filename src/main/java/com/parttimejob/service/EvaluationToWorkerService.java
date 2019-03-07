package com.parttimejob.service;

import com.parttimejob.entity.EvaluationToWorker;
import com.parttimejob.repository.EvaluationToWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.service
 * @Author: Hinstein
 * @CreateTime: 2019-03-03 16:48
 * @Description:
 */
@Service
public class EvaluationToWorkerService {

    @Autowired
    EvaluationToWorkerRepository evaluationToWorkerRepository;

    public void evaluationToWorkerSave(EvaluationToWorker evaluation) {
        evaluationToWorkerRepository.save(evaluation);
    }

    public List<EvaluationToWorker> findByManagerId(int id) {
        return evaluationToWorkerRepository.findByManagerId(id);
    }

    public EvaluationToWorker findByManagerIdAndWorkerId(int managerId, int workerId) {
        return evaluationToWorkerRepository.findByManagerIdAndWorkerId(managerId, workerId);
    }

    public List<EvaluationToWorker> findByWorkerId(int id) {
        return evaluationToWorkerRepository.findByWorkerId(id);
    }
}
