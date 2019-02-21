package com.parttimejob.repository;

import com.parttimejob.entity.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-01-21 19:07
 * @Description:
 */
public interface ManagerRepository extends JpaRepository<Manager,Integer> {
    Manager findByUserName(String userName);

    Manager findById(int id);

    List<Manager> findByAudit(int i);

    @Transactional
    @Modifying
    @Query(value = "delete from Manager where id =?1",nativeQuery = true)
    void deleteManagerById(int id);

    @Transactional
    @Modifying
    @Query(value = "update Manager SET audit = 1 where id =?1",nativeQuery = true)
    void passManager(int id);


    Page<Manager> findByAudit(int audit , Pageable pageable);
}
