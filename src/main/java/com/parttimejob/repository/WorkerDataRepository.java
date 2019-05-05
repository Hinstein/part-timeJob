package com.parttimejob.repository;

import com.parttimejob.entity.WorkerData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * 通过兼职者实体类更新该兼职者的个人资料
     *
     * @param workerData
     * @return
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
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

    /**
     * 通过兼职者的id找到该兼职者的个人资料
     *
     * @param id
     * @return
     */
    WorkerData findByWorkerId(int id);

    /**
     * 分页找到所有兼职者的个人资料
     *
     * @param pageable
     * @return
     */
    @Override
    Page<WorkerData> findAll(Pageable pageable);

    /**
     * 通过兼职者的id删除兼职者的个人资料
     *
     * @param id
     */
    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "delete from WorkerData a where a.workerId =?1")
    void deleteWorkerDataByWorkerId(int id);

    //
    @Query(value = "select w from WorkerData w where w.jobIntension like CONCAT('%',?1,'%')")
    Page<WorkerData> findByJobIntensionLike(String content, Pageable pageable);

    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "update WorkerData a SET a.active = a.active+1 where a.workerId =?1")
    void active(int id);

    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "update WorkerData a SET a.headPhoto =?1 ,a.pathName=?2 where a.id =?3")
    void headPhotoEditor(String src, String pathName, int id);

    int countByEducationBackground(String limit);
}
