package me.guojiang.blogbackend.Models.Views;

import me.guojiang.blogbackend.Models.Article;
import me.guojiang.blogbackend.Models.Category;

import java.util.List;

public class CategoryView extends Category {

    private List<Article> articles;

    public CategoryView(Category category, List<Article> articles) {
        this.setId(category.getId());
        this.setName(category.getName());
        this.setGmtCreate(category.getGmtCreate());
        this.setGmtModified(category.getGmtModified());
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
