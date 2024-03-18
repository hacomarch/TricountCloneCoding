package org.example.tricountcloneproject.repository;

import org.example.tricountcloneproject.entity.Member;
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
}
