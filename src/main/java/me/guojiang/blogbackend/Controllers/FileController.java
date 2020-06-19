package me.guojiang.blogbackend.Controllers;

import me.guojiang.blogbackend.Models.JsonResult;
import me.guojiang.blogbackend.Services.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/avatar")
    public JsonResult<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        var path = fileService.store(file);
        return new JsonResult<>(Map.of("path", path)).setStatus(200).setMessage("upload ok!");
    }
}
