package com.parttimejob.repository;

import com.parttimejob.entity.Employ;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-02-26 20:48
 * @Description:
 */
public interface EmployRepository extends JpaRepository<Employ, Integer> {
    Employ findByWorkerIdAndJobIdAndManagerId(int workerId, int jobId, int managerId);

    List<Employ> findByManagerId(int managerId);

    List<Employ> findByJobId(int jobId);
}
