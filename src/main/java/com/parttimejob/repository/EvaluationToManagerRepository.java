package com.parttimejob.repository;

import com.parttimejob.entity.EvaluationToManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-03-09 12:54
 * @Description:
 */

public interface EvaluationToManagerRepository extends JpaRepository<EvaluationToManager, Integer> {
    List<EvaluationToManager> findByManagerId(int id);


    List<EvaluationToManager> findByWorkerId(int id);

    EvaluationToManager findByManagerIdAndWorkerId(int managerId, int workerId);

    EvaluationToManager findById(int id);
}
