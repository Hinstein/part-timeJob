package com.parttimejob.repository;

import com.parttimejob.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-02-03 16:49
 * @Description:
 */
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    /**
     * 通过用户名找到管理员
     *
     * @param userName
     * @return
     */
    Admin findByUserName(String userName);
}
