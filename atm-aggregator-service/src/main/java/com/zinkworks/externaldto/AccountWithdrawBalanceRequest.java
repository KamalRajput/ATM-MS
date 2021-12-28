package com.zinkworks.externaldto;

import lombok.Data;

@Data
public class AccountWithdrawBalanceRequest {
    public int atmId;
    public String hashedAccountNumber;
    public Double amount;
}
