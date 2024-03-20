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
        member.setMemberId(key.longValue());
    }

    public void delete(Long memberId) {
        String expenseSql = "delete from Expense where member_id = :id";
        template.update(expenseSql, Map.of("id", memberId));

        String memberSql = "delete from Member where member_id = :id";
        template.update(memberSql, Map.of("id", memberId));
    }

    public Optional<Member> findById(Long memberId) {
        String sql = "select * from Member where member_id=:id";
        try {
            return Optional.of(template.queryForObject(sql, Map.of("id", memberId), memberRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public String findNicknameById(Long memberId) {
        String sql = "select nickname from Member where member_id = :id";
        return template.queryForObject(sql, Map.of("id", memberId), String.class);
    }

    //멤버 1명이 갖고 있는 정산 가져오기
    public List<Settlement> findSettlementsById(Long memberId) {
        String sql = "select distinct s.settlement_id, s.name" +
                " from Member m" +
                " join Expense e on m.member_id = e.member_id" +
                " join Settlement s on e.settlement_id = s.settlement_id" +
                " where m.member_id = :id";
        return template.query(sql, Map.of("id", memberId), settlementRowMapper());
    }

    public Optional<Member> findByUserId(String userId) {
        String sql = "select * from Member where user_id = :id";
        try {
            return Optional.of(template.queryForObject(sql, Map.of("id", userId), memberRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Member> findAll() {
        String sql = "select * from Member";
        return template.query(sql, memberRowMapper());
    }

    public RowMapper<Member> memberRowMapper() {
        return BeanPropertyRowMapper.newInstance(Member.class);
    }

    public RowMapper<Settlement> settlementRowMapper() {
        return BeanPropertyRowMapper.newInstance(Settlement.class);
    }
}
