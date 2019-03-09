package com.parttimejob.entity;

import javax.persistence.*;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.entity
 * @Author: Hinstein
 * @CreateTime: 2019-03-09 15:45
 * @Description:
 */
@Entity
@Table(name = "CollectWorker")
public class CollectWorker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private int workerId;

    @Column
    private int workerDataId;

    @Column
    private int managerId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public int getWorkerDataId() {
        return workerDataId;
    }

    public void setWorkerDataId(int workerDataId) {
        this.workerDataId = workerDataId;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }
}
