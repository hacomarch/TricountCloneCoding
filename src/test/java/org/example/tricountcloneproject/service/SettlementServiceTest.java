package org.example.tricountcloneproject.service;

import org.example.tricountcloneproject.entity.Settlement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SettlementServiceTest {

    @Autowired
    SettlementService settlementService;

    static Settlement settlement1;
    static Settlement settlement2;

    @BeforeAll
    static void init() {
        settlement1 = new Settlement();
        settlement1.setName("강릉 여행 정산");

        settlement2 = new Settlement();
        settlement2.setName("한강 나들이 정산");
    }

    @Test
    @DisplayName("Save")
    void save() {
        assertEquals(0, settlementService.findAll().size());

        settlementService.insert(settlement1);
        settlementService.insert(settlement2);

        assertEquals(2, settlementService.findAll().size());
    }

    @Test
    @DisplayName("Delete")
    void delete() {
        assertEquals(2, settlementService.findAll().size());

        settlementService.delete(2L);

        assertEquals(1, settlementService.findAll().size());
    }

    @Test
    @DisplayName("FindById")
    void findById() {
        Settlement findSettlement = settlementService.findById(1L);

        assertEquals(1L, findSettlement.getSettlementId());
        assertEquals("강릉 여행 정산", findSettlement.getName());
    }

    @Test
    @DisplayName("FindAll")
    void findAll() {
        settlementService.insert(settlement2);
        assertEquals(2, settlementService.findAll().size());
    }
}