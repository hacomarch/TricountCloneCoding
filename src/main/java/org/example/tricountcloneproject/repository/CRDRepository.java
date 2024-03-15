package org.example.tricountcloneproject.repository;

import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

public interface CRDRepository<T> {
    void save(T entity);

    void delete(Long id);

    Optional<T> findById(Long id);

    List<T> findAll();

    RowMapper<T> rowMapper();
}
