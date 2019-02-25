package com.parttimejob.repository;

import com.parttimejob.entity.WorkerAndJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;


/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-02-25 14:37
 * @Description:
 */
public interface WorkerAndJobRepository extends JpaRepository<WorkerAndJob, Integer> {

    WorkerAndJob findByWorkerIdAndJobId(int workerId, int jobId);

    @Transactional
    @Modifying
    @Query(value = "delete from WorkerAndJob a where a.workerId = ?1 and a.jobId=?2 ")
    void deleteByWorkerIdAndJobId(int workerId, int jobId);

    List<WorkerAndJob> findByWorkerId(int workerId);
}
