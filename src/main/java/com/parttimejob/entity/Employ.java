package com.parttimejob.entity;

import javax.persistence.*;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.entity
 * @Author: Hinstein
 * @CreateTime: 2019-02-26 20:46
 * @Description:
 */
@Entity
@Table(name = "employ")
public class Employ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private int workerId;

    @Column
    private int jobId;

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

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }
}
