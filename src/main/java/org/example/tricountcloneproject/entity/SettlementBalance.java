package org.example.tricountcloneproject.entity;

import lombok.Data;

import java.util.List;

@Data
public class SettlementBalance {
    private Settlement settlement;
    private List<Balance> balances;
}
