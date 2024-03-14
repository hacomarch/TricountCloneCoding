package org.example.tricountcloneproject.entity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Expense {
    private Long expense_id;
    private Long member_id;
    private Long settlement_id;
    private String name;
    private BigDecimal amount;
    private LocalDateTime dateTime;
}
