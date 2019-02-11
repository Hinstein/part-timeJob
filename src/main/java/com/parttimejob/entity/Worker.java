package com.parttimejob.entity;


import javax.persistence.*;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.entity
 * @Author: Hinstein
 * @CreateTime: 2019-01-21 10:35
 * @Description:
 */

//使用jpa注解配置映射关系
@Entity //告诉JPA这是一个实体类（和数据表映射的类）
@Table(name="worker") //@Table来指定和哪个数据表对应：如果省略默认表名就是user
public class Worker {

    @Id //这是一个主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private Integer id;

    @Column(name="user_name",length = 50)
    private String userName;

    @Column//省略默认列名就是属性名
    private String password;

    @JoinColumn
    @OneToOne(cascade = {CascadeType.ALL})
    private WorkerDate workerData;

    public WorkerDate getWorkerData() {
        return workerData;
    }

    public void setWorkerData(WorkerDate workerData) {
        this.workerData = workerData;
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
}
