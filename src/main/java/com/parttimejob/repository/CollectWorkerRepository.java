package com.parttimejob.repository;

import com.parttimejob.entity.CollectWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-03-09 15:58
 * @Description:
 */
public interface CollectWorkerRepository extends JpaRepository<CollectWorker, Integer> {

    CollectWorker findByWorkerIdAndManagerId(int workerId, int managerId);

    @Transactional
    @Modifying
    @Query(value = "delete from CollectWorker a where a.workerId = ?1 and a.managerId=?2 ")
    void deleteByWorkerIdAndManagerId(int workerId, int managerId);

    List<CollectWorker> findByManagerId(int workerId);
}
