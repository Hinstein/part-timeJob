package com.parttimejob.repository;

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
 * @CreateTime: 2019-02-18 12:19
 * @Description:
 */
public interface JobRepository extends JpaRepository<Job, Integer> {

    List<Job> findByManagerId(int id);

    Job findById(int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Job job  SET job.content= :#{#job.content}," +
            "job.title= :#{#job.title}," +
            "job.lowPay= :#{#job.lowPay}," +
            "job.hightPay= :#{#job.hightPay} " +
            "WHERE job.id = :#{#job.id}")
    int updateEditor(Job job);

    @Query(value = "select job from Job job where job.content like  CONCAT('%',?1,'%')")
    List<Job> findByContentLike(String content);
}
