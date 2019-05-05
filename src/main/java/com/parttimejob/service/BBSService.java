package com.parttimejob.service;

import com.parttimejob.entity.BBS;
import com.parttimejob.repository.BBSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Autowired
    DiscussService discussService;

    public void BBSSave(BBS bbs) {
        bbsRepository.save(bbs);
    }


    @Transactional(rollbackOn = Exception.class)
    public Page<BBS> findByManagerId(int managerId, int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return bbsRepository.findByManagerIdAndStatus(managerId, 2, pageable);
    }

    public BBS findById(int id) {
        return bbsRepository.findById(id);
    }

    public void editorSave(BBS bbs) {
        bbsRepository.editorSave(bbs);
    }

    @Transactional(rollbackOn = Exception.class)
    public Page<BBS> findAll(int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return bbsRepository.findAll(pageable);
    }

    public void deleteById(int id) {
        discussService.deleteByBbsId(id);
        bbsRepository.deleteById(id);
    }

    @Transactional(rollbackOn = Exception.class)
    public Page<BBS> findByWorkerId(int managerId, int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return bbsRepository.findByWorkerIdAndStatus(managerId, 1, pageable);
    }

    public void views(int id) {
        bbsRepository.views(id);
    }

    public List<BBS> findByWorkerId(int id) {
        return bbsRepository.findByWorkerIdAndStatus(id, 1);
    }

    public List<BBS> findByManagerId(int id) {
        return bbsRepository.findByManagerIdAndStatus(id, 2);
    }

    public List<BBS> findAll() {
        return bbsRepository.findAll();
    }

    public void discuss(int id) {
        bbsRepository.discuss(id);
    }

    @Transactional(rollbackOn = Exception.class)
    public Page<BBS> hot(int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        Sort sort = new Sort(Sort.Direction.DESC, "discuss");
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return bbsRepository.findAll(pageable);
    }

    @Transactional(rollbackOn = Exception.class)
    public Page<BBS> view(int pageNo, int pageSize) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        Sort sort = new Sort(Sort.Direction.DESC, "views");
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return bbsRepository.findAll(pageable);
    }

}
