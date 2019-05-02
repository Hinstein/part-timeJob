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
@Table(name = "manager")
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

    @Column
    private String name;

    @Column
    private int active;

    @Column
    private String vendorName;

    @Column
    private String address;

    @Column
    private String vendorTime;

    @Column
    private String headPhoto;


    /**
     * 绝对路径
     */
    @Column
    private String datePath;

    /**
     * 相对路径
     *
     * @return
     */
    @Column
    private String relativePath;

    public String getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVendorTime() {
        return vendorTime;
    }

    public void setVendorTime(String vendorTime) {
        this.vendorTime = vendorTime;
    }

    public String getDatePath() {
        return datePath;
    }

    public void setDatePath(String datePath) {
        this.datePath = datePath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
