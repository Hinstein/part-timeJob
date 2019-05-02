package com.parttimejob.service;

import com.parttimejob.entity.Discuss;
import com.parttimejob.repository.DiscussRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.service
 * @Author: Hinstein
 * @CreateTime: 2019-05-02 19:39
 * @Description:
 */
@Service
public class DiscussService {

    @Autowired
    DiscussRepository discussRepository;

    public void save(Discuss discuss)
    {
        discussRepository.save(discuss);
    }

    public List<Discuss> findByBbsId (int id){
        return discussRepository.findByBbsId(id);
    }

    public int good(int id){
        discussRepository.good(id);
        Discuss d = discussRepository.findById(id);
        return  d.getGood();
    }

    public int cancelGood(int id){
        discussRepository.cancelGood(id);
        Discuss d = discussRepository.findById(id);
        return  d.getGood();
    }
}
