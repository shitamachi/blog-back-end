package me.guojiang.blogbackend.Controllers;

import me.guojiang.blogbackend.Repositories.ArchiveRepository;
import me.guojiang.blogbackend.Models.Archive;
import me.guojiang.blogbackend.Models.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/archives")
public class ArchiveController {

    private final ArchiveRepository repository;

    public ArchiveController(ArchiveRepository repository) {
        this.repository = repository;
    }

    /* bad code design but it works, we will refactor all about Archive design */
    @GetMapping
    public JsonResult<ArrayList<Archive>> index() {
        var archives = new ArrayList<Archive>();
        var results = repository.findArticleGroupByYearAndMonthAndDay();
        results.forEach(r -> archives.add(
                new Archive(r[0].toString(), r[1].toString(), r[2].toString(), r[3].toString(),
                        repository.findArticleByYearAndMonthAndDay(
                                r[0].toString(),
                                r[1].toString(),
                                r[2].toString()))));
        return new JsonResult<>(archives).setStatus(200).setMessage("successful");
    }
}
