package com.zinkworks.dto;

import lombok.Data;

@Data
public class AccountWithdrawableDetails {
    //public long trxId;
    public Double availableBalance;
    public Double overDraftBalance;
    
    public String errorCode;
    public String errorMessage;

}
