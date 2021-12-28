package com.zinkworks.externaldto;

import lombok.Data;

@Data
public class RevertBalanceRequest {
	
	public String hashedAccountNumber;
	public double amount;

}
