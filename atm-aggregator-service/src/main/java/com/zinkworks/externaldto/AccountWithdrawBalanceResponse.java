package com.zinkworks.externaldto;

import lombok.Data;

@Data
public class AccountWithdrawBalanceResponse {
    public Double availableBalance;
    public Double overDraftBalance;
    public String errorCode;
    public String errorMessage;
}
