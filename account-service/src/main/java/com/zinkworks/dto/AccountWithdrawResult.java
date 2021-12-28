package com.zinkworks.dto;

import lombok.Data;

@Data
public class AccountWithdrawResult {

	public Double availableBalance;
	public Double overDraftBalance;
	public String errorCode;
	public String errorMessage;

}
