package org.example.tricountcloneproject.settlement;

import org.example.tricountcloneproject.expense.Expense;
import org.example.tricountcloneproject.expense.ExpenseRepository;
import org.example.tricountcloneproject.member.Member;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.*;

@Repository
public class SettlementRepository {
    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public SettlementRepository(DataSource dataSource, ExpenseRepository expenseRepository) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("Settlement")
                .usingGeneratedKeyColumns("settlement_id");
    }

    public void save(Settlement settlement) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(settlement);
        Number key = jdbcInsert.executeAndReturnKey(param);
        settlement.setSettlementId(key.longValue());
    }

    public void delete(Long settlementId) {
        String expenseSql = "delete from Expense where settlement_id = :id";
        template.update(expenseSql, Map.of("id", settlementId));

        String sql = "delete from Settlement where settlement_id = :id";
        template.update(sql, Map.of("id", settlementId));
    }

    public Optional<Settlement> findById(Long settlementId) {
        String sql = "select * from Settlement where settlement_id = :id";
        try {
            Map<String, Object> param = Map.of("id", settlementId);
            return Optional.of(template.queryForObject(sql, param, settlementRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    //정산 1개가 갖고 있는 지출 가져오기
    public List<Expense> findExpensesById(Long settlementId) {
        String sql = "select * from Expense where settlement_id = :id";
        return template.query(sql, Map.of("id", settlementId), expenseRowMapper());
    }

    //정산에 참여한 멤버 리스트 가져오기
    public List<Member> findMembersById(Long settlementId) {
        String sql = "select distinct m.member_id, m.user_id, m.user_pw, m.nickname" +
                " from Settlement s" +
                " join Expense e on s.settlement_id = e.settlement_id" +
                " join Member m on e.member_id = m.member_id" +
                " where s.settlement_id = :id";
        return template.query(sql, Map.of("id", settlementId), memberRowMapper());
    }

    public List<Settlement> findAll() {
        String sql = "select * from Settlement";
        return template.query(sql, settlementRowMapper());
    }

    //정산에서 멤버별로 얼마나 지출했는지 구하는 메서드
    public Map<Long, BigDecimal> getTotalAmountByMember(Long settlementId) {
        String sql = "select member_id, sum(amount) as total_amount" +
                " from Expense" +
                " where settlement_id = :settlement_id" +
                " group by member_id";
        Map<String, Object> param = Map.of("settlement_id", settlementId);

        return template.query(sql, param, rs -> {
            HashMap<Long, BigDecimal> totalAmountByMember = new HashMap<>();
            while (rs.next()) {
                totalAmountByMember.put(
                        rs.getLong("member_id"),
                        rs.getBigDecimal("total_amount")
                );
            }
            return totalAmountByMember;
        });
    }

    //정산에 참여한 총 참가자 수
    public BigDecimal totalMemberCount(Long settlementId) {
        String sql = "select count(distinct m.member_id) as member_count" +
                " from Settlement s" +
                " join Expense e on s.settlement_id = e.settlement_id" +
                " join Member m on e.member_id = m.member_id" +
                " where s.settlement_id = :id";
        return template.queryForObject(sql, Map.of("id", settlementId), BigDecimal.class);
    }

    //정산이 갖고 있는 지출 금액 리스트 가져오기
    public List<BigDecimal> getExpensesById(Long settlementId) {
        String sql = "select amount from Expense where settlement_id = :id";
        return template.queryForList(sql, Map.of("id", settlementId), BigDecimal.class);
    }

    public RowMapper<Settlement> settlementRowMapper() {
        return BeanPropertyRowMapper.newInstance(Settlement.class);
    }

    public RowMapper<Member> memberRowMapper() {
        return BeanPropertyRowMapper.newInstance(Member.class);
    }

    public RowMapper<Expense> expenseRowMapper() {
        return BeanPropertyRowMapper.newInstance(Expense.class);
    }
}
