package com.fooddeliveryplatform.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T>{
    T get(int id);
    List<T> getAll() throws SQLException;
    T save(T t);
    T update(T t);
    void delete(T t);
}
