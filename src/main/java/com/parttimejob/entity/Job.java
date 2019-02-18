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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @ManyToOne(cascade = {CascadeType.ALL}, optional = false)//可选属性optional=false,表示author不能为空。删除文章，不影响用户
    @JoinColumn(name = "manager_id")
    private Manager manager;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
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
