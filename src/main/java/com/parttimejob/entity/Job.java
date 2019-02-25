package com.parttimejob.entity;

import javax.persistence.*;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.entity
 * @Author: Hinstein
 * @CreateTime: 2019-02-17 22:49
 * @Description:
 */
@Table(name = "job")
@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String title;

    @Column
    private int lowPay;

    @Column
    private int hightPay;

    @Column
    private String content;

    @Column
    private String date;

    @Column
    private Integer managerId;

    @Column
    private int collection;

    @Column
    private int deliver;

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public int getDeliver() {
        return deliver;
    }

    public void setDeliver(int deliver) {
        this.deliver = deliver;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLowPay() {
        return lowPay;
    }

    public void setLowPay(int lowPay) {
        this.lowPay = lowPay;
    }

    public int getHightPay() {
        return hightPay;
    }

    public void setHightPay(int hightPay) {
        this.hightPay = hightPay;
    }
}
