package com.zinkworks.dto;

import lombok.Data;

@Data
public class ATMWithdrawBalanceResponse {
    public DenominationCount denominationCount;
    public AccountWithdrawableDetails accountWithdrawableDetails;
    public String errorCode;
    public String errorMessage;
}
