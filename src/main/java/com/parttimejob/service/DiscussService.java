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

    @Autowired
    BBSService bbsService;

    public void save(Discuss discuss) {
        bbsService.discuss(discuss.getBbsId());
        discussRepository.save(discuss);
    }

    public List<Discuss> findByBbsId(int id) {
        return discussRepository.findByBbsId(id);
    }

    public int good(int id) {
        discussRepository.good(id);
        Discuss d = discussRepository.findById(id);
        return d.getGood();
    }

    public int cancelGood(int id) {
        discussRepository.cancelGood(id);
        Discuss d = discussRepository.findById(id);
        return d.getGood();
    }

    public void deleteByBbsId(int id) {
        discussRepository.deleteByBbsId(id);
    }

    public void changeHeadPhoto(String old,String newPhoto){
        System.out.println("更换成功！");
        System.out.println("旧头像路径："+old);
        System.out.println("新头像路径："+newPhoto);
        discussRepository.changeHeadPhoto(old,newPhoto);
    }
}
