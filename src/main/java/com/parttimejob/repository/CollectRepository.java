package com.parttimejob.repository;

import com.parttimejob.entity.Collect;
import com.parttimejob.entity.Job;
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
public interface CollectRepository extends JpaRepository<Collect, Integer> {

    /**
     * 通过兼职者id和工作找到收藏信息
     *
     * @param workerId
     * @param jobId
     * @return
     */
    Collect findByWorkerIdAndJobId(int workerId, int jobId);

    /**
     * 通过兼职者id和工作id删除收藏信息
     *
     * @param workerId
     * @param jobId
     */
    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "delete from Collect a where a.workerId = ?1 and a.jobId=?2 ")
    void deleteByWorkerIdAndJobId(int workerId, int jobId);

    List<Collect> findByWorkerId(int workerId);

    /**
     * 通过兼职者id删除收藏信息
     *
     * @param id
     */
    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "delete from Collect a where a.workerId =?1")
    void deleteCollectByWorkerId(int id);

    /**
     * 通过工作id删除工作信息
     *
     * @param id
     */
    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "delete from Collect a where a.jobId =?1")
    void deleteCollectByJobId(int id);

}
