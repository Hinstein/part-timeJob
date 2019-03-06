package com.parttimejob.repository;

import com.parttimejob.entity.BBS;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-03-06 14:38
 * @Description:
 */
public interface BBSRepository extends JpaRepository<BBS, Integer> {

}
