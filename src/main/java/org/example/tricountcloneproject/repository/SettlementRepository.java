package org.example.tricountcloneproject.repository;

import org.example.tricountcloneproject.entity.*;
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
import java.math.RoundingMode;
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
        settlement.setSettlement_id(key.longValue());
    }

    public void delete(Long settlement_id) {
        String sql = "delete from Settlement where settlement_id = :id";
        Map<String, Object> param = Map.of("id", settlement_id);
        template.update(sql, param);
    }

    public List<Settlement> findAll() {
        String sql = "select * from Settlement";
        return template.query(sql, rowMapper());
    }

    public Optional<Settlement> findById(Long settlement_id) {
        String sql = "select * from Settlement where settlement_id = :id";
        try {
            Map<String, Object> param = Map.of("id", settlement_id);
            Settlement settlement = template.queryForObject(sql, param, rowMapper());
            return Optional.of(settlement);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    //정산에 참여한 멤버 가져오기
    public List<Member> findMembersById(Long settlement_id) {
        String sql = "select distinct m.member_id, m.user_id, m.user_pw, m.nickname" +
                " from Settlement s" +
                " join Expense e on s.settlement_id = e.settlement_id" +
                " join Member m on e.member_id = m.member_id" +
                " where s.settlement_id = :id";
        Map<String, Object> param = Map.of("id", settlement_id);
        return template.query(sql, param, memberRowMapper());
    }

    //TODO : 정산 결과 구해서 매핑하기
    public List<SettlementResponse> getBalance(Long settlement_id) {
        BigDecimal balanceAmount = getBalanceAmount(settlement_id);//얼마씩 내야하는지
        Map<Long, BigDecimal> totalAmountForSettlement = getTotalAmountForSettlement(settlement_id);
        if (isSameAmount(totalAmountForSettlement)) {
            return List.of();
        }
        List<SettlementResponse> settlementResponses = new ArrayList<>();

        //멤버 각각이 얼마나 지출하거나 받아야 하는지 계산
        for (Map.Entry<Long, BigDecimal> entry : totalAmountForSettlement.entrySet()) {
            Long memberId = entry.getKey();
            BigDecimal totalAmount = entry.getValue(); //멤버 1명이 총 지출한 금액
            BigDecimal difference = balanceAmount.subtract(totalAmount); //얼마를 내야 하는지 or 얼마를 받아야 하는지

            if (difference.compareTo(BigDecimal.ZERO) > 0) { //보내야 하는 경우
                createSettlementResponses(totalAmountForSettlement, memberId, difference, settlementResponses);
            }
        }
        return settlementResponses;
    }

    private boolean isSameAmount(Map<Long, BigDecimal> map) {
        Iterator<BigDecimal> iterator = map.values().iterator();
        BigDecimal first = iterator.next();
        while (iterator.hasNext()) {
            BigDecimal current = iterator.next();
            if (!first.equals(current)) return false;
        }
        return true;
    }

    //응답값 만들기
    private void createSettlementResponses(Map<Long, BigDecimal> totalAmountForSettlement,
                                           Long memberId, BigDecimal difference,
                                           List<SettlementResponse> settlementResponses) {

        for (Map.Entry<Long, BigDecimal> receiverEntity : totalAmountForSettlement.entrySet()) {
            Long receiverId = receiverEntity.getKey();
            BigDecimal receiverAmount = receiverEntity.getValue();

            if (!memberId.equals(receiverId) && difference.compareTo(BigDecimal.ZERO) > 0
                    && totalAmountForSettlement.get(memberId).compareTo(BigDecimal.ZERO) != 0
                    && receiverAmount.compareTo(BigDecimal.ZERO) != 0) {
                totalAmountForSettlement.put(memberId, BigDecimal.ZERO);
                totalAmountForSettlement.put(receiverId, BigDecimal.ZERO);

                BigDecimal transferAmount = difference.min(receiverAmount); //보낼 금액
                settlementResponses.add(createSettlement(memberId, transferAmount, receiverId));
                difference = difference.subtract(transferAmount);
            }
        }
    }

    private SettlementResponse createSettlement(Long memberId, BigDecimal transferAmount, Long receiverId) {
        return new SettlementResponse(memberId, findNicknameById(memberId), transferAmount.intValue(),
                receiverId, findNicknameById(receiverId));
    }

    private String findNicknameById(Long member_id) {
        String sql = "select nickname from Member where member_id = :id";
        Map<String, Object> param = Map.of("id", member_id);
        return template.queryForObject(sql, param, String.class);
    }

    //A정산에서 멤버별로 지출한 총 금액 계산
    private Map<Long, BigDecimal> getTotalAmountForSettlement(Long settlement_id) {
        String sql = "select member_id, sum(amount) as total_amount" +
                " from Expense" +
                " where settlement_id = :settlement_id" +
                " group by member_id";
        Map<String, Object> param = Map.of("settlement_id", settlement_id);
        return template.query(sql, param, rs -> {
            HashMap<Long, BigDecimal> totalAmounts = new HashMap<>();
            while (rs.next()) {
                Long memberId = rs.getLong("member_id");
                BigDecimal totalAmount = rs.getBigDecimal("total_amount");
                totalAmounts.put(memberId, totalAmount);
            }
            return totalAmounts;
        });
    }


    //A정산에서 (총 지출 금액 / 참가자 수) 해서 각각 얼마씩 내야 하는지 구하기
    private BigDecimal getBalanceAmount(Long settlement_id) {
        return totalExpenseAmount(settlement_id)
                .divide(BigDecimal.valueOf(totalMemberCount(settlement_id)), RoundingMode.UNNECESSARY)
                .setScale(0, RoundingMode.HALF_UP);
    }

    //정산에 참여한 총 참가자수
    private int totalMemberCount(Long settlement_id) {
        String sql = "select count(distinct m.member_id) as member_count" +
                " from Settlement s" +
                " join Expense e on s.settlement_id = e.settlement_id" +
                " join Member m on e.member_id = m.member_id" +
                " where s.settlement_id = :id";
        Map<String, Object> param = Map.of("id", settlement_id);
        return template.queryForObject(sql, param, Integer.class);
    }

    //정산에서 발생한 총 지출 금액
    private BigDecimal totalExpenseAmount(Long settlement_id) {
        List<Expense> expenses = findBySettlementId(settlement_id);
        return expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(0, RoundingMode.HALF_UP);
    }

    //A정산이 갖고 있는 지출 내역 가져오기
    private List<Expense> findBySettlementId(Long settlement_id) {
        String sql = "select * from Expense where settlement_id = :id";
        Map<String, Object> param = Map.of("id", settlement_id);
        return template.query(sql, param, expenseRowMapper());
    }

    public RowMapper<Settlement> rowMapper() {
        return BeanPropertyRowMapper.newInstance(Settlement.class);
    }

    public RowMapper<Member> memberRowMapper() {
        return BeanPropertyRowMapper.newInstance(Member.class);
    }

    public RowMapper<Expense> expenseRowMapper() {
        return BeanPropertyRowMapper.newInstance(Expense.class);
    }
}
