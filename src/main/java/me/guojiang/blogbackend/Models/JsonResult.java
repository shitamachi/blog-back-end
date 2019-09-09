package me.guojiang.blogbackend.Models;

public class JsonResult<T> {
    private Integer code;
    private String msg;
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

    public Integer getCode() {
        return code;
    }

    public JsonResult<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public JsonResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
