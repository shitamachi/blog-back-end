package me.guojiang.blogbackend.Repositories;

import me.guojiang.blogbackend.Models.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Integer> {
    @Override
    List<Tag> findAll();

    @Query(value = "select tag.id, tag.name, tag.count, tag.description, tag.create_date from article  " +
            "join tag_article ta on article.id = ta.article_id " +
            "join tag on tag.id = ta.tag_id " +
            "where article_id = :id", nativeQuery = true)
    List<Tag> findTagsByArticleId(@Param("id") Long id);

}
