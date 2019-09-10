package me.guojiang.blogbackend.Repositories;

import me.guojiang.blogbackend.Models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiveRepository extends JpaRepository<Article, Long> {

    @Query(value = "select year(date) as year, month(date) as month, day(date) as day, count(*) as count from article group by year(date), month(date), day(date)" +
            "order by year desc, month desc, day desc;"
            , nativeQuery = true)
    List<Object[]> findArticleGroupByYearAndMonthAndDay();


    @Query(value = "select *, year(date) as year, month(date) as month, day(date) as day from article" +
            " where year(date) = :year" +
            "  and month(date) = :month" +
            "  and day(date) = :day order by date desc;"
            , nativeQuery = true)
    List<Article> findArticleByYearAndMonthAndDay(@Param("year") String year
                                          , @Param("month") String month
                                          , @Param("day") String day);
}
