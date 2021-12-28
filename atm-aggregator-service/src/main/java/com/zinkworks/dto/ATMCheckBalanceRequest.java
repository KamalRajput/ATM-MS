package com.zinkworks.dto;

import lombok.Data;

@Data
public class ATMCheckBalanceRequest {
    public String hashedAccountNumber;
}
