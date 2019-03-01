package com.parttimejob.repository;

import com.parttimejob.entity.Worker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-01-21 10:49
 * @Description: 继承JpaRepository来完成对数据库的操作
 */

public interface WorkerRepository extends JpaRepository<Worker, Integer> {
    /**
     * 通过用户名找到找到兼职者
     * @param userName
     * @return Worker
     */
    Worker findByUserName(String userName);

    /**
     * 通过id找到兼职者
     * @param id
     * @return
     */
    Worker findById(int id);

    /**
     * 分页找到所有兼职者
     * @param pageable
     * @return
     */
    @Override
    Page<Worker> findAll(Pageable pageable);

    /**
     *通过id删除兼职者信息
     * @param id
     */
    @Transactional
    @Modifying
    @Query(value = "delete from Worker a where a.id =?1")
    void deleteWorkerById(int id);
}
