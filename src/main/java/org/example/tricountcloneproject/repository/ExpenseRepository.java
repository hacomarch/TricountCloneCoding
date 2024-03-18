package org.example.tricountcloneproject.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.tricountcloneproject.entity.Expense;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class ExpenseRepository {
    private final NamedParameterJdbcTemplate template;

    public ExpenseRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    public void save(Long member_id, Long settlement_id, Expense expense) {
        String sql = "insert into Expense (member_id, settlement_id, name, amount, expense_date)" +
                " values (:member_id, :settlement_id, :name, :amount, :expense_date)";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("member_id", member_id)
                .addValue("settlement_id", settlement_id)
                .addValue("name", expense.getName())
                .addValue("amount", expense.getAmount())
                .addValue("expense_date", expense.getExpense_date());
        template.update(sql, param);
    }

    public void delete(Long id) {
        String sql = "delete from Expense where expense_id = :id";
        Map<String, Object> param = Map.of("id", id);
        template.update(sql, param);
    }

    public Optional<Expense> findById(Long id) {
        String sql = "select * from Expense where expense_id = :id";
        try {
            Map<String, Object> param = Map.of("id", id);
            Expense expense = template.queryForObject(sql, param, rowMapper());
            return Optional.of(expense);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    //멤버 1명이 갖고 있는 지출 가져오기
    public List<Expense> findByMemberId(Long id) {
        String sql = "select * from Expense where member_id = :id";
        Map<String, Object> param = Map.of("id", id);
        return template.query(sql, param, rowMapper());
    }

    //정산 1개가 갖고 있는 지출 가져오기
    public List<Expense> findBySettlementId(Long id) {
        String sql = "select * from Expense where settlement_id = :id";
        Map<String, Object> param = Map.of("id", id);
        return template.query(sql, param, rowMapper());
    }

    public List<Expense> findAll() {
        String sql = "select * from Expense";
        return template.query(sql, rowMapper());
    }

    public RowMapper<Expense> rowMapper() {
        return BeanPropertyRowMapper.newInstance(Expense.class);
    }
}
