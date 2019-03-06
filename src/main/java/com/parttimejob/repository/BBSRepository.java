package com.parttimejob.repository;

import com.parttimejob.entity.BBS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-03-06 14:38
 * @Description:
 */
public interface BBSRepository extends JpaRepository<BBS, Integer> {
    Page<BBS> findByManagerId(int id, Pageable pageable);

    BBS findById(int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE BBS b  SET b.title= :#{#bbs.title}," +
            "b.content= :#{#bbs.content} " +
            "WHERE b.id = :#{#bbs.id}")
    int editorSave(BBS bbs);

    @Override
    Page<BBS> findAll(Pageable pageable);

    void deleteById(int id);
}
