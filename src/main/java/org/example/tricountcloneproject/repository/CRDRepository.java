package org.example.tricountcloneproject.repository;

import org.example.tricountcloneproject.entity.Member;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

public interface CRDRepository<T> {
    void save(Member member);

    void delete(Long id);

    Optional<T> findById(Long id);

    List<T> findAll();

    RowMapper<T> rowMapper();
}
