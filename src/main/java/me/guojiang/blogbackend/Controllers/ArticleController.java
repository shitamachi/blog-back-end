package me.guojiang.blogbackend.Controllers;

import me.guojiang.blogbackend.Models.Article;
import me.guojiang.blogbackend.Models.JsonResult;
import me.guojiang.blogbackend.Services.ArticleService;
import me.guojiang.blogbackend.Services.IService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
                setStatus(200).
                setMessage("successful");
    }


    @GetMapping("/{id}")
    public JsonResult<Article> getArticleById(@PathVariable Long id) {
        return new JsonResult<Article>()
                .setData(service.getOneById(id))
                .setStatus(200)
                .setMessage("successful");
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

    @DeleteMapping("/{id}")
    public JsonResult<Map<String, Boolean>> deleteArticleById(@PathVariable String id) {
        var toDeleteOne = service.getOneById(Long.valueOf(id));
        if (toDeleteOne == null)
            return new JsonResult<>(Map.of("isDeleted", Boolean.FALSE)).setStatus(404).setMessage("删除文章不存在");
        service.deleteOne(toDeleteOne);
        return new JsonResult<>(Map.of("isDeleted", Boolean.TRUE)).setStatus(200).setMessage("删除OK");
    }

    @PutMapping("")
    public Article updateArticle(@RequestBody Article data) {
        return service.updateOne(data);
    }
}
