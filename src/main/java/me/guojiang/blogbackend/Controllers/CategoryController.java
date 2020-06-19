package me.guojiang.blogbackend.Controllers;

import me.guojiang.blogbackend.Models.Category;
import me.guojiang.blogbackend.Models.JsonResult;
import me.guojiang.blogbackend.Models.VO.AddClassificationVO;
import me.guojiang.blogbackend.Models.Views.CategoryView;
import me.guojiang.blogbackend.Services.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
        return new JsonResult<>(categoryViews).setStatus(200).setMessage("successful!");
    }

    @PostMapping()
    public JsonResult<Category> addNewCategory(@RequestBody AddClassificationVO newOne) {
        if (newOne == null || newOne.getName().isBlank()){
            return new JsonResult.Builder<Category>().data(null).message("failed to add category, need name").status(500).build();
        }
        var category = new Category();
        category.setName(newOne.getName());
        category.setDescription(newOne.getDescription());
        category.setCount(0);
        category.setCreateDate(LocalDate.now());
        return new JsonResult.Builder<Category>().data(service.addOne(category)).message("create successful!").status(200).build();
    }

    @DeleteMapping()
    public JsonResult<List<Category>> deleteTags(@RequestBody List<Category> categories) {
        var unableDeletedCategories = service.deleteAll(categories);
        if (unableDeletedCategories.isEmpty()) {
            return new JsonResult.Builder<List<Category>>().data(null).status(200).message("successful deleted all").build();
        } else {
            return new JsonResult.Builder<List<Category>>().data(unableDeletedCategories).status(500).message("unable to deleted all tags").build();
        }
    }

    @GetMapping("/{id}")
    public JsonResult<List<Category>> getAllCategoriesByArticleId(@PathVariable Long id) {
        return new JsonResult<>(service.getAllByArticleId(id)).setStatus(200).setMessage("successful!");
    }

}
