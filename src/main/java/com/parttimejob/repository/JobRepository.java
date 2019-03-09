package com.parttimejob.repository;

import com.parttimejob.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * 通过招聘者找到该招聘者的所有工作
     * @param id
     * @return
     */
    List<Job> findByManagerId(int id);

    List<Job> findAll();

    /**
     * 通过工作id找到该工作
     * @param id
     * @return
     */
    Job findById(int id);

    /**
     * 通过工作实体类更新该工作
     * @param job
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE Job job  SET job.content= :#{#job.content}," +
            "job.title= :#{#job.title}," +
            "job.lowPay= :#{#job.lowPay}," +
            "job.hightPay= :#{#job.hightPay} " +
            "WHERE job.id = :#{#job.id}")
    int updateEditor(Job job);

    /**
     * 通过工作的招聘信息找到相应的工作
     * @param content
     * @param pageable
     * @return
     */
    @Query(value = "select job from Job job where job.title like  CONCAT('%',?1,'%')")
    Page<Job> findByTitleLike(String content, Pageable pageable);

    /**
     * 分页获得所有工作
     * @param pageable
     * @return
     */
    @Override
    Page<Job> findAll(Pageable pageable);

    /**
     * 通过招聘者id删除所有工作
     * @param id
     */
    @Transactional
    @Modifying
    @Query(value = "delete from Job a where a.managerId =?1")
    void deleteJobByManagerId(int id);

    Page<Job> findByManagerId(int id,Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update Job a SET a.collection = a.collection+1 where a.id =?1")
    void collectionSave(int id);

    @Transactional
    @Modifying
    @Query(value = "update Job a SET a.deliver = a.deliver+1 where a.id =?1")
    void deliverSave(int id);

    @Transactional
    @Modifying
    @Query(value = "update Job a SET a.collection = a.collection-1 where a.id =?1")
    void collectionCancel(int id);

    @Transactional
    @Modifying
    @Query(value = "update Job a SET a.deliver = a.deliver-1 where a.id =?1")
    void deliverCancel(int id);
}
