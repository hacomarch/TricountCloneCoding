package org.example.tricountcloneproject.expense;

import lombok.extern.slf4j.Slf4j;
import org.example.tricountcloneproject.expense.Expense;
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

    public void save(Long memberId, Long settlementId, Expense expense) {
        String sql = "insert into Expense (member_id, settlement_id, name, amount, expense_date)" +
                " values (:member_id, :settlement_id, :name, :amount, :expense_date)";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("member_id", memberId)
                .addValue("settlement_id", settlementId)
                .addValue("name", expense.getName())
                .addValue("amount", expense.getAmount())
                .addValue("expense_date", expense.getExpenseDate());
        template.update(sql, param);
    }

    public void delete(Long expenseId) {
        String sql = "delete from Expense where expense_id = :id";
        template.update(sql, Map.of("id", expenseId));
    }

    public Optional<Expense> findById(Long expenseId) {
        String sql = "select * from Expense where expense_id = :id";
        try {
            return Optional.of(template.queryForObject(sql, Map.of("id", expenseId), expenseRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    //멤버 1명이 갖고 있는 지출 가져오기
    public List<Expense> findByMemberId(Long memberId) {
        String sql = "select * from Expense where member_id = :id";
        return template.query(sql, Map.of("id", memberId), expenseRowMapper());
    }

    //정산 1개가 갖고 있는 지출 가져오기
    //정산에 참여한 참가자만 지출 내역을 볼 수 있다.
    public List<Expense> findBySettlementId(Long settlementId, Long memberId) {
        String sql = "select * from Expense where settlement_id = :id";
        return template.query(sql, Map.of("id", settlementId), expenseRowMapper());
    }

    public List<Expense> findAll() {
        String sql = "select * from Expense";
        return template.query(sql, expenseRowMapper());
    }

    public RowMapper<Expense> expenseRowMapper() {
        return BeanPropertyRowMapper.newInstance(Expense.class);
    }
}
