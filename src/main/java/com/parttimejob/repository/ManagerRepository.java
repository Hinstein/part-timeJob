package com.parttimejob.repository;

import com.parttimejob.entity.Manager;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.repository
 * @Author: Hinstein
 * @CreateTime: 2019-01-21 19:07
 * @Description:
 */
public interface ManagerRepository extends JpaRepository<Manager, Integer> {
    /**
     * 通过用户名找到招聘者
     *
     * @param userName
     * @return
     */
    Manager findByUserName(String userName);

    @Override
    List<Manager> findAll();

    /**
     * 通过id找到招聘者
     *
     * @param id
     * @return
     */
    Manager findById(int id);

    /**
     * 通过id删除招聘者
     *
     * @param id
     */
    @Transactional
    @Modifying
    @Query(value = "delete from Manager a where a.id =?1")
    void deleteManagerById(int id);

    /**
     * 通过审核
     *
     * @param id
     */
    @Transactional
    @Modifying
    @Query(value = "update Manager a SET a.audit = 1 where id =?1")
    void passManager(int id);

    /**
     * 分页找到所有未通过审核的招聘者
     *
     * @param audit
     * @param pageable
     * @return
     */
    Page<Manager> findByAudit(int audit, Pageable pageable);

    /**
     * 保存招聘者
     *
     * @param manager
     */
    @Transactional
    @Modifying
    @Query(value = "update Manager m SET m.password= :#{#manager.password} where m.id = :#{#manager.id}")
    void saveEditor(Manager manager);

    /**
     * 分页找到所有招聘者
     *
     * @param pageable
     * @return
     */
    @Override
    Page<Manager> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Manager m  SET m.userName= :#{#manager.userName}," +
            "m.name= :#{#manager.name}," +
            "m.email= :#{#manager.email}," +
            "m.address= :#{#manager.address}," +
            "m.vendorName= :#{#manager.vendorName}," +
            "m.vendorTime= :#{#manager.vendorTime}," +
            "m.datePath= :#{#manager.datePath}," +
            "m.relativePath= :#{#manager.relativePath}," +
            "m.phoneNumber= :#{#manager.phoneNumber} " +
            "WHERE m.id = :#{#manager.id}")
    void informationSave(Manager manager);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Manager m  SET m.userName= :#{#manager.userName}," +
            "m.name= :#{#manager.name}," +
            "m.email= :#{#manager.email}," +
            "m.address= :#{#manager.address}," +
            "m.vendorName= :#{#manager.vendorName}," +
            "m.vendorTime= :#{#manager.vendorTime}," +
            "m.phoneNumber= :#{#manager.phoneNumber} " +
            "WHERE m.id = :#{#manager.id}")
    void adminSave(Manager manager);

    @Transactional
    @Modifying
    @Query(value = "update Manager a SET a.active = a.active+1 where a.id =?1")
    void active(int id);

    @Transactional
    @Modifying
    @Query(value = "update Manager a SET a.headPhoto =?1 ,a.pathName=?2 where  a.id =?3")
    void headPhotoEditor(String src, String pathName,int id);
}
