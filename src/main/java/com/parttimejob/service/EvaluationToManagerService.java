package com.parttimejob.service;

import com.parttimejob.entity.EvaluationToManager;
import com.parttimejob.repository.EvaluationToManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.service
 * @Author: Hinstein
 * @CreateTime: 2019-03-09 12:55
 * @Description:
 */


@Service
public class EvaluationToManagerService {

    @Autowired
    EvaluationToManagerRepository evaluationToManagerRepository;

    public void evaluationToManagerSave(EvaluationToManager evaluation) {
        evaluationToManagerRepository.save(evaluation);
    }

    public List<EvaluationToManager> findByManagerId(int id) {
        return evaluationToManagerRepository.findByManagerId(id);
    }

    public EvaluationToManager findByManagerIdAndWorkerId(int managerId, int workerId) {
        return evaluationToManagerRepository.findByManagerIdAndWorkerId(managerId, workerId);
    }

    public List<EvaluationToManager> findByWorkerId(int id) {
        return evaluationToManagerRepository.findByWorkerId(id);
    }

    public EvaluationToManager findById(int id) {
        return evaluationToManagerRepository.findById(id);
    }
}

