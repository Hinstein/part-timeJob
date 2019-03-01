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
public interface DeliverRepository extends JpaRepository<Deliver, Integer> {
    /**
     * 通过兼职者id和工作id找到所有投递信息
     * @param workerId
     * @param jobId
     * @return
     */
    Deliver findByWorkerIdAndJobId(int workerId, int jobId);

    /**
     * 通过兼职者id和工作id删除投递信息
     * @param workerId
     * @param jobId
     */
    @Transactional
    @Modifying
    @Query(value = "delete from Deliver a where a.workerId = ?1 and a.jobId=?2 ")
    void deleteByWorkerIdAndJobId(int workerId, int jobId);

    /**
     * 通过兼职者id找到所有投递信息
     * @param workerId
     * @return
     */
    List<Deliver> findByWorkerId(int workerId);

    /**
     * 通过工作id找到所有投递信息
     * @param jobId
     * @return
     */
    List<Deliver> findByJobId(int jobId);

    /**
     * 通过兼职者id删除所有投递信息
     * @param id
     */
    @Transactional
    @Modifying
    @Query(value = "delete from Deliver a where a.workerId =?1")
    void deleteDeliverByWorkerId(int id);
}
