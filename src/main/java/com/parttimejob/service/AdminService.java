package com.parttimejob.service;

import com.parttimejob.entity.Admin;
import com.parttimejob.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.service
 * @Author: Hinstein
 * @CreateTime: 2019-02-08 15:50
 * @Description:
 */
@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    /**
     * 通过用户名找到管理员
     * @param userName
     * @return
     */
    public Admin findByUserName(String userName) {
        return adminRepository.findByUserName(userName);
    }
}
