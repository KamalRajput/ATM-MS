package com.zinkworks.externaldto;

import lombok.Data;

@Data
public class AccountCheckBalanceResponse {
	public Double availableBalance;
    public Double maxWithdrawableBalance;
	public String errorCode;
	public String errorMessage;
}
