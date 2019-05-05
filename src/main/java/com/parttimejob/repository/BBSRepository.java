package com.parttimejob.repository;

import com.parttimejob.entity.BBS;
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
 * @CreateTime: 2019-03-06 14:38
 * @Description:
 */
public interface BBSRepository extends JpaRepository<BBS, Integer> {
    Page<BBS> findByManagerIdAndStatus(int id, int status, Pageable pageable);

    List<BBS> findByManagerIdAndStatus(int id, int status);

    @Override
    List<BBS> findAll();

    Page<BBS> findByWorkerIdAndStatus(int id, int status, Pageable pageable);

    List<BBS> findByWorkerIdAndStatus(int id, int status);

    BBS findById(int id);

    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query(value = "UPDATE BBS b  SET b.title= :#{#bbs.title}," +
            "b.content= :#{#bbs.content} " +
            "WHERE b.id = :#{#bbs.id}")
    int editorSave(BBS bbs);

    @Override
    Page<BBS> findAll(Pageable pageable);

    void deleteById(int id);

    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "update BBS a SET a.views = a.views+1 where a.id =?1")
    void views(int id);

    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "update BBS a SET a.discuss = a.discuss+1 where a.id =?1")
    void discuss(int id);
}
