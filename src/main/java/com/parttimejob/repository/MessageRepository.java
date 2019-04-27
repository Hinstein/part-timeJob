package com.parttimejob.repository;

import com.parttimejob.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-03-12 16:24
 * @Description:
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findByManagerId(int id);

    void deleteById(int id);
}
