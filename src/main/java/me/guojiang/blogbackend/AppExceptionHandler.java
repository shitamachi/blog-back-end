package me.guojiang.blogbackend;

import me.guojiang.blogbackend.Exceptions.FileStorageException;
import me.guojiang.blogbackend.Models.JsonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(FileStorageException.class)
    public JsonResult<Void> handleFileStorageException(FileStorageException e) {
        return new JsonResult<Void>().setStatus(500).setMessage(e.getMessage());
    }
}
