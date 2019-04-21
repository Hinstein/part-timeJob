package com.parttimejob.repository;

import com.parttimejob.entity.Employ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-02-26 20:48
 * @Description:
 */
public interface EmployRepository extends JpaRepository<Employ, Integer> {
    /**
     * 通过兼职者id和招聘者id找到雇佣信息
     *
     * @param workerId
     * @param managerId
     * @return
     */
    Employ findByWorkerIdAndManagerId(int workerId, int managerId);


    /**
     * 通过招聘者id找到所有雇佣信息
     *
     * @param managerId
     * @return
     */
    List<Employ> findByManagerId(int managerId);

    /**
     * 通过工作id找到所有雇佣信息
     *
     * @param jobId
     * @return
     */
    List<Employ> findByJobId(int jobId);

    /**
     * 通过兼职者id找到雇佣信息
     *
     * @param workerId
     * @return
     */
    List<Employ> findByWorkerId(int workerId);

    /**
     * 通过兼职者id删除所有雇佣信息
     *
     * @param id
     */
    @Transactional
    @Modifying
    @Query(value = "delete from Employ a where a.workerId =?1")
    void deleteEmployByWorkerId(int id);

    /**
     * 通过招聘者id删除所有雇佣信息
     *
     * @param id
     */
    @Transactional
    @Modifying
    @Query(value = "delete from Employ a where a.managerId =?1")
    void deleteEmployByManagerId(int id);

    /**
     * 通过招聘id删除所有雇佣信息
     *
     * @param id
     */
    @Transactional
    @Modifying
    @Query(value = "delete from Employ a where a.jobId =?1")
    void deleteEmployByJobId(int id);

    @Transactional
    @Modifying
    @Query(value = "update Employ a SET a.evaluate = 1 where a.workerId =?1 and a.managerId =?2")
    void evaluated(int workerId, int managerId);

    Employ findByWorkerIdAndJobId(int workerId, int jobId);
}
