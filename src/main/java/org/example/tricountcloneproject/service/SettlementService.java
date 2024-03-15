package org.example.tricountcloneproject.service;

import lombok.RequiredArgsConstructor;
import org.example.tricountcloneproject.entity.Settlement;
import org.example.tricountcloneproject.repository.SettlementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SettlementService {
    private final SettlementRepository settlementRepository;

    public void insert(Settlement settlement) {
        settlementRepository.save(settlement);
    }

    public void delete(Long id) {
        settlementRepository.delete(id);
    }

    public Settlement findById(Long id) {
        return settlementRepository.findById(id).get();
    }

    public List<Settlement> findAll() {
        return settlementRepository.findAll();
    }
}
