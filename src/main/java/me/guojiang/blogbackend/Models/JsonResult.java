package me.guojiang.blogbackend.Models;

public class JsonResult<T> {
    private Integer status;
    private String message;
    private T data;

    public JsonResult() {
    }

    public JsonResult(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public JsonResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public JsonResult<T> setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public JsonResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }
}
