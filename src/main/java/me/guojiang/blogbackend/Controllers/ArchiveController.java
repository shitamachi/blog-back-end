package me.guojiang.blogbackend.Controllers;

import me.guojiang.blogbackend.Repositories.ArticleRepository;
import me.guojiang.blogbackend.Models.Archive;
import me.guojiang.blogbackend.Models.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/archives")
public class ArchiveController {

    private final ArticleRepository repository;

    public ArchiveController(ArticleRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public JsonResult<List<Archive>> index() {
        var archives = new ArrayList<Archive>();
        repository.findAll().forEach(article -> archives.add(new Archive(article.getTitle(), article.getDate())));
        return new JsonResult<List<Archive>>()
                .setData(archives)
                .setCode(200)
                .setMsg("successful");
    }
}
