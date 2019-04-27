package com.parttimejob.service;

import com.parttimejob.entity.Message;
import com.parttimejob.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.service
 * @Author: Hinstein
 * @CreateTime: 2019-03-12 16:24
 * @Description:
 */
@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    public void save(Message message) {
        messageRepository.save(message);
    }

    public List<Message> findByManagerId(int id) {
        return messageRepository.findByManagerId(id);
    }


    public void deleteById(int id) {
        messageRepository.deleteById(id);
    }
}
