package me.guojiang.blogbackend.Repositories;

import me.guojiang.blogbackend.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(value =
            "select c.id, c.name, c.gmt_create, c.count, c.description" +
            " from article" +
            " join category_article ca on article.id = ca.article_id" +
            " join category c on ca.category_id = c.id" +
            " where article.id =:articleId", nativeQuery = true)
    List<Category> findCategoriesByArticleId(@Param("articleId") Long id);

    @Override
    List<Category> findAll();

}
