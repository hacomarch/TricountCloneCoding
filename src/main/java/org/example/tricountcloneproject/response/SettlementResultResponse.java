package org.example.tricountcloneproject.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SettlementResultResponse {
    private Long senderUserNo;
    private String senderUserName;
    private int sendAmount;
    private Long receiverUserNo;
    private String receiverUserName;
}
