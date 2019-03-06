package com.parttimejob.service;

import com.parttimejob.repository.BBSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.service
 * @Author: Hinstein
 * @CreateTime: 2019-03-06 14:39
 * @Description:
 */
@Service
public class BBSService {
    @Autowired
    BBSRepository bbsRepository;
}
