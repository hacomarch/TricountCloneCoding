package org.example.tricountcloneproject.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Balance {
    private Long balance_id;
    private Long sender_id;
    private Long receiver_id;
    private BigDecimal amount;
}
