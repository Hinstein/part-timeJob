package com.parttimejob.repository;

import com.parttimejob.entity.Worker;
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

public interface WorkerRepository extends JpaRepository<Worker,Integer> {
    /**
     * @Description: 在数据库查找用户
     * @param userName
     * @return Worker
     */
    Worker findByUserName(String userName);
}
