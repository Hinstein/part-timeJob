package com.parttimejob.repository;

import com.parttimejob.entity.Discuss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-05-02 19:39
 * @Description:
 */
public interface DiscussRepository extends JpaRepository<Discuss, Integer> {

    List<Discuss> findByBbsId(int id);

    Discuss findById(int id);

    @Transactional
    @Modifying
    @Query(value = "update Discuss a SET a.good = a.good+1 where a.id =?1")
    void good(int id);

    @Transactional
    @Modifying
    @Query(value = "update Discuss a SET a.good = a.good-1 where a.id =?1")
    void cancelGood(int id);

    @Transactional
    @Modifying
    @Query(value = "delete from Discuss a where a.bbsId = ?1 ")
    void deleteByBbsId(int id);
}
