package me.guojiang.blogbackend.Repositories;

import me.guojiang.blogbackend.Models.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {
//    Article getArticleById(String id);

    @Override
    <S extends Article> S save(S s);

    @Override
    <S extends Article> Iterable<S> saveAll(Iterable<S> iterable);

    @Override
    Optional<Article> findById(Long s);

    @Override
    boolean existsById(Long s);

    @Override
    Iterable<Article> findAll();

    @Override
    Iterable<Article> findAllById(Iterable<Long> iterable);

    @Override
    long count();

    @Override
    void deleteById(Long s);

    @Override
    void delete(Article article);

    @Override
    void deleteAll(Iterable<? extends Article> iterable);

    @Override
    void deleteAll();

    Article deleteArticleById(Long id);
}
