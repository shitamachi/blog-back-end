package me.guojiang.blogbackend.Models;

import java.util.List;

public class Archive {
    private String year;
    private String month;
    private String day;
    private String count;
    private List<Article> articles;

    public Archive(String year, String month, String day, String count, List<Article> articles) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.count = count;
        this.articles = articles;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}