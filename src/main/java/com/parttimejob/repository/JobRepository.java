package com.parttimejob.repository;

import com.parttimejob.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-02-18 12:19
 * @Description:
 */
public interface JobRepository extends JpaRepository<Job,Integer> {

}
