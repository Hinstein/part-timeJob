package com.parttimejob.repository;

import com.parttimejob.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Map;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-01-21 19:07
 * @Description:
 */
public interface ManagerRepository extends JpaRepository<Manager,Integer> {
    Manager findByUserName(String username);
}
