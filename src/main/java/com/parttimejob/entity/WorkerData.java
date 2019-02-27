package com.parttimejob.entity;

import javax.persistence.*;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.entity
 * @Author: Hinstein
 * @CreateTime: 2019-02-01 19:34
 * @Description:
 */
@Table(name="workerData")
@Entity
public class WorkerData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;


    @Column
    private String phoneNumber;

    @Column
    private String email;

    @Column
    private Integer age;

    //求职意向
    @Column
    private String jobIntension;

    //期望薪资
    @Column
    private String expectation;

    //工作时间
    @Column
    private String workerTime;

    //工作经历
    @Column
    private String workerExperience;

    //学历
    @Column
    private int educationBackground;

    @Column
    private int sex;

    //自我评价
    @Column
    private String introduce;

    @Column
    private int workerId;


    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getJobIntension() {
        return jobIntension;
    }

    public void setJobIntension(String jobIntension) {
        this.jobIntension = jobIntension;
    }

    public String getExpectation() {
        return expectation;
    }

    public void setExpectation(String expectation) {
        this.expectation = expectation;
    }

    public String getWorkerTime() {
        return workerTime;
    }

    public void setWorkerTime(String workerTime) {
        this.workerTime = workerTime;
    }

    public String getWorkerExperience() {
        return workerExperience;
    }

    public void setWorkerExperience(String workerExperience) {
        this.workerExperience = workerExperience;
    }

    public int getEducationBackground() {
        return educationBackground;
    }

    public void setEducationBackground(int educationBackground) {
        this.educationBackground = educationBackground;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
