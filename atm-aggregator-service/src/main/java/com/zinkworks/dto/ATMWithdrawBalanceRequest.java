package com.zinkworks.dto;

import lombok.Data;

@Data
public class ATMWithdrawBalanceRequest {
    public int atmId;
    public String hashedAccountNumber;
    public Double amount;
}
