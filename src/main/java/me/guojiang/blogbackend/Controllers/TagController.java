package me.guojiang.blogbackend.Controllers;

import me.guojiang.blogbackend.Models.JsonResult;
import me.guojiang.blogbackend.Models.Tag;
import me.guojiang.blogbackend.Models.Views.TagView;
import me.guojiang.blogbackend.Services.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService service;

    public TagController(TagService service) {
        this.service = service;
    }

    @GetMapping
    public JsonResult<List<TagView>> getAllTagsWithArticles() {
        List<TagView> categoryViews = new ArrayList<>();
        service.getAll().forEach(tag -> {
            var articles = service.getAllArticlesByTagId(tag.getId());
            categoryViews.add(new TagView(tag, articles));
        });
        return new JsonResult<>(categoryViews).setCode(200).setMsg("successful!");
    }

    @GetMapping("/{id}")
    public JsonResult<List<Tag>> getAllTagsByArticleId(@PathVariable Long id) {
        return new JsonResult<>(service.getAllByArticleId(id)).setCode(200).setMsg("successful!");    }
}
