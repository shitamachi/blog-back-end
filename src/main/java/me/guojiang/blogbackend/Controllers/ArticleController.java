package me.guojiang.blogbackend.Controllers;
import me.guojiang.blogbackend.Services.IService;
import me.guojiang.blogbackend.Services.ArticleService;
import me.guojiang.blogbackend.Models.Article;
import me.guojiang.blogbackend.Models.JsonResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private IService<Article> service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    @GetMapping()
    public JsonResult<List<Article>> getAllArticles() {
        return new JsonResult<List<Article>>().
                setData(service.getAll()).
                setCode(200).
                setMsg("successful");
    }


    @GetMapping("/{id}")
    public JsonResult<Article> getArticleById(@PathVariable Long id) {
        return new JsonResult<Article>()
                .setData(service.getOneById(id))
                .setCode(200)
                .setMsg("successful");
    }

    @PostMapping("")
    public Article addOneArticle(@RequestBody Article article) {
        article.setDate(LocalDateTime.now());
        return service.addOne(article);
    }

    @DeleteMapping("")
    public Article deleteArticle(@RequestBody Article deleteOne) {
        if (deleteOne == null) {
            return null;
        }
        var toDeleteArticles = service.getOneById(deleteOne.getId());
        if (toDeleteArticles != null) {
            var isDeleted = service.deleteOne(toDeleteArticles);
            if (isDeleted) {
                return toDeleteArticles;
            } else {
                return null;
            }
        }
        return null;
    }

    @PutMapping("")
    public Article updateArticle(@RequestBody Article data) {
        return service.updateOne(data);
    }
}
