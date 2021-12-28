package com.zinkworks.dto;

import lombok.Data;

@Data
public class AccountBalanceRevertRequest {

	public String hashedAccountNumber;
	public double amount;
}
