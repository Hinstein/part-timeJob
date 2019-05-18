package com.parttimejob.repository;

import com.parttimejob.entity.AdminDataType;
import com.parttimejob.entity.BBS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-05-18 10:51
 * @Description:
 */
public interface AdminDataTypeRepository extends JpaRepository<AdminDataType,Integer> {
    AdminDataType findById(int id);

    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query(value = "UPDATE AdminDataType b  SET b.data= :#{#adminDataType.data}," +
            "b.jobType= :#{#adminDataType.jobType}," +
            "b.managerType= :#{#adminDataType.managerType} " +
            "WHERE b.id =:#{#adminDataType.id} ")
    void updata(AdminDataType adminDataType);
}
