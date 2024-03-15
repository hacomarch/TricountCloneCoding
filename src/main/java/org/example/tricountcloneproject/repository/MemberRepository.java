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
public class MemberRepository implements CRDRepository<Member> {
    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("Member")
                .usingGeneratedKeyColumns("member_id");
    }

    @Override
    public void save(Member member) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(member);
        Number key = jdbcInsert.executeAndReturnKey(param);
        member.setMember_id(key.longValue());
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from Member where member_id = :id";
        Map<String, Object> param = Map.of("id", id);
        template.update(sql, param);
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select member_id, user_id, user_pw, nickname" +
                " from Member where member_id=:id";
        try {
            Map<String, Object> param = Map.of("id", id);
            Member member = template.queryForObject(sql, param, rowMapper());
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "select member_id, user_id, user_pw, nickname from Member";
        return template.query(sql, rowMapper());
    }

    @Override
    public RowMapper<Member> rowMapper() {
        return BeanPropertyRowMapper.newInstance(Member.class);
    }
}
