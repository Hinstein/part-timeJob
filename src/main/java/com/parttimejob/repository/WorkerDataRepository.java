package com.parttimejob.repository;

import com.parttimejob.entity.WorkerData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-02-01 19:57
 * @Description:
 */
public interface WorkerDataRepository extends JpaRepository<WorkerData, Integer> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE WorkerData workerData  SET workerData.age= :#{#workerData.age}," +
            "workerData.educationBackground= :#{#workerData.educationBackground}," +
            "workerData.email= :#{#workerData.email}," +
            "workerData.expectation= :#{#workerData.expectation}," +
            "workerData.introduce= :#{#workerData.introduce}, " +
            "workerData.jobIntension= :#{#workerData.jobIntension}, " +
            "workerData.name= :#{#workerData.name}, " +
            "workerData.phoneNumber= :#{#workerData.phoneNumber}, " +
            "workerData.sex= :#{#workerData.sex}, " +
            "workerData.workerExperience= :#{#workerData.workerExperience}, " +
            "workerData.workerTime= :#{#workerData.workerTime} " +
            "WHERE workerData.workerId = :#{#workerData.workerId}")
    int update(WorkerData workerData);

    WorkerData findByWorkerId(int id);
}
