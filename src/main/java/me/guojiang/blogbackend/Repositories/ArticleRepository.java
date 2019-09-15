package me.guojiang.blogbackend.Repositories;

import me.guojiang.blogbackend.Models.Article;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Query(value = "select article.id, article.content, article.date, article.preview, article.title" +
            " from article," +
            " category_article," +
            " category" +
            " where article.id = category_article.article_id" +
            " and category.id = category_article.category_id" +
            " and category.id = :id", nativeQuery = true)
    List<Article> getAllArticlesByCategotyId(@Param("id") Integer id);
}
