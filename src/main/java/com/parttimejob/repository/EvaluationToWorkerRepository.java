package com.parttimejob.repository;

import com.parttimejob.entity.EvaluationToWorker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-03-03 16:47
 * @Description:
 */
public interface EvaluationToWorkerRepository extends JpaRepository<EvaluationToWorker,Integer>
{
    List<EvaluationToWorker> findByManagerId(int id);

    List<EvaluationToWorker> findByWorkerId(int id);

    EvaluationToWorker findByManagerIdAndWorkerId(int managerId,int workerId);

    EvaluationToWorker findById(int id);
}
