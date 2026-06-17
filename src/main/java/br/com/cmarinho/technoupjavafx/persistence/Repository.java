package br.com.cmarinho.technoupjavafx.persistence;

import java.util.List;

public interface Repository<T> {
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(T entity);
}