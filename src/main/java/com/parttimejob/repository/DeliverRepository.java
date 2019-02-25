package com.parttimejob.repository;

import com.parttimejob.entity.Deliver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-02-25 16:04
 * @Description:
 */
public interface DeliverRepository extends JpaRepository<Deliver,Integer> {
    Deliver findByWorkerIdAndJobId(int workerId, int jobId);

    @Transactional
    @Modifying
    @Query(value = "delete from Deliver a where a.workerId = ?1 and a.jobId=?2 ")
    void deleteByWorkerIdAndJobId(int workerId, int jobId);

    List<Deliver> findByWorkerId(int workerId);

    List<Deliver> findByJobId(int jobId);
}
