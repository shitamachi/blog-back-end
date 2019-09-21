package me.guojiang.blogbackend.Services;

import me.guojiang.blogbackend.Models.Article;
import me.guojiang.blogbackend.Models.Tag;
import me.guojiang.blogbackend.Repositories.ArticleRepository;
import me.guojiang.blogbackend.Repositories.TagRepository;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class TagService implements IService<Tag> {

    private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;

    public TagService(TagRepository tagRepository, ArticleRepository articleRepository) {
        this.tagRepository = tagRepository;
        this.articleRepository = articleRepository;
    }

    public List<Tag> getAllByArticleId(@NotNull Long id) {
        return tagRepository.findTagsByArticleId(id);
    }

    public List<Article> getAllArticlesByTagId(Integer id) {
        return articleRepository.getAllArticlesByTagId(id);
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag addOne(Tag x) {
        return null;
    }

    @Override
    public boolean deleteOne(Tag d) {
        return false;
    }

    @Override
    public Tag updateOne(Tag u) {
        return null;
    }

    @Override
    public Tag getOneById(Long id) {
        return null;
    }
}
