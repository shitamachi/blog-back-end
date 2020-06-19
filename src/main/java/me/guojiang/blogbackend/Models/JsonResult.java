package me.guojiang.blogbackend.Models;

import java.util.Objects;

public class JsonResult<T> {
    private Integer status;
    private String message;
    private T data;

    public JsonResult() {
    }

    public JsonResult(T data) {
        this.data = data;
    }

    private JsonResult(Builder<T> builder) {
        this.status = builder.status;
        this.message = builder.message;
        this.data = builder.data;
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

    public static <U> JsonResult<U> create(Integer statusCode, String message, U data) {
        var res = new Builder<U>();
        res.status(Objects.requireNonNullElse(statusCode, 200));
        res.message(Objects.requireNonNullElse(message, "request ok,status code 200"));
        if (data != null) res.data(data);
        return res.build();
    }

    public static <U> JsonResult<U> Ok(U data) {
        return new Builder<U>().status(200).message("request ok,status code 200").data(data).build();
    }

    public static <U> JsonResult<U> NotFound(U result) {
        return new Builder<U>().status(404).message("not found,status code 404").data(result).build();
    }

    public static <U> JsonResult<U> BadRequest(U result) {
        return new Builder<U>().status(400).message("bad request ,status code 400").data(result).build();
    }

    public static class Builder<T> {
        private Integer status;
        private String message;
        private T data;

        public Builder<T> status(Integer status) {
            this.status = status;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public JsonResult<T> build() {
            return new JsonResult<>(this);
        }
    }
}
