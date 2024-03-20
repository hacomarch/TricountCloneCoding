package org.example.tricountcloneproject.settlement;

import lombok.RequiredArgsConstructor;
import org.example.tricountcloneproject.member.Member;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/settlement")
public class SettlementController {

    private final SettlementService settlementService;

    @ResponseBody
    @PostMapping("/save")
    public String save(@RequestBody Settlement settlement) {
        settlementService.insert(settlement);
        return "settlement save ok";
    }

    @ResponseBody
    @DeleteMapping("/{settlementId}")
    public String delete(@PathVariable Long settlementId) {
        settlementService.delete(settlementId);
        return "settlement delete ok";
    }

    @ResponseBody
    @GetMapping("/{settlementId}")
    public Settlement findById(@PathVariable Long settlementId) {
        return settlementService.findById(settlementId);
    }

    @ResponseBody
    @GetMapping("/{settlementId}/members")
    public List<Member> findMembersById(@PathVariable Long settlementId) {
        return settlementService.findMembersById(settlementId);
    }

    @ResponseBody
    @GetMapping
    public List<Settlement> findAll() {
        return settlementService.findAll();
    }

    @ResponseBody
    @GetMapping("/{settlementId}/result")
    public List<SettlementResponse> findResult(@PathVariable Long settlementId) {
        return settlementService.getSettlementResponses(settlementId);
    }
}
