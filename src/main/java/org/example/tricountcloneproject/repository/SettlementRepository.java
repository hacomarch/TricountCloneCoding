package org.example.tricountcloneproject.repository;

import org.example.tricountcloneproject.entity.Settlement;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class SettlementRepository {
    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public SettlementRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("Settlement")
                .usingGeneratedKeyColumns("settlement_id");
    }

    public void save(Settlement settlement) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(settlement);
        Number key = jdbcInsert.executeAndReturnKey(param);
        settlement.setSettlement_id(key.longValue());
    }

    public void delete(Long id) {
        String sql = "delete from Settlement where settlement_id = :id";
        Map<String, Long> param = Map.of("id", id);
        template.update(sql, param);
    }

    public Optional<Settlement> findById(Long id) {
        String sql = "select * from Settlement where settlement_id = :id";
        try {
            Map<String, Long> param = Map.of("id", id);
            Settlement settlement = template.queryForObject(sql, param, rowMapper());
            return Optional.of(settlement);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Settlement> findAll() {
        String sql = "select * from Settlement";
        return template.query(sql, rowMapper());
    }

    public RowMapper<Settlement> rowMapper() {
        return BeanPropertyRowMapper.newInstance(Settlement.class);
    }
}
