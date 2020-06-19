package me.guojiang.blogbackend.Models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 50)
    private String name;

    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "description")
    private String description;

    @Column(name = "gmt_create")
    private LocalDate createDate;

//    @Column(name = "gmt_modified")
//    private LocalDate modifiyDate;

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

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
