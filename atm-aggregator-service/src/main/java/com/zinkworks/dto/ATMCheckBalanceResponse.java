package com.zinkworks.dto;

import lombok.Data;

@Data
public class ATMCheckBalanceResponse {
    public Double availableBalance;
    public Double maxWithdrawableBalance;
    public String errorCode;
    public String errorMessage;
}
