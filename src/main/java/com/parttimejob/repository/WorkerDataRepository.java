package com.parttimejob.repository;

import com.parttimejob.entity.WorkerData;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-02-01 19:57
 * @Description:
 */
public interface WorkerDataRepository extends JpaRepository<WorkerData,Integer> {

//    @Modifying
//    @Transactional
//    @Query(value="UPDATE WorkerDate workerdate  SET worker.name= :#{#worker.name}," +
//            "worker.phoneNumber= :#{#worker.phoneNumber}," +
//            "worker.email= :#{#worker.email}," +
//            "worker.birth= :#{#worker.birth}," +
//            "worker.introduce= :#{#worker.introduce}, "+
//            "worker.sex= :#{#worker.sex} " +
//            "WHERE worker.userName = :#{#worker.userName}")
//    int updateEditor(WorkerDate worker);

    WorkerData findByWorkerId(int id);
}
