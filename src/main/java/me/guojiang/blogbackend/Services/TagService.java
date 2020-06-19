package me.guojiang.blogbackend.Services;

import me.guojiang.blogbackend.Models.Article;
import me.guojiang.blogbackend.Models.Tag;
import me.guojiang.blogbackend.Repositories.ArticleRepository;
import me.guojiang.blogbackend.Repositories.TagRepository;
import me.guojiang.blogbackend.Services.interfaces.IService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

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
        return tagRepository.save(x);
    }

    @Override
    public boolean deleteOne(Tag d) {
        return false;
    }

    public List<Tag> deleteAll(List<Tag> tags) {
        tagRepository.deleteAll(tags);
        return tags.stream()
                .filter(tag -> tagRepository.existsById(tag.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Tag updateOne(Tag u) {
        return tagRepository.save(u);
    }

    @Override
    public Tag getOneById(Long id) {
        return null;
    }
}
