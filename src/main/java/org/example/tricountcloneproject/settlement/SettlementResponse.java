package org.example.tricountcloneproject.settlement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SettlementResponse {
    private Long senderUserNo;
    private String senderUserName;
    private int sendAmount;
    private Long receiverUserNo;
    private String receiverUserName;
}
