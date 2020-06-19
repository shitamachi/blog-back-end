package me.guojiang.blogbackend.Controllers;

import me.guojiang.blogbackend.Models.JsonResult;
import me.guojiang.blogbackend.Models.Tag;
import me.guojiang.blogbackend.Models.VO.AddClassificationVO;
import me.guojiang.blogbackend.Models.Views.TagView;
import me.guojiang.blogbackend.Services.TagService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
        return new JsonResult<>(categoryViews).setStatus(200).setMessage("successful!");
    }

    @GetMapping("/{id}")
    public JsonResult<List<Tag>> getAllTagsByArticleId(@PathVariable Long id) {
        return new JsonResult<>(service.getAllByArticleId(id)).setStatus(200).setMessage("successful!");
    }

    @PostMapping()
    public JsonResult<Tag> addNewTag(@RequestBody AddClassificationVO newOne) {
        if (newOne == null || newOne.getName().isBlank()) {
            return new JsonResult.Builder<Tag>().data(null).message("failed to add").status(500).build();
        }
        var tag = new Tag();
        tag.setName(newOne.getName());
        tag.setDescription(newOne.getDescription());
        tag.setCount(0);
        tag.setCreateDate(LocalDate.now());
        return new JsonResult<>(service.addOne(tag)).setStatus(200).setMessage("create successful!");
    }

    @PostMapping("/{id}")
    public JsonResult<Tag> updateTag(@RequestBody Tag tag, @PathVariable(name = "id") Long tagId) {
        return new JsonResult<>(service.updateOne(tag)).setMessage("update Ok").setStatus(200);
    }

    @DeleteMapping()
    public JsonResult<List<Tag>> deleteTags(@RequestBody List<Tag> tags) {
        var unableDeletedTags = service.deleteAll(tags);
        if (unableDeletedTags.isEmpty()) {
            return new JsonResult.Builder<List<Tag>>().data(null).status(200).message("successful deleted all").build();
        } else {
            return new JsonResult.Builder<List<Tag>>().data(unableDeletedTags).status(500).message("unable to deleted all tags").build();
        }
    }
}
