package me.guojiang.blogbackend.Services;

import me.guojiang.blogbackend.Repositories.ArticleRepository;
import me.guojiang.blogbackend.Models.Article;
import me.guojiang.blogbackend.Services.interfaces.IService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService implements IService<Article> {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> getAll() {
        return (List<Article>) articleRepository.findAll();
    }

    @Override
    public Article addOne(Article x) {
        articleRepository.save(x);
        return articleRepository.findById(x.getId()).orElse(null);
    }

    @Override
    public boolean deleteOne(Article d) {
        articleRepository.delete(d);
        return articleRepository.findById(d.getId()).isEmpty();
    }

    @Override
    public Article updateOne(Article u) {
        var toUpdateArticle = getOneById(u.getId());
        if (toUpdateArticle == null) return null;
        u.setDate(toUpdateArticle.getDate());
        u.setId(toUpdateArticle.getId());
        return articleRepository.save(u);
    }

    @Override
    public Article getOneById(Long id) {
        return articleRepository.findById(id).orElseGet(null);
    }

}
