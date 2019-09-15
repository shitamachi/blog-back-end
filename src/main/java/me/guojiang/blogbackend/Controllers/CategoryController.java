package me.guojiang.blogbackend.Controllers;

import me.guojiang.blogbackend.Models.Category;
import me.guojiang.blogbackend.Models.JsonResult;
import me.guojiang.blogbackend.Models.Views.CategoryView;
import me.guojiang.blogbackend.Services.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping()
    public JsonResult<List<CategoryView>> getAllCategoriesWithArticles() {
        List<CategoryView> categoryViews = new ArrayList<>();
        service.getAll().forEach(category -> {
            var articles = service.getAllArticlesByCategoryId(category.getId());
            categoryViews.add(new CategoryView(category, articles));
        });
        return new JsonResult<>(categoryViews).setCode(200).setMsg("successful!");
    }

    @GetMapping("/{id}")
    public JsonResult<List<Category>> getAllCategoriesByArticleId(@PathVariable Long id) {
        return new JsonResult<>(service.getAllByArticleId(id)).setCode(200).setMsg("successful!");
    }

}
