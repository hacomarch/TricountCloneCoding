package org.example.tricountcloneproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.tricountcloneproject.entity.Settlement;
import org.example.tricountcloneproject.service.SettlementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    @ResponseBody
    @PostMapping("/settlement/save")
    public String save(@RequestBody Settlement settlement) {
        settlementService.insert(settlement);
        return "settlement save ok";
    }

    @ResponseBody
    @GetMapping("/settlement/delete/{id}")
    public String delete(@PathVariable Long id) {
        settlementService.delete(id);
        return "settlement delete ok";
    }

    @ResponseBody
    @GetMapping("/settlement/{id}")
    public Settlement findById(@PathVariable Long id) {
        return settlementService.findById(id);
    }

    @ResponseBody
    @GetMapping("/settlement/list")
    public List<Settlement> findAll() {
        return settlementService.findAll();
    }
}
