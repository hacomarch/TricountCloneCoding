package org.example.tricountcloneproject.repository;

import org.example.tricountcloneproject.entity.Member;
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
public class MemberRepository {
    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("Member")
                .usingGeneratedKeyColumns("member_id");
    }

    public void save(Member member) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(member);
        Number key = jdbcInsert.executeAndReturnKey(param);
        member.setMember_id(key.longValue());
    }

    public void delete(Long id) {
        String expenseSql = "delete from Expense where member_id = :id";
        Map<String, Object> expenseParam = Map.of("id", id);
        template.update(expenseSql, expenseParam);

        String sql = "delete from Member where member_id = :id";
        Map<String, Object> param = Map.of("id", id);
        template.update(sql, param);
    }

    public Optional<Member> findById(Long id) {
        String sql = "select * from Member where member_id=:id";
        try {
            Map<String, Object> param = Map.of("id", id);
            Member member = template.queryForObject(sql, param, rowMapper());
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    //멤버 1명이 갖고 있는 정산 가져오기
    public List<Settlement> findSettlementListById(Long id) {
        String sql = "select distinct s.settlement_id, s.name" +
                " from Member m" +
                " join Expense e on m.member_id = e.member_id" +
                " join Settlement s on e.settlement_id = s.settlement_id" +
                " where m.member_id = :id";
        Map<String, Object> param = Map.of("id", id);
        return template.query(sql, param, settlementRowMapper());
    }

    public Optional<Member> authenticate(String user_id, String password) {
        String sql = "select * from Member where user_id = :user_id";
        try {
            Map<String, Object> param = Map.of("user_id", user_id);
            Member member = template.queryForObject(sql, param, rowMapper());
            if (member.getUser_pw().equals(password)) {
                return Optional.of(member);
            } else {
                throw new IllegalStateException("Password Incorrect");
            }
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Member> findAll() {
        String sql = "select * from Member";
        return template.query(sql, rowMapper());
    }

    public RowMapper<Member> rowMapper() {
        return BeanPropertyRowMapper.newInstance(Member.class);
    }

    public RowMapper<Settlement> settlementRowMapper() {
        return BeanPropertyRowMapper.newInstance(Settlement.class);
    }
}
