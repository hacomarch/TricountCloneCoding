package org.example.tricountcloneproject.settlement;

import lombok.RequiredArgsConstructor;
import org.example.tricountcloneproject.exception.EntityNotFoundException;
import org.example.tricountcloneproject.exception.ExpenseAccessDeniedException;
import org.example.tricountcloneproject.expense.Expense;
import org.example.tricountcloneproject.member.Member;
import org.example.tricountcloneproject.member.MemberRepository;
import org.example.tricountcloneproject.settlement.response.SettlementResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SettlementService {
    private final SettlementRepository settlementRepository;
    private final MemberRepository memberRepository;

    public void insert(Settlement settlement) {
        settlementRepository.save(settlement);
    }

    public void delete(Long settlementId) {
        settlementRepository.delete(settlementId);
    }

    public Settlement findById(Long settlementId) {
        return settlementRepository.findById(settlementId)
                .orElseThrow(() -> new EntityNotFoundException("Settlement"));
    }

    public List<Member> findMembersById(Long settlementId) {
        return settlementRepository.findMembersById(settlementId);
    }

    public List<Expense> findExpensesById(Long settlementId, Long memberId) {
        List<Expense> expenses = settlementRepository.findExpensesById(settlementId);
        boolean match = expenses.stream()
                .anyMatch(expense -> expense.getMemberId().equals(memberId));
        if (!match) {
            throw new ExpenseAccessDeniedException();
        }
        return expenses;
    }

    public List<Settlement> findAll() {
        return settlementRepository.findAll();
    }

    public List<SettlementResponse> getSettlementResponses(Long settlementId) {
        //멤버 1명이 얼마씩 내야 하는지
        BigDecimal balanceAmount = calculateBalanceAmount(settlementId);

        //멤버별 정산에서 총 지출한 금액 맵 (memberId, 총 지출 금액)
        Map<Long, BigDecimal> totalExpensesByMember =
                settlementRepository.getTotalAmountByMember(settlementId);

        //모두 같은 금액을 지출했다면, 빈 리스트 반환
        if (allMembersExpenseSameAmount(totalExpensesByMember)) {
            return Collections.emptyList();
        }

        return generateSettlementResponses(balanceAmount, totalExpensesByMember);
    }

    //정산에 참여한 참가자들이 모두 같은 금액을 지출했는지 확인하는 메서드
    private boolean allMembersExpenseSameAmount(Map<Long, BigDecimal> map) {
        BigDecimal first = map.values().stream().findFirst().orElse(BigDecimal.ZERO);
        return map.values().stream().allMatch(amount -> amount.compareTo(first) == 0);
    }

    private List<SettlementResponse> generateSettlementResponses(BigDecimal balanceAmount,
                                                                 Map<Long, BigDecimal> expensesByMember) {
        List<SettlementResponse> responses = new ArrayList<>();

        expensesByMember.forEach((memberId, totalSpent) -> {
            if (balanceAmount.compareTo(totalSpent) > 0) { //정산 금액 > 총 지출 금액 == 더 내야 할 경우
                adjustMemberSettlements(memberId, totalSpent, balanceAmount, expensesByMember, responses);
            }
        });

        return responses;
    }

    private void adjustMemberSettlements(Long senderId,
                                         BigDecimal senderTotalExpense, BigDecimal balanceAmount,
                                         Map<Long, BigDecimal> expensesByMember,
                                         List<SettlementResponse> responses) {

        for (Map.Entry<Long, BigDecimal> entry : expensesByMember.entrySet()) {
            // 보낼 금액이 정산 금액과 같아지면 다 보냈다는 것과 같으니 끝.
            if (senderTotalExpense.compareTo(balanceAmount) == 0) break;

            Long receiverId = entry.getKey();
            BigDecimal receiverTotalExpense = entry.getValue();

            // 지출한 금액 > 정산 금액 == 받아야 하는 멤버
            if (entry.getValue().compareTo(balanceAmount) > 0) {
                //보낼 수 있는 금액
                BigDecimal amountNeededToSend = balanceAmount.subtract(senderTotalExpense);
                //받을 수 있는 금액
                BigDecimal amountNeededToReceive = balanceAmount.subtract(receiverTotalExpense).abs();

                //송금자가 보내야 하는 금액과 받는 사람이 받아야 하는 금액 중 작은 금액을 결정해서 송금 금액으로 저장
                BigDecimal sendAmount = amountNeededToSend.min(amountNeededToReceive);

                responses.add(createSettlement(senderId, sendAmount, receiverId));
                senderTotalExpense = senderTotalExpense.add(sendAmount);
                expensesByMember.put(senderId, senderTotalExpense);
                expensesByMember.put(receiverId, receiverTotalExpense.subtract(sendAmount));
            }
        }
    }

    private SettlementResponse createSettlement(Long senderId, BigDecimal sendAmount, Long receiverId) {
        return new SettlementResponse(
                senderId, memberRepository.findNicknameById(senderId),
                sendAmount.intValue(),
                receiverId, memberRepository.findNicknameById(receiverId)
        );
    }

    //정산에서 (총 지출 금액 / 참가자 수) 해서 각각 얼마씩 내야 하는지 구하기
    private BigDecimal calculateBalanceAmount(Long settlementId) {
        return totalExpenseAmount(settlementId)
                .divide(settlementRepository.totalMemberCount(settlementId), RoundingMode.HALF_UP);
    }

    //정산에서 발생한 총 지출 금액 계산하기
    private BigDecimal totalExpenseAmount(Long settlementId) {
        List<BigDecimal> expenses = settlementRepository.getExpensesById(settlementId);
        return expenses.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(0, RoundingMode.HALF_UP);
    }
}
