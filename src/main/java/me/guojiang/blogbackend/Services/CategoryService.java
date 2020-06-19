package me.guojiang.blogbackend.Services;

import me.guojiang.blogbackend.Models.Article;
import me.guojiang.blogbackend.Models.Category;
import me.guojiang.blogbackend.Repositories.ArticleRepository;
import me.guojiang.blogbackend.Repositories.CategoryRepository;
import me.guojiang.blogbackend.Services.interfaces.IService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService implements IService<Category> {

    private final CategoryRepository repository;
    private final ArticleRepository articleRepository;

    public CategoryService(CategoryRepository repository, ArticleRepository articleRepository) {
        this.repository = repository;
        this.articleRepository = articleRepository;
    }

    public List<Category> getAllByArticleId(@NotNull Long id) {
        return repository.findCategoriesByArticleId(id);
    }

    public List<Article> getAllArticlesByCategoryId(Integer id) {
        return articleRepository.getAllArticlesByCategotyId(id);
    }

    @Override
    public List<Category> getAll() {
        return repository.findAll();
    }

    @Override
    public Category addOne(Category x) {
        return repository.save(x);
    }

    @Override
    public boolean deleteOne(Category d) {
        return false;
    }

    public List<Category> deleteAll(List<Category> categories) {
        repository.deleteAll(categories);
        return categories.stream().filter(category -> repository.existsById(category.getId())).collect(Collectors.toList());
    }

    @Override
    public Category updateOne(Category u) {
        return null;
    }

    @Override
    public Category getOneById(Long id) {
        return null;
    }
}
