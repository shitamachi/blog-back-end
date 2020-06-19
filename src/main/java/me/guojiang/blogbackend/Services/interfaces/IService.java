package me.guojiang.blogbackend.Services.interfaces;

import java.util.List;

public interface IService<T> {
    List<T> getAll();

    T addOne(T x);

    boolean deleteOne(T d);

    T updateOne(T u);

    T getOneById(Long id);
}
