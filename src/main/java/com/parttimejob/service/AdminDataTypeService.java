package com.parttimejob.service;

import com.parttimejob.entity.AdminDataType;
import com.parttimejob.repository.AdminDataTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.service
 * @Author: Hinstein
 * @CreateTime: 2019-05-18 10:54
 * @Description:
 */
@Service
public class AdminDataTypeService {
    @Autowired
    AdminDataTypeRepository adminDataTypeRepository;
    public AdminDataType findById() {
        return adminDataTypeRepository.findById(1);
    }

    public void updata(AdminDataType adminDataType) {
        adminDataType.setId(1);
        adminDataTypeRepository.updata(adminDataType);
    }

}
