package me.guojiang.blogbackend.Repositories;

import me.guojiang.blogbackend.Models.Article;
import me.guojiang.blogbackend.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(value =
            "select c.id, c.name, c.gmt_create, c.gmt_modified" +
            " from article" +
            " join category_article ca on article.id = ca.article_id" +
            " join category c on ca.category_id = c.id" +
            " where article.id =:articleId", nativeQuery = true)
    List<Category> findCategoriesByArticleId(@Param("articleId") Long id);

    @Override
    List<Category> findAll();


    @Query(value = "select article.id, content, date, preview, title" +
            " from article," +
            " category_article," +
            " category" +
            " where article.id = category_article.article_id" +
            " and category.id = category_article.category_id" +
            " and category.id = :id", nativeQuery = true)
    List<Article> getAllArticlesUsingCategotyId(@Param("id") Long id);
}
