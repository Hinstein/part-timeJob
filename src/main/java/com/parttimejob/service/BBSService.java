package com.parttimejob.service;

import com.parttimejob.entity.BBS;
import com.parttimejob.repository.BBSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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

    public void BBSSave(BBS bbs) {
        bbsRepository.save(bbs);
    }


    @Transactional
    public Page<BBS> findByManagerId(int managerId, int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return bbsRepository.findByManagerId(managerId, pageable);
    }

    public BBS findById(int id) {
        return bbsRepository.findById(id);
    }

    public void editorSave(BBS bbs) {
        bbsRepository.editorSave(bbs);
    }

    @Transactional
    public Page<BBS> findAll(int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return bbsRepository.findAll(pageable);
    }

    public void deleteById(int id) {
        bbsRepository.deleteById(id);
    }

    @Transactional
    public Page<BBS> findByWorkerId(int managerId, int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return bbsRepository.findByWorkerId(managerId, pageable);
    }

    public void views(int id) {
        bbsRepository.views(id);
    }

    public List<BBS> findByWorkerId(int id) {
        return bbsRepository.findByWorkerId(id);
    }

    public List<BBS> findByManagerId(int id) {
        return bbsRepository.findByManagerId(id);
    }

    public List<BBS> findAll() {
        return bbsRepository.findAll();
    }

}
