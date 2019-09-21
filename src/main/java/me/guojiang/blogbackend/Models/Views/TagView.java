package me.guojiang.blogbackend.Models.Views;

import me.guojiang.blogbackend.Models.Article;
import me.guojiang.blogbackend.Models.Tag;

import java.util.List;

public class TagView extends Tag {
    private List<Article> articles;

    public TagView(Tag tag, List<Article> articles) {
        this.setId(tag.getId());
        this.setName(tag.getName());
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
