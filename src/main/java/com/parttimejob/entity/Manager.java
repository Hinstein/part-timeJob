package com.parttimejob.entity;

import javax.persistence.*;
import java.util.List;


/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.entity
 * @Author: Hinstein
 * @CreateTime: 2019-01-21 19:02
 * @Description:
 */

@Entity
@Table(name="manager")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String userName;

    @Column
    private String password;

    @Column
    private int audit;

    @Column
    private String date;

    @Column
    private String email;

    @Column
    private String phoneNumber;

    @OneToMany(mappedBy = "manager",cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Job> jobs;


    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAudit() {
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
    }
}
